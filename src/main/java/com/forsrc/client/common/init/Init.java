package com.forsrc.client.common.init;

import com.forsrc.client.forsrc.manager.ManagerForsrc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service
@DependsOn({"toolBean"})
@Slf4j
public class Init {

  @Resource
  private ManagerForsrc managerForsrc;

  @PostConstruct
  public void init() {
    managerForsrc.work();
  }

}