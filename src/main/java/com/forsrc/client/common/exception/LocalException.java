package com.forsrc.client.common.exception;

import com.forsrc.client.common.constant.Code;
import com.forsrc.common.exception.CommonException;

public class LocalException extends CommonException {

  public LocalException(Code code) {
    super(code.getCode(), code.getMsg());
  }

  public LocalException(int code, String message) {
    super(code, message);
  }
  
}