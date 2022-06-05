package com.forsrc.client.common.monitor;

import com.forsrc.client.forsrc.manager.ManagerForsrc;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class Monitor {
  @Resource
  @Getter
  private ManagerForsrc managerForsrc;

  public Monitor() {

  }

  public void start() {
    managerForsrc.work();
  }

}