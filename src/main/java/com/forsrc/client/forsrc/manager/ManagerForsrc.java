package com.forsrc.client.forsrc.manager;

import com.forsrc.client.forsrc.generator.base.BForsrc;
import com.forsrc.client.forsrc.generator.item.Download;
import com.forsrc.client.forsrc.generator.item.Generator;
import com.forsrc.client.forsrc.generator.item.LoadDb;
import com.forsrc.client.forsrc.generator.item.LoadFile;
import com.forsrc.client.forsrc.tool.ToolGenerator;
import com.forsrc.common.constant.Code;
import com.forsrc.common.exception.CommonException;
import com.forsrc.data.common.bean.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class ManagerForsrc extends BForsrc {

  @Resource
  private LoadFile loadFile;
  @Resource
  private LoadDb loadDb;
  @Resource
  private Generator generator;
  @Resource
  private Download download;

  // <<----------------------- public -----------------------

  // <<<----------------------- normal -----------------------

  public void work() {
    doWork();
  }

  // >>>----------------------- normal -----------------------

  // >>----------------------- public -----------------------

  // <<----------------------- private -----------------------

  // <<<----------------------- normal -----------------------

  private void doWork() {
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

  // >>>----------------------- normal -----------------------

  // <<<----------------------- getData -----------------------

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

  // >>>----------------------- getData -----------------------

  // <<<----------------------- tool -----------------------

  // >>>----------------------- tool -----------------------

  // >>----------------------- private -----------------------

}