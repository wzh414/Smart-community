package com.example.communitymanagement.security;



import com.example.communitymanagement.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;



@Component
public class UserAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    //从数据库中读取用户 相关信息

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 用户输入的用户名和密码
        String inputPassword = authentication.getCredentials(). toString();

        // userDetails为数据库中查询到的用户信息
        // 只能获取数据库字段加 权限 合成的userDetail
        UserDetails userDetails;
        if(authentication.getPrincipal() instanceof SysUser) {
            userDetails = myUserDetailService.loadUser(((SysUser) authentication.getPrincipal()).getUsername());
        }
        else{
            String inputName = authentication.getName();
            userDetails = myUserDetailService.loadUserByUsername(inputName);
        }

        // 如果是自定义AuthenticationProvider，需要手动密码校验
        if(!passwordEncoder.matches(inputPassword,userDetails.getPassword())) {
            throw new BadCredentialsException("用户名或密码错误");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, inputPassword, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 这里不要忘记，和UsernamePasswordAuthenticationToken比较,传入的参数类型相同返回true就以此Provider处理
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
