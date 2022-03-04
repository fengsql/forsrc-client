package com.forsrc.client.common.init;

import com.forsrc.client.common.monitor.common.Monitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service
@Slf4j
public class Init {

  @Resource
  private Monitor monitor;

  @PostConstruct
  public void init() {
    monitor.start();
  }

}