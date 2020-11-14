package com.example.demo.Util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletUtil {

    public static ServletRequestAttributes getHttpRequestAttributes()
    {
        RequestAttributes attributes= RequestContextHolder.currentRequestAttributes();
        return (ServletRequestAttributes)attributes;
    }

    public static HttpServletRequest getHttpServletRequest()
    {
        return getHttpRequestAttributes().getRequest();
    }

    public static HttpServletResponse getHttpServletResponse()
    {
        return getHttpRequestAttributes().getResponse();
    }
}
