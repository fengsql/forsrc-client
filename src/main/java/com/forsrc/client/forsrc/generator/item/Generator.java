package com.forsrc.client.forsrc.generator.item;

import com.forsrc.client.common.constant.ConfigForsrc;
import com.forsrc.client.forsrc.generator.base.BForsrc;
import com.forsrc.common.configure.okhttp.BeanOkHttp;
import com.forsrc.common.tool.Tool;
import com.forsrc.common.tool.ToolJson;
import com.forsrc.data.common.bean.*;
import com.forsrc.data.common.tool.ToolSign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class Generator extends BForsrc {

  private static final String sep_url = "/";
  private static final String http_protocol = "http://";
  private static final String https_protocol = "https://";

  private static final String host_default = "www.forsrc.com";
  private static final int port_default = 80;
  private static final String path_default = "forsrc/generator";

  private static String host = ConfigForsrc.forsrc.server.host;
  private static int port = ConfigForsrc.forsrc.server.port;
  private static String path = ConfigForsrc.forsrc.server.path.generator;

  private static final String datasourceType = ConfigForsrc.forsrc.datasource.type;
  private static final String database = ConfigForsrc.forsrc.datasource.database;
  private static final boolean fromFile = ConfigForsrc.forsrc.generator.fromFile;

  private static final String language = ConfigForsrc.forsrc.generator.application.language;
  private static final String type = ConfigForsrc.forsrc.generator.application.type;
  private static final String framework = ConfigForsrc.forsrc.generator.application.framework;
  private static final String databaseType = ConfigForsrc.forsrc.generator.application.databaseType;
  private static final String web = ConfigForsrc.forsrc.generator.application.web;
  private static final String branch = ConfigForsrc.forsrc.generator.application.branch;
  private static final String version = ConfigForsrc.forsrc.generator.application.version;

  private static final String projectName = ConfigForsrc.forsrc.generator.project.name;
  private static final String projectTitle = ConfigForsrc.forsrc.generator.project.title;
  private static final String srcPack = ConfigForsrc.forsrc.generator.project.srcPack;

  private static final boolean generatorSrc = ConfigForsrc.forsrc.generator.output.generatorSrc;
  private static final boolean generatorWeb = ConfigForsrc.forsrc.generator.output.generatorWeb;
  private static final boolean generatorSql = ConfigForsrc.forsrc.generator.output.generatorSql;
  //
  private static final String appid = ConfigForsrc.forsrc.authorization.appid;
  private static final String secret = ConfigForsrc.forsrc.authorization.secret;
  private static final String username = ConfigForsrc.forsrc.authorization.username;
  private static final String password = ConfigForsrc.forsrc.authorization.password;
  //
  private static final boolean ridTablePrefix = ConfigForsrc.forsrc.generator.option.database.ridTablePrefix;
  private static int selectFieldTotal = ConfigForsrc.forsrc.generator.option.database.selectFieldTotal;

  private static String url = null;

  @Resource
  private BeanOkHttp beanOkHttp;

  // <<----------------------- public -----------------------

  // <<<----------------------- normal -----------------------

  public ResultGenerator work(String data) {
    initConfig();
    return generator(data);
  }

  // >>>----------------------- normal -----------------------

  // >>----------------------- public -----------------------

  // <<----------------------- private -----------------------

  // <<<----------------------- generator -----------------------

  private ResultGenerator generator(String data) {
    log.info("generator start. type: {}. framework: {}. srcPack: {}. generatorSrc: {}. generatorWeb: {}. generatorSql: {}.", //
      type, framework, srcPack, generatorSrc, generatorWeb, generatorSql);
    long start = System.currentTimeMillis();

    String json = sendGenerator(data);
    ResultGenerator resultGenerator = getResultGenerator(json);

    long end = System.currentTimeMillis();
    int ms = (int) (end - start);
    String cost = getCost(ms);

    //    log.info("generator result: {}", getGeneratorResult(resultGenerator));
    if (resultGenerator == null || !resultGenerator.isSuccess()) {
      String msg = resultGenerator == null ? null : resultGenerator.getMsg();
      log.warn("generator fail! msg: {}", msg);
      return null;
    } else {
      log.info("<<----------------------------------------------------------");
      log.info("generator success. cost {}", cost);
      log.info(">>----------------------------------------------------------");
      return resultGenerator;
    }
  }

  private String getGeneratorResult(ResultGenerator resultGenerator) {
    if (resultGenerator == null) {
      return null;
    }
    return ToolJson.toJsonPretty(resultGenerator);
  }

  // >>>----------------------- generator -----------------------

  // <<<----------------------- sendGenerator -----------------------

  private String sendGenerator(String data) {
    String param = getSendParam(data);
    return beanOkHttp.postJson(url, param);
  }

  private String getSendParam(String data) {
    ReqGenerator reqGenerator = getReqGenerator(data);
    return ToolJson.toJson(reqGenerator);
  }

  private ReqGenerator getReqGenerator(String data) {
    ReqGenerator reqGenerator = createReqGenerator(data);
    setGeneratorOption(reqGenerator);
    return reqGenerator;
  }

  private void setGeneratorOption(ReqGenerator reqGenerator) {
    GeneratorOption generatorOption = new GeneratorOption();
    setOptionDatabase(generatorOption);
    reqGenerator.setGeneratorOption(generatorOption);
  }

  private void setOptionDatabase(GeneratorOption generatorOption) {
    OptionDatabase optionDatabase = new OptionDatabase();
    optionDatabase.setRidTablePrefix(ridTablePrefix);
    optionDatabase.setSelectFieldTotal(selectFieldTotal);
    generatorOption.setOptionDatabase(optionDatabase);
  }

  private ReqGenerator createReqGenerator(String data) {
    long timestamp = System.currentTimeMillis();
    ReqGenerator reqGenerator = new ReqGenerator();
    reqGenerator.setDatasourceType(datasourceType);
    reqGenerator.setDatabase(database);
    reqGenerator.setFromFile(fromFile);

    reqGenerator.setLanguage(language);
    reqGenerator.setApplication(type);
    reqGenerator.setFramework(framework);
    reqGenerator.setDatabaseType(databaseType);
    reqGenerator.setWeb(web);
    reqGenerator.setBranch(branch);
    reqGenerator.setVersion(version);

    reqGenerator.setProjectName(projectName);
    reqGenerator.setProjectTitle(projectTitle);
    reqGenerator.setSrcPack(srcPack);

    reqGenerator.setGeneratorSrc(generatorSrc);
    reqGenerator.setGeneratorWeb(generatorWeb);
    reqGenerator.setGeneratorSql(generatorSql);
    reqGenerator.setData(data);
    //
    reqGenerator.setTimestamp(timestamp);
    reqGenerator.setAppid(appid);
    reqGenerator.setUsername(username);
    //
    reqGenerator.setKey(ToolSign.getKey(timestamp, appid, secret, username, password));
    reqGenerator.setSign(ToolSign.getSign(reqGenerator));
    return reqGenerator;
  }

  // >>>----------------------- sendGenerator -----------------------

  // <<<----------------------- getResultGenerator -----------------------

  private ResultGenerator getResultGenerator(String json) {
    if (Tool.isNull(json)) {
      return null;
    }
    RepGenerator repGenerator = ToolJson.toBean(json, RepGenerator.class);
    if (!Tool.isSuccess(repGenerator)) {
      return getResultGenerator(repGenerator);
    }
    return repGenerator.getData();
  }

  private ResultGenerator getResultGenerator(RepGenerator repGenerator) {
    ResultGenerator resultGenerator = new ResultGenerator();
    resultGenerator.setSuccess(resultGenerator.isSuccess());
    resultGenerator.setMsg(repGenerator.getMessage());
    return resultGenerator;
  }

  // >>>----------------------- getResultGenerator -----------------------

  // <<<----------------------- initConfig -----------------------

  private void initConfig() {
    if (url != null) {
      return;
    }
    // host
    host = Tool.toString(host);
    if (Tool.isNull(host)) {
      host = host_default;
    }
    if (!host.startsWith(http_protocol) && !host.startsWith(https_protocol)) {
      host = http_protocol + host;
    }
    if (port <= 0) {
      port = port_default;
    }
    url = host;
    // port
    if (port != port_default) {
      url += ":" + port;
    }
    if (!url.endsWith(sep_url)) {
      url += sep_url;
    }
    // path
    path = Tool.toString(path);
    if (Tool.isNull(path)) {
      path = path_default;
    }
    if (path.startsWith(sep_url)) {
      path = path.substring(sep_url.length());
    }
    url += path;
  }

  // >>>----------------------- initConfig -----------------------

  // <<<----------------------- tool -----------------------

  private String getCost(int ms) {
    String cost;
    if (ms < 1000) {
      cost = ms + " ms.";
    } else {
      int second = ms / 1000;
      int tail = ms % 1000;
      cost = second + " s " + tail + " ms.";
    }
    return cost;
  }

  // >>>----------------------- tool -----------------------

  // >>----------------------- private -----------------------

}