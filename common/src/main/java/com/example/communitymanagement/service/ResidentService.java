package com.example.communitymanagement.service;

import com.alibaba.fastjson.JSONArray;
import com.example.communitymanagement.dao.AccessDataRepository;
import com.example.communitymanagement.dao.ResidentRepository;
import com.example.communitymanagement.entity.SysResident;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;


@Service
@Transactional(rollbackOn = Exception.class)
public class ResidentService {
    @Autowired
    ResidentRepository residentRepository;
    @Autowired
    AccessDataRepository accessDataRepository;

    public String hasResident(String openid){
        SysResident resident = residentRepository.findByWxOpenid(openid);
        //用户未注册
        if(resident == null){
            return "notExists";
        }
        //用户已注册但被锁定
        else if (resident.getLocked() == 1){
            return "locked";
        }
        //用户已注册但未被锁定
        else{
            return "exists";
        }
    }

    public String saveResident(String openid,String avatar,String residentName,String idCardNumber,String homeNumber,String phoneNumber){
        try{
            residentRepository.save(
                    SysResident.builder()
                    .wxOpenid(openid)
                    .avatar(avatar)
                    .residentName(residentName)
                    .idCardNumber(idCardNumber)
                    .homeNumber(homeNumber)
                    .phoneNumber(phoneNumber)
                    .build()
            );
            return "success";
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "error";
        }
    }

    public SysResident findOneByWxOpenid(String openid){
        return residentRepository.findByWxOpenid(openid);
    }

    public SysResident findOneByResidentId(String resident){
        Long residentId = Long.valueOf(resident);
        return residentRepository.findByResidentId(residentId);
    }

    public String getResidentList(int pageNum,int size){
        Sort sort = Sort.by(Sort.Direction.ASC, "residentId");
        Pageable pageable = PageRequest.of(pageNum-1,size,sort);

        return JSONArray.toJSONString(residentRepository.findAll(pageable));
    }

    public String getResidentListBySearch(int pageNum,int size,String search){
        Pageable pageable = PageRequest.of(pageNum-1,size);

        return JSONArray.toJSONString(residentRepository.findByResidentNameLike(search, pageable));
    }

    public String modifyResidentStatus(Long residentId,int status){
        try{
            residentRepository.updateStatusByResidentId(status,residentId);
            return "success";
        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "error";
        }
    }

    public String deleteResident(Long residentId){
        try{
            residentRepository.deleteById(residentId);
            accessDataRepository.deleteAccessDataByResidentId(residentId);
            return "success";
        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "error";
        }
    }

    public void setStatus(SysResident resident){
        if(accessDataRepository.countHighAccessDataByResidentId(
                resident.getResidentId(),getPastDate(7),getNowDate()
        )<3 && resident.getLocked() == 1){
            residentRepository.updateStatusByResidentId(0,resident.getResidentId());
        }
    }

    public Date getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        return calendar.getTime();

    }

    public Date getNowDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }
}
