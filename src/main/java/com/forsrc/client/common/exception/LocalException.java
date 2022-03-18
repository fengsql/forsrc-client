package com.forsrc.client.common.exception;

import com.forsrc.client.common.constant.EnumCode;
import com.forsrc.common.exception.CommonException;

public class LocalException extends CommonException {

  public LocalException(EnumCode enumCode) {
    super(enumCode.getCode(), enumCode.getMsg());
  }

  public LocalException(EnumCode enumCode, String message) {
    super(enumCode.getCode(), message);
  }

  public LocalException(int code, String message) {
    super(code, message);
  }

}