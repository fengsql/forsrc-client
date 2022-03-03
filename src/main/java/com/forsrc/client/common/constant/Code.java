package com.forsrc.client.common.constant;

public enum Code {
    ;

    Integer code;
    String msg;


    Code(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}