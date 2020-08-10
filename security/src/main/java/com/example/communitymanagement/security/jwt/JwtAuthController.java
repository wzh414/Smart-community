package com.example.communitymanagement.security.jwt;

import com.example.communitymanagement.entity.SysResident;
import com.example.communitymanagement.entity.SysUser;
import com.example.communitymanagement.exception.AjaxResponse;
import com.example.communitymanagement.exception.CustomException;
import com.example.communitymanagement.exception.CustomExceptionType;
import com.example.communitymanagement.service.ResidentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangpengwei
 */
@RestController
public class JwtAuthController {

    @Resource
    JwtAuthService jwtAuthService;

    @Autowired
    ResidentService residentService;

    @RequestMapping(value = "/authentication")
    public AjaxResponse login(@RequestBody Map<String, String> map){
        String username  = map.get("username");
        String password = map.get("password");

        if(StringUtils.isEmpty(username)
                || StringUtils.isEmpty(password)){
            return AjaxResponse.error(
                    new CustomException(CustomExceptionType.USER_INPUT_ERROR,
                            "用户名或密码不能为空"));
        }
        try {
            return AjaxResponse.success(jwtAuthService.login(username, password));
        }catch (CustomException e){
            return AjaxResponse.error(e);
        }
    }

    @RequestMapping(value = "/refreshtoken")
    public  AjaxResponse refresh(@RequestHeader("${jwt.header}") String token){
        return AjaxResponse.success(jwtAuthService.refreshToken(token));
    }
}
