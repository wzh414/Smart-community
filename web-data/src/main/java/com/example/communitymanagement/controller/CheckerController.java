package com.example.communitymanagement.controller;

import com.example.communitymanagement.entity.SysUser;
import com.example.communitymanagement.exception.AjaxResponse;
import com.example.communitymanagement.security.jwt.JwtAuthService;
import com.example.communitymanagement.service.AccessDataService;
import com.example.communitymanagement.service.HealthDataService;
import com.example.communitymanagement.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@PreAuthorize("hasAnyRole('ROLE_CHECK')")
public class CheckerController {

    @Autowired
    JwtAuthService jwtAuthService;

    @Autowired
    ResidentService residentService;

    @Autowired
    HealthDataService healthDataService;

    @Autowired
    AccessDataService accessDataService;

    @RequestMapping(value = "/getChecker")
    public AjaxResponse getChecker(@RequestHeader("${jwt.header}") String token){
        SysUser sysUser = jwtAuthService.getCheckerMessage(token);
        Map<String,Object> map = new HashMap<>();
        map.put("id",sysUser.getUserId().toString());
        map.put("name",sysUser.getNickname());
        return AjaxResponse.success(map);
    }

    @RequestMapping(value = "/scanCode")
    public String scanUserCode(@RequestHeader("${jwt.header}") String token,@RequestBody Map<String,String> map){
        if (jwtAuthService.judgeToken(token)){
            return "fail";
        }
        return healthDataService.getScanHealthData(map.get("residentId"));
    }

    @RequestMapping(value = "/enterTem")
    public String enterTemperature(@RequestHeader("${jwt.header}") String token,@RequestBody Map<String,String> map){
        if (jwtAuthService.judgeToken(token)){
            return "overdue";
        }
        accessDataService.insertTemperature(map);
        return "success";

    }

    @RequestMapping(value = "/todayEnter")
    public AjaxResponse searchTodayEnter(@RequestHeader("${jwt.header}") String token,@RequestBody Map<String,String> map){
        if (jwtAuthService.judgeToken(token)){
            return AjaxResponse.success("overdue");
        }
        return AjaxResponse.success(accessDataService.searchTodayEnter(map.get("checkerId")));
    }

}
