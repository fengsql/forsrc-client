package com.forsrc.client.common.response;

import com.forsrc.common.spring.base.BResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ResponseBody extends BResponse {

  private Boolean success;
  
  private Integer code;

  private String message;

  private Object data;

}