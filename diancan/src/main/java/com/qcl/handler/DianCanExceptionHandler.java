package com.qcl.handler;

import com.qcl.yichang.DianCanAuthorizeException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.net.URISyntaxException;

@ControllerAdvice
public class DianCanExceptionHandler {


    //拦截登录异常
    //http://localhost:8080/diancan/leimu/list
  /*  @ExceptionHandler(value = DianCanAuthorizeException.class)
    public String handlerAuthorizeException() {
        return "zujian/loginView";
    }
    */
  @ExceptionHandler(value = DianCanAuthorizeException.class)
  public ResponseEntity<Void> handlerAuthorizeException() throws URISyntaxException{
      return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT).location(new URI("/diancan/login")).build();
  }


}
