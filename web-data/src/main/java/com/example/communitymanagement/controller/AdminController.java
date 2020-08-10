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
@RequestMapping("/admin/data")
@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
public class AdminController {
    @Autowired
    UserService userService;

    @RequestMapping("/listAllAdmin")
    public String listAllAdmin(@NotNull @RequestParam("pageNum") int pageNum, @NotNull @RequestParam("search") String search){
        if(Strings.isBlank(search)){
            return userService.getAdminList(pageNum,10);
        }
        else{
            return userService.getAdminListBySearch(pageNum,10,search);
        }
    }

    @RequestMapping("/addOneAdmin")
    public String addOneAdmin(@NotNull @RequestParam("username") String username, @NotNull @RequestParam("password")String password, @NotNull @RequestParam("nickname")String nickname){
        if(userService.findOne(username)!=null){
            return "exits";
        }
        else{
            return userService.addOne(username,password,nickname,"ROLE_ADMIN");
        }
    }

    @RequestMapping("/modifyAdminPass")
    public String modifyAdminPass(@NotNull @RequestParam("userId") String userId, @NotNull @RequestParam("password") String password){
        return userService.modifyAdminPass(Long.parseLong(userId),password);
    }

    @RequestMapping("/modifyAdminNickname")
    public String modifyAdminNickname(@NotNull @RequestParam("userId") String userId, @NotNull @RequestParam("nickname") String nickname){
        return userService.modifyAdminNick(Long.parseLong(userId),nickname);
    }

    @RequestMapping("/disableAdmin")
    public String disableAdmin(@NotNull @RequestParam("userId") String userId){
        return userService.modifyAdminStatus(Long.parseLong(userId),1);
    }

    @RequestMapping("/enableAdmin")
    public String enableAdmin(@NotNull @RequestParam("userId") String userId){
        return userService.modifyAdminStatus(Long.parseLong(userId),0);
    }

    @RequestMapping("/deleteAdmin")
    public String deleteAdmin(@NotNull @RequestParam("userId") String userId){
        return userService.deleteAdmin(Long.parseLong(userId));
    }

}
