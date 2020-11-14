package com.example.demo.service.impl;

import com.example.demo.Util.JwtUtil;
import com.example.demo.Util.RedisCache;
import com.example.demo.Util.ServletUtil;
import com.example.demo.dto.SelfUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service("ss")
public class PermissionService {

    @Autowired
    RedisCache redisCache;

    private static final String ADMIN="admin";
    private static final String USER="user";

    private Logger log= LoggerFactory.getLogger(getClass());
    public boolean hasPermission(String permission)
    {
        if(permission==null||permission.equals(""))
            return false;
        HttpServletRequest request= ServletUtil.getHttpServletRequest();
        String uid= JwtUtil.getUid(request.getHeader("Authorization").substring("Bearer ".length()));
        SelfUser selfUser=redisCache.getCacheObject("user:"+uid);
        if(selfUser==null||selfUser.getPermissions()==null)
            return false;
        for(String per:selfUser.getPermissions())
        {
            if(per.equals(permission)) return true;
        }
        return false;
    }
}
