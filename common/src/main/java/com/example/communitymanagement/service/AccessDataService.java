package com.example.communitymanagement.service;

import com.alibaba.fastjson.JSONArray;
import com.example.communitymanagement.dao.AccessDataRepository;
import com.example.communitymanagement.dao.ResidentRepository;
import com.example.communitymanagement.entity.AccessData;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
@Transactional(rollbackOn = Exception.class)
public class AccessDataService {
    @Resource
    AccessDataRepository accessDataRepository;

    @Resource
    ResidentRepository residentRepository;

    public String getAllAccessDataPage(int pageNum,int size,String start, String end, String sorted){
        Pageable pageable = PageRequest.of(pageNum-1,size);

        if("desc".equals(sorted)) {
            return JSONArray.toJSONString(accessDataRepository.findAllBetweenDESC(start, end, pageable));
        }
        else{
            return JSONArray.toJSONString(accessDataRepository.findAllBetweenASC(start, end, pageable));
        }
    }

    public String getTagAccessDataPage(int pageNum,int size, String tag, String start, String end, String sorted){
        Pageable pageable = PageRequest.of(pageNum-1,size);

        if("normal".equals(tag)) {
            if("desc".equals(sorted)) {
                return JSONArray.toJSONString(accessDataRepository.findAllNormalBetweenDesc(start,end,pageable));
            }
            else{
                return JSONArray.toJSONString(accessDataRepository.findAllNormalBetweenAsc(start,end,pageable));
            }
        }
        else{
            if("desc".equals(sorted)) {
                return JSONArray.toJSONString(accessDataRepository.findAllHighBetweenDesc(start,end,pageable));
            }
            else{
                return JSONArray.toJSONString(accessDataRepository.findAllHighBetweenAsc(start,end,pageable));
            }
        }
    }

    public List<AccessData> getAllAccessDataList(String start, String end, String sorted){

        if("desc".equals(sorted)) {
            return accessDataRepository.findAllListBetweenDESC(start, end);
        }
        else{
            return accessDataRepository.findAllListBetweenASC(start, end);
        }
    }

    public List<AccessData> getTagAccessDataList( String tag, String start, String end, String sorted){

        if("normal".equals(tag)) {
            if("desc".equals(sorted)) {
                return accessDataRepository.findAllNormalListBetweenDesc(start,end);
            }
            else{
                return accessDataRepository.findAllNormalListBetweenAsc(start,end);
            }
        }
        else{
            if("desc".equals(sorted)) {
                return accessDataRepository.findAllHighListBetweenDesc(start,end);
            }
            else{
                return accessDataRepository.findAllHighListBetweenAsc(start,end);
            }
        }
    }

    public boolean insertTemperature(Map<String,String> map){
        AccessData data = AccessData.builder().checkerId(Long.valueOf(map.get("checkerId")))
                .checkerName(map.get("checkerName"))
                .createTime(getNowDate())
                .residentName(map.get("residentName"))
                .temperature(Double.valueOf(map.get("temperature")))
                .residentId(Long.valueOf(map.get("residentId")))
                .build();
        Long residentId = Long.valueOf(map.get("residentId"));
        AccessData data2 = accessDataRepository.save(data);
        residentRepository.updateTotalCheckTimesByResidentId(residentId);
        if(accessDataRepository.countHighAccessDataByResidentId(
                residentId,getPastDate(7),getNowDate()
        )>=3){
            residentRepository.updateStatusByResidentId(1,residentId);
        }
        return true;
    }

    public List<AccessData> searchTodayEnter(String id){
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd" );
        try {
            Date today = sdf.parse("2010-01-01");
            return accessDataRepository.findTodayEnter(Long.valueOf(id),today);
        } catch (ParseException e) {
            System.out.println(e);
        }
        return null;

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
