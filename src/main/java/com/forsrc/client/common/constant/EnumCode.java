package com.forsrc.client.common.constant;

public enum EnumCode {
  //user 2000
  user_exception(2000, "用户异常"),
  //roleType 2101
  roleType_permissions(2101, "当前用户角色权限不足"),
  ;

  Integer code;
  String msg;

  EnumCode(Integer code, String msg) {
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