package com.example.communitymanagement.security;


import com.example.communitymanagement.dao.UserRepository;
import com.example.communitymanagement.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;




/*
 *
 *
 * UserDetailsService 属于原生接口 自己实现功能
 *
 *
 * 自动注入 UserRepository  进行数据库查找操作（需使用构造方法注入或者setter方法注入）
 *
 *
 */


@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        SysUser user = userRepository.checkinBack(s);
        return getUserDetails(user);
    }

    public UserDetails loadUser(String s) throws UsernameNotFoundException {

        SysUser user = userRepository.checkinFront(s);
        return getUserDetails(user);
    }

    private UserDetails getUserDetails(SysUser user) {
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        if(user.getUserStatus()==1){
            throw new BadCredentialsException("账户已被禁用，请联系系统管理员解决");
        }
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        String r = user.getUserRole();
        authorities.add(new SimpleGrantedAuthority(r));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPasswd(), authorities);
    }
}
