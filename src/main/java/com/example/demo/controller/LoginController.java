package com.example.demo.controller;

import com.example.demo.Util.JwtUtil;
import com.example.demo.Util.ResponseGenerator;
import com.example.demo.Util.RedisCache;
import com.example.demo.dao.UserMapper;
import com.example.demo.dto.SelfUser;
import com.example.demo.dto.SuccessResponse;
import com.example.demo.dto.user;

import com.example.demo.exception.CustomException;
import com.example.demo.exception.ExceptionEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "Login")
@RestController
public class LoginController {
    @Autowired
    com.example.demo.service.userService userService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisCache redisCache;


    private Logger log= LoggerFactory.getLogger(getClass());

    @ApiOperation("register")
    @PostMapping("/register")
    public SuccessResponse register(@RequestParam("username") String username,@RequestParam("password") String password,@RequestParam("email") String email) throws CustomException {
        if(!userService.getAllEmail(email).isEmpty()) throw new CustomException(ExceptionEnum.VALID_EMAIL,"register");
        if(userService.getAllUsername(username).isEmpty()){
        user u=new user();
        u.setUsername(username);
        u.setEmail(email);
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        u.setPassword(encoder.encode(password));
        userService.addUser(u);
        return ResponseGenerator.getSuccessResponse();}
        else {
               throw new CustomException(ExceptionEnum.VALID_NAME,"register");
        }
    }

    @ApiOperation("login")
    @PostMapping("/userLogin")
    public SuccessResponse login(@RequestParam("username")String username,@RequestParam("password") String password)
    {
        Authentication authenticate=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        SelfUser selfUser=(SelfUser)authenticate.getPrincipal();
        Map<String,Object> m=new HashMap<>();
        String uid=userMapper.getUserIdByName(username);
        m.put("uid",uid);
        m.put("nowTime",System.currentTimeMillis());
        String token=JwtUtil.createToken(username,m);
        selfUser.setToken(token);
        redisCache.setCacheObject("user:"+uid,selfUser);
        return ResponseGenerator.getSuccessResponse(token);
    }

//    @PreAuthorize("@ss.hasPermission('admin')")
    @ApiOperation("test")
    @GetMapping("/test")
    public SuccessResponse test(HttpServletRequest request)
    {
        String uid=JwtUtil.getUid(request.getHeader("Authorization").substring("Bearer ".length()));
        return ResponseGenerator.getSuccessResponse(uid);
    }

    @ApiOperation("logout")
    @PostMapping("/userLogout")
    public SuccessResponse logout( HttpServletRequest request)
    {
        String uid=JwtUtil.getUid(request.getHeader("Authorization").substring("Bearer ".length()));
        redisCache.deleteCacheObject("user:"+uid);
        return ResponseGenerator.getSuccessResponse();
    }

    @ApiOperation("changePassword")
    @PostMapping("/changePassword")
    public SuccessResponse changePassword(@RequestParam("oldPassword")String oldPassword,@RequestParam("newPassword")String newPassword,HttpServletRequest request) throws CustomException {
        String uid=JwtUtil.getUid(request.getHeader("Authorization").substring("Bearer ".length()));
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        String HashNewPassword=encoder.encode(newPassword);
        String Password=userService.getPassword(uid);
        if(encoder.matches(oldPassword,Password))
        {
             userService.changePassword(HashNewPassword,uid);
             return ResponseGenerator.getSuccessResponse();
        }
        else{
            throw new CustomException(ExceptionEnum.WRONG_PASSWORD,"changePassword");
        }
    }

    @ApiOperation("changePermission")
    @PostMapping("/changePermission")
    public SuccessResponse changePermissions(@RequestParam("permission")String permissions,HttpServletRequest request)
    {
        String uid=JwtUtil.getUid(request.getHeader("Authorization").substring("Bearer ".length()));
        userService.changePermissions(permissions,uid);
        return ResponseGenerator.getSuccessResponse();
    }

}
