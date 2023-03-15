package com.forsrc.client.response;

import com.forsrc.common.reponse.IResponseHandler;
import org.springframework.stereotype.Service;

@Service
public class ResponseHandler implements IResponseHandler<ResponseBody> {

  @Override
  public ResponseBody createResponse(Boolean success, Integer code, String message, Object data) {
    ResponseBody responseBody = new ResponseBody();
    responseBody.setSuccess(success);
    responseBody.setCode(code);
    responseBody.setMessage(message);
    responseBody.setData(data);
    return responseBody;
  }
}