package com.forsrc.client.common.init;

import com.forsrc.common.constant.Code;
import com.forsrc.common.exception.CommonException;
import com.forsrc.data.common.bean.ResultGenerator;
import com.forsrc.data.common.tool.ToolGenerator;
import com.forsrc.data.generator.item.Download;
import com.forsrc.data.generator.item.Generator;
import com.forsrc.data.generator.item.LoadDb;
import com.forsrc.data.generator.item.LoadFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service
@DependsOn({"configForsrc"})
@Slf4j
public class Init {

  @Resource
  private LoadFile loadFile;
  @Resource
  private LoadDb loadDb;
  @Resource
  private Generator generator;
  @Resource
  private Download download;

  @PostConstruct
  private void init() {
    ToolGenerator.checkConfig();
    String data = loadData();
    ResultGenerator resultGenerator = generator(data);
    download(resultGenerator);
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

  private String loadData() {
    String data = null;
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

}