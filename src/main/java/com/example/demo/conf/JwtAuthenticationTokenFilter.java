package com.example.demo.conf;

import com.example.demo.Util.JwtUtil;
import com.example.demo.Util.ResponseGenerator;
import com.example.demo.Util.RedisCache;
import com.example.demo.dto.SelfUser;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ExceptionEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    com.example.demo.service.impl.myLoadService myLoadService;
    @Autowired
    RedisCache redisCache;
    private Logger log= LoggerFactory.getLogger(getClass());
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                final String authToken = authHeader.substring("Bearer ".length());
                String username = JwtUtil.getSubject(authToken);
                String uid = JwtUtil.getUid(authToken);
                SelfUser selfUser=redisCache.getCacheObject("user:"+uid);
                if (null == selfUser) {
                    SelfUser s=new SelfUser();
                    s.setToken(authToken);
                    redisCache.setCacheObject("user:"+uid,s);
                    getUserFromDatabase(request,username);
                } else {
                    String hasToken = selfUser.getToken();
                    if (hasToken.equals(authToken)) {
                        setSecurityContext(selfUser,request);
                    } else {
                        if (JwtUtil.CompareDate(authToken, hasToken)) {
                            selfUser.setToken(authToken);
                            redisCache.setCacheObject("user:" + uid, selfUser);
                            getUserFromDatabase(request,username);
                        } else {
                            throw new CustomException(ExceptionEnum.TWICE_LOGIN,"OncePerRequestFilter");
                        }
                    }
                }

            }
        }catch (CustomException e)
        {
             log.info("二次登录");
             dealWithError(response,e.getMessage());
        }catch (io.jsonwebtoken.MalformedJwtException e)
        {
            dealWithError(response,"token无效");
        }
        filterChain.doFilter(request,response);
    }

    private static void dealWithError(HttpServletResponse httpServletResponse, String message) throws IOException {
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON.toString());
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();

        ObjectMapper build = Jackson2ObjectMapperBuilder.json().build();
        build.writeValue(outputStream, ResponseGenerator.getErrorResponse(message));
    }

    private void getUserFromDatabase(HttpServletRequest request,String username)
    {
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = myLoadService.loadUserByUsername(username);
            if (userDetails != null) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
    }

    private void setSecurityContext(UserDetails userDetails,HttpServletRequest request)
    {
        if (userDetails != null) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }
}
