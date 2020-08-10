package com.example.communitymanagement.controller;

import com.alibaba.fastjson.JSON;
import com.example.communitymanagement.dao.UserRepository;
import com.example.communitymanagement.entity.SysUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@Controller
public class RouteController {
    @Resource
    UserRepository userRepository;

    @RequestMapping("/test")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    public String test(){
        return "hehe";
    }

    @RequestMapping("/getUserInfo")
    @ResponseBody
    public String getUserInfo(){
        Map<String,Object> map = new HashMap<>();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SysUser user = userRepository.findByUsername(username);
        map.put("nickname", user.getNickname());
        map.put("role", user.getUserRole());

        return JSON.toJSONString(map);
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping({"/","/home"})
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMIN')")
    public String home(){
        return "home";
    }

    @RequestMapping("/index")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMIN')")
    public String index(){
        return "index";
    }

    @RequestMapping("/ManageAdmin")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    public String manageAdmin(){
        return "ManageAdmin";
    }

    @RequestMapping("/ManageCheck")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMIN')")
    public String manageCheck(){
        return "ManageCheck";
    }

    @RequestMapping("/ManageResident")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMIN')")
    public String manageResident(){
        return "ManageResident";
    }

    @RequestMapping("/AccessDataDetail")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMIN')")
    public String accessDataDetail(){
        return "AccessDataDetail";
    }

    @RequestMapping("/HealthDataDetail")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMIN')")
    public String healthDataDetail(){
        return "HealthDataDetail";
    }
}
