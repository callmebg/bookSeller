package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.dao.UserMapper;
import com.example.demo.dto.SelfUser;
import com.example.demo.dto.user;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Component
public class myLoadService implements UserDetailsService {

    private Logger log= LoggerFactory.getLogger(getClass());
    @Autowired
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<user> t=new QueryWrapper<>();
        t.eq("username",username);
        user u=userMapper.selectOne(t);
        SelfUser selfUser=new SelfUser();
        if(u==null) {
            log.info("用户不存在");
         throw new UsernameNotFoundException("用户不存在");
        }
         else {
            selfUser.setUsername(u.getUsername());
            selfUser.setPassword(u.getPassword());
            Set<String> permissions=new HashSet<>();
            if(u.getPermissions()!=null) {
                String[] per = u.getPermissions().split(",");
                for (String ss : per) {
                    permissions.add(ss);
                }
            }
            selfUser.setPermissions(permissions);
        }
         log.info(selfUser.toString());
        return selfUser;
    }
}
