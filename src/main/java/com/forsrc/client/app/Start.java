package com.forsrc.client.app;

import com.forsrc.common.constant.Code;
import com.forsrc.common.exception.CommonException;
import com.forsrc.common.tool.ToolBean;
import com.forsrc.data.common.bean.ResultGenerator;
import com.forsrc.data.common.constant.ConfigForsrc;
import com.forsrc.data.common.tool.ToolGenerator;
import com.forsrc.data.generator.item.Download;
import com.forsrc.data.generator.item.Generator;
import com.forsrc.data.load.item.LoadDb;
import com.forsrc.data.load.item.LoadFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service
@Slf4j
public class Start {

  @Resource
  private ConfigForsrc configForsrc;
  @Resource
  private ToolBean toolBean;
  @Resource
  private Generator generator;
  @Resource
  private Download download;
  @Resource
  private LoadFile loadFile;
  @Resource
  private LoadDb loadDb;

  @PostConstruct
  public void init() {
    log.info("start.");
    startGenerator();
  }

  private void startGenerator() {
    ToolGenerator.checkConfig();
    log.info("checkConfig ok.");
    String data = loadData();
    log.info("loadData ok.");
    
    ResultGenerator resultGenerator = generator(data);
    if (resultGenerator == null) {
      log.warn("generator fail!");
      return;
    } else {
      log.info("generator ok. app: {}. web: {}. sql: {}.",  //
        resultGenerator.getSuccessApp(), resultGenerator.getSuccessWeb(), resultGenerator.getSuccessSql());
    }
    download(resultGenerator);
    log.info("download ok.");
  }

  private String loadData() {
    String data;
    if (ToolGenerator.isGeneratorFromFile()) {
      data = loadFile();
    } else {
      data = loadDb();
    }
    if (data == null) {
      throw new CommonException(Code.OBJECT_NULL.getCode(), "载入数据错误.");
    }
    return data;
  }

  private String loadFile() {
    return loadFile.load();
  }

  private String loadDb() {
    return loadDb.load();
  }

  private ResultGenerator generator(String data) {
    if (data == null) {
      return null;
    }
    return generator.work(data);
  }

  private void download(ResultGenerator resultGenerator) {
    if (resultGenerator == null) {
      return;
    }
    download.work(resultGenerator);
  }

}