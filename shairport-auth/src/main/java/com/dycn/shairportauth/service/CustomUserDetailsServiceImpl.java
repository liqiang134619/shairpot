package com.dycn.shairportauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Liq
 * @date 2021/3/30
 */
@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {



    @Autowired
    private PasswordEncoder passwordEncoder;



    // 查询加载用户


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 返回一个认证的用户
        String role = "ROLE_ADMIN";
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        // 线上环境应该通过用户名查询数据库获取加密后的密码
        String password = passwordEncoder.encode("123456");
        return new org.springframework.security.core.userdetails.User(username,password, authorities);
    }


}
