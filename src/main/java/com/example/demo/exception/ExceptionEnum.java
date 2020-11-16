package com.example.demo.exception;

public enum ExceptionEnum {

    UNKNOWN_EXCEPTION(100,"未知错误"),

    WRONG_PASSWORD(101,"错误密码"),

    VALID_NAME(102,"重复命名"),

    TWICE_LOGIN(103,"账号已在别处登录"),

    VALID_EMAIL(104,"重复邮箱"),

    DELETE_FAILED(105,"删除失败"),

    VALID_BOOKID(106,"书籍不存在");




    private int code;

    private String msg;

    public int getCode()
    {
        return code;
    }

    public String getMsg()
    {
        return msg;
    }

    ExceptionEnum(int code,String msg)
    {
        this.code=code;
        this.msg=msg;
    }

    public static ExceptionEnum getByCode(int code)
    {
        for(ExceptionEnum r:ExceptionEnum.values())
        {
            if(code==r.getCode())
            {
                return r;
            }
        }
        return null;
    }
}
