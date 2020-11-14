package com.example.demo.exception;

import com.example.demo.Util.ResponseGenerator;
import com.example.demo.dto.SuccessResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler({CustomException.class})
    public SuccessResponse handleCustomException(HttpServletRequest request,HttpServletResponse response,CustomException e)
    {
        return ResponseGenerator.getErrorResponse(e.getMessage());
    }


    @ExceptionHandler({Exception.class})
    public SuccessResponse handleException(HttpServletRequest request, HttpServletResponse response,Exception e)
    {
        return ResponseGenerator.getErrorResponse(e.getMessage());
    }

}
