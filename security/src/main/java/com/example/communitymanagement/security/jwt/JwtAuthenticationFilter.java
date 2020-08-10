package com.example.communitymanagement.security.jwt;

import com.example.communitymanagement.security.CustomAuthenticationFailureHandler;
import com.example.communitymanagement.security.MyUserDetailService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wangpengwei
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Resource
    CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Resource
    JwtTokenUtil jwtTokenUtil;

    @Resource
    MyUserDetailService myUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String jwtToken = request.getHeader(jwtTokenUtil.getHeader());
        try {
            if (!StringUtils.isEmpty(jwtToken)) {
                String username = jwtTokenUtil.getUsernameFromToken(jwtToken);

                if (username != null &&
                        SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = myUserDetailsService.loadUser(username);
                    if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                        //给使用该JWT令牌的用户进行授权
                        UsernamePasswordAuthenticationToken authenticationToken
                                = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
                else if(username == null){
                    throw new BadCredentialsException("token解析错误，请重新登录");
                }
            }
        }catch (AuthenticationException e){
            customAuthenticationFailureHandler.onAuthenticationFailure(
                    request,response,e
            );
            return;
        }

        filterChain.doFilter(request,response);
    }
}