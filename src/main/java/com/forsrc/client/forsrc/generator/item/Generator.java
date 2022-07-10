package com.forsrc.client.forsrc.generator.item;

import com.forsrc.client.common.constant.ConfigForsrc;
import com.forsrc.client.forsrc.generator.base.BForsrc;
import com.forsrc.common.cipher.tool.ToolBase64;
import com.forsrc.common.cipher.tool.ToolCipher;
import com.forsrc.common.configure.okhttp.BeanOkHttp;
import com.forsrc.common.tool.Tool;
import com.forsrc.common.tool.ToolJson;
import com.forsrc.data.common.bean.*;
import com.forsrc.data.common.tool.ToolSign;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class Generator extends BForsrc {

  private static final String sep_url = "/";
  private static final String http_protocol = "http://";
  private static final String https_protocol = "https://";

  private static final String host_default = "gen.forsrc.com";
  private static final int port_default = 80;
  private static final String path_default = "forsrc/generator";

  private static String host = ConfigForsrc.forsrc.server.host;
  private static int port = ConfigForsrc.forsrc.server.port;
  private static String path = ConfigForsrc.forsrc.server.path.generator;

  private static final String datasourceType = ConfigForsrc.forsrc.datasource.type;
  private static final String database = ConfigForsrc.forsrc.datasource.database;
  private static final boolean fromFile = ConfigForsrc.forsrc.generator.fromFile;

  private static final String appKey = ConfigForsrc.forsrc.generator.application.appKey;
  private static final String version = ConfigForsrc.forsrc.generator.application.version;

  private static final String databaseType = ConfigForsrc.forsrc.generator.database.type;
  private static final String sqlVersion = ConfigForsrc.forsrc.generator.database.sqlVersion;

  private static final String projectName = ConfigForsrc.forsrc.generator.project.name;
  private static final String projectTitle = ConfigForsrc.forsrc.generator.project.title;
  private static final String srcPack = ConfigForsrc.forsrc.generator.project.srcPack;

  private static final boolean generatorSrc = ConfigForsrc.forsrc.generator.output.generatorSrc;
  private static final boolean generatorWeb = ConfigForsrc.forsrc.generator.output.generatorWeb;
  private static final boolean generatorSql = ConfigForsrc.forsrc.generator.output.generatorSql;
  //
  private static final String username = ConfigForsrc.forsrc.authorization.username;
  private static final String secret = ConfigForsrc.forsrc.authorization.secret;
  //
  private static final boolean ridTablePrefix = ConfigForsrc.forsrc.generator.option.database.ridTablePrefix;
  private static final boolean sqlAddComment = ConfigForsrc.forsrc.generator.option.database.sqlAddComment;
  private static final boolean sqlAddCommentInfo = ConfigForsrc.forsrc.generator.option.database.sqlAddCommentInfo;
  private static final int selectFieldNum = ConfigForsrc.forsrc.generator.option.database.selectFieldNum;

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
    log.info("generator start. appKey: {}. version: {}. databaseType: {}. sqlVersion: {}. srcPack: {}. generatorSrc: {}. generatorWeb: {}. generatorSql: {}.", //
      appKey, version, databaseType, sqlVersion, srcPack, generatorSrc, generatorWeb, generatorSql);
    long start = System.currentTimeMillis();

    String json = sendGenerator(data);
    ResultGenerator resultGenerator = getResultGenerator(json);

    long end = System.currentTimeMillis();
    int ms = (int) (end - start);
    String total = getCost(ms);

    //    log.info("generator result: {}", getGeneratorResult(resultGenerator));
    if (resultGenerator == null || !resultGenerator.isSuccess()) {
      String msg = resultGenerator == null ? null : resultGenerator.getMsg();
      log.warn("generator fail! msg: {}", msg);
      return null;
    } else {
      String cost = getCost(resultGenerator.getCost());
      log.info("---------------------------------------------------------------------------");
      log.info("                                                                           ");
      log.info("                     generator success. cost {}.", cost);
      log.info("                                                                           ");
      log.info("---------------------------------------------------------------------------");
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
    byte[] password = getPassword();
    data = encode(data, password);
    ReqGenerator reqGenerator = getReqGenerator(data);
    return ToolJson.toJson(reqGenerator);
  }

  private String encode(String data, byte[] password) {
    long start = System.currentTimeMillis();
    byte[] source = Tool.toBytes(data);
    byte[] target = ToolCipher.encryptAes(source, password);
    String result = ToolBase64.encode(target);
    long end = System.currentTimeMillis();
    int ms = (int) (end - start);
    String total = getCost(ms);
//    log.info("encode ok. cost: {}. sourceLength: {}. targetLength: {}", total, data.length(), result.length());
    return result;
  }

  private byte[] getPassword() {
    return ToolCipher.md5(secret.getBytes());
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
    optionDatabase.setSqlAddComment(sqlAddComment);
    optionDatabase.setSqlAddCommentInfo(sqlAddCommentInfo);
    optionDatabase.setSelectFieldNum(selectFieldNum);
    generatorOption.setOptionDatabase(optionDatabase);
  }

  private ReqGenerator createReqGenerator(String data) {
    long timestamp = System.currentTimeMillis();
    ReqGenerator reqGenerator = new ReqGenerator();
    reqGenerator.setDatasourceType(datasourceType);
    reqGenerator.setDatabase(database);
    reqGenerator.setFromFile(fromFile);

    reqGenerator.setAppKey(appKey);
    reqGenerator.setVersion(version);

    reqGenerator.setDatabaseType(databaseType);
    reqGenerator.setSqlVersion(sqlVersion);

    reqGenerator.setProjectName(projectName);
    reqGenerator.setProjectTitle(projectTitle);
    reqGenerator.setSrcPack(srcPack);

    reqGenerator.setGeneratorSrc(generatorSrc);
    reqGenerator.setGeneratorWeb(generatorWeb);
    reqGenerator.setGeneratorSql(generatorSql);
    reqGenerator.setData(data);
    //
    reqGenerator.setTimestamp(timestamp);
    reqGenerator.setUsername(username);
    //
    reqGenerator.setKey(ToolSign.getKey(username, secret, timestamp));
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
    setResult(repGenerator);
    if (!isSuccess(repGenerator)) {
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

  private boolean isSuccess(RepGenerator repGenerator) {
    return repGenerator != null && repGenerator.getSuccess() != null && repGenerator.getSuccess();
  }

  private void setResult(RepGenerator repGenerator) {
    if (repGenerator.getSuccess() != null) {
      return;
    }
    Integer code = repGenerator.getCode();
    if (code != null && code == HttpStatus.SC_OK) {
      repGenerator.setSuccess(true);
    } else {
      repGenerator.setSuccess(false);
    }
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

  private String getCost(long ms) {
    String cost;
    if (ms < 1000) {
      cost = ms + " ms";
    } else {
      long second = ms / 1000;
      long tail = ms % 1000;
      cost = second + " s " + tail + " ms";
    }
    return cost;
  }

  // >>>----------------------- tool -----------------------

  // >>----------------------- private -----------------------

}