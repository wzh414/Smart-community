package com.example.communitymanagement.security;

import com.example.communitymanagement.dao.UserRepository;
import com.example.communitymanagement.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class LockedFilter extends OncePerRequestFilter {
    @Resource
    CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try{
                validate();
            }
        catch (AuthenticationException e) {
                customAuthenticationFailureHandler.onAuthenticationFailure(
                        httpServletRequest,httpServletResponse,e
                );
                return;
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    private void validate(){
        if(SecurityContextHolder.getContext().getAuthentication()!=null){
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            SysUser user = userRepository.findByUsername(username);
            if(user.getUserStatus() == 1){
                throw new BadCredentialsException("您的账户已被禁用，请与管理员联系解决");
            }
        }
    }
}
