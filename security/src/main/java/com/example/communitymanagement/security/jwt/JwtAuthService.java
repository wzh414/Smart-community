package com.example.communitymanagement.security.jwt;


import com.example.communitymanagement.dao.UserRepository;
import com.example.communitymanagement.entity.SysUser;
import com.example.communitymanagement.exception.CustomException;
import com.example.communitymanagement.exception.CustomExceptionType;
import com.example.communitymanagement.security.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wangpengwei
 */
@Service
public class JwtAuthService {

    @Resource
    AuthenticationManager authenticationManager;

    @Resource
    MyUserDetailService userDetailsService;

    @Resource
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserRepository userRepository;

    /**
     * 登录认证换取JWT令牌
     * @return JWT
     */
    public String login(String username, String password) throws CustomException {
        try {
            SysUser user = new SysUser();
            user.setUsername(username);
            UsernamePasswordAuthenticationToken upToken =
                    new UsernamePasswordAuthenticationToken(user, password);
            Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (AuthenticationException e){
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR
                    ,"用户名或密码不正确");
        }

        UserDetails userDetails = userDetailsService.loadUser(username);
        return jwtTokenUtil.generateToken(userDetails);
    }


    public String refreshToken(String oldToken){
        if(!jwtTokenUtil.isTokenExpired(oldToken)){
            return jwtTokenUtil.refreshToken(oldToken);
        }
        return null;
    }

    public Boolean judgeToken(String token){
        return jwtTokenUtil.isTokenExpired(token);
    }

    public SysUser getCheckerMessage(String token){
        String username = jwtTokenUtil.getUsernameFromToken(token);
        SysUser user = userRepository.checkinFront(username);
        return user;
    }


}
