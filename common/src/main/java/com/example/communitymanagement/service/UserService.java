package com.example.communitymanagement.service;

import com.alibaba.fastjson.JSONArray;
import com.example.communitymanagement.dao.AccessDataRepository;
import com.example.communitymanagement.dao.UserRepository;
import com.example.communitymanagement.entity.SysUser;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import javax.transaction.Transactional;


@Service
@Transactional(rollbackOn = Exception.class)
public class UserService {
    @Resource
    UserRepository userRepository;
    @Resource
    AccessDataRepository accessDataRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String getAdminList(int pageNum,int size){
        Sort sort = Sort.by(Sort.Direction.ASC, "userId");
        Pageable pageable = PageRequest.of(pageNum-1,size,sort);

        return JSONArray.toJSONString(userRepository.findByUserRole("ROLE_ADMIN",pageable));
    }

    public String getAdminListBySearch(int pageNum,int size,String search){
        Pageable pageable = PageRequest.of(pageNum-1,size);

        return JSONArray.toJSONString(userRepository.findAdminBySearch(search, search, pageable));
    }

    public String getCheckList(int pageNum,int size){
        Sort sort = Sort.by(Sort.Direction.ASC, "userId");
        Pageable pageable = PageRequest.of(pageNum-1,size,sort);

        return JSONArray.toJSONString(userRepository.findByUserRole("ROLE_CHECK",pageable));
    }

    public String getCheckListBySearch(int pageNum,int size,String search){
        Pageable pageable = PageRequest.of(pageNum-1,size);

        return JSONArray.toJSONString(userRepository.findCheckBySearch(search, search, pageable));
    }

    public SysUser findOne(String username){
        return userRepository.findByUsername(username);
    }

    public String addOne(String username, String password, String nickname, String role){
        SysUser user = SysUser.builder()
                .username(username)
                .passwd(passwordEncoder.encode(password))
                .nickname(nickname)
                .userRole(role)
                .build();
        try{
            userRepository.save(user);
            return "success";
        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "error";
        }
    }

    public String modifyAdminPass(Long userId,String newPassword){
        try{
            userRepository.updateUserPasswordByUserId(passwordEncoder.encode(newPassword),userId,"ROLE_ADMIN");
            return "success";
        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "error";
        }
    }

    public String modifyCheckPass(Long userId,String newPassword){
        try{
            userRepository.updateUserPasswordByUserId(passwordEncoder.encode(newPassword),userId,"ROLE_CHECK");
            return "success";
        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "error";
        }
    }

    public String modifyAdminNick(Long userId,String nickname){
        try{
            userRepository.updateUserNicknameByUserId(nickname,userId,"ROLE_ADMIN");
            return "success";
        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "error";
        }
    }

    public String modifyCheckNick(Long userId,String nickname){
        try{
            userRepository.updateUserNicknameByUserId(nickname,userId,"ROLE_CHECK");
            accessDataRepository.modifyCheckerName(nickname,userId);
            return "success";
        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "error";
        }
    }

    public String modifyAdminStatus(Long userId,int status){
        try{
            userRepository.updateUserStatusByUserId(status,userId,"ROLE_ADMIN");
            return "success";
        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "error";
        }
    }

    public String modifyCheckStatus(Long userId,int status){
        try{
            userRepository.updateUserStatusByUserId(status,userId,"ROLE_CHECK");
            return "success";
        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "error";
        }
    }

    public String deleteAdmin(Long userId){
        try{
            userRepository.deleteByUserIdAndUserRole(userId,"ROLE_ADMIN");
            return "success";

        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "error";
        }
    }

    public String deleteCheck(Long userId){
        try{
            userRepository.deleteByUserIdAndUserRole(userId,"ROLE_CHECK");
            return "success";

        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "error";
        }
    }
}
