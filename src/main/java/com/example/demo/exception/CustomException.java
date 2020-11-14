package com.example.demo.exception;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CustomException extends Exception {

    private final int code;

    private final String method;

    public CustomException(ExceptionEnum exceptionEnum,String method)
    {
        super(exceptionEnum.getMsg());
        this.code=exceptionEnum.getCode();
        this.method=method;
    }

    public CustomException(String msg,int code,String method)
    {
        super(msg);
        this.code=code;
        this.method=method;
    }

}
