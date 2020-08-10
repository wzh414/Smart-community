package com.example.communitymanagement.controller;

import com.example.communitymanagement.service.UserService;
import com.sun.istack.NotNull;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/check/data")
@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMIN')")
public class CheckController {
    @Autowired
    UserService userService;

    @RequestMapping("/listAllCheck")
    public String listAllAdmin(@NotNull @RequestParam("pageNum") int pageNum, @NotNull @RequestParam("search") String search){
        if(Strings.isBlank(search)){
            return userService.getCheckList(pageNum,10);
        }
        else{
            return userService.getCheckListBySearch(pageNum,10,search);
        }
    }

    @RequestMapping("/addOneCheck")
    public String addOneAdmin(@NotNull @RequestParam("username") String username, @NotNull @RequestParam("password")String password, @NotNull @RequestParam("nickname")String nickname){
        if(userService.findOne(username)!=null){
            return "exits";
        }
        else{
            return userService.addOne(username,password,nickname,"ROLE_CHECK");
        }
    }

    @RequestMapping("/modifyCheckPass")
    public String modifyCheckPass(@NotNull @RequestParam("userId") String userId, @NotNull @RequestParam("password") String password){
        return userService.modifyCheckPass(Long.parseLong(userId),password);
    }

    @RequestMapping("/modifyCheckNickname")
    public String modifyCheckNickname(@NotNull @RequestParam("userId") String userId, @NotNull @RequestParam("nickname") String nickname){
        return userService.modifyCheckNick(Long.parseLong(userId),nickname);
    }

    @RequestMapping("/disableCheck")
    public String disableCheck(@NotNull @RequestParam("userId") String userId){
        return userService.modifyCheckStatus(Long.parseLong(userId),1);
    }

    @RequestMapping("/enableCheck")
    public String enableCheck(@NotNull @RequestParam("userId") String userId){
        return userService.modifyCheckStatus(Long.parseLong(userId),0);
    }

    @RequestMapping("/deleteCheck")
    public String deleteCheck(@NotNull @RequestParam("userId") String userId){
        return userService.deleteCheck(Long.parseLong(userId));
    }
}
