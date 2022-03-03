package com.forsrc.client.forsrc.generator.item;

import com.forsrc.client.common.constant.ConfigForsrc;
import com.forsrc.client.forsrc.generator.base.BForsrc;
import com.forsrc.common.configure.okhttp.BeanOkHttp;
import com.forsrc.common.constant.Code;
import com.forsrc.common.constant.Enum;
import com.forsrc.common.exception.CommonException;
import com.forsrc.common.tool.Tool;
import com.forsrc.common.tool.ToolJson;
import com.forsrc.common.tool.ToolZip;
import com.forsrc.data.common.bean.ReqDownload;
import com.forsrc.data.common.bean.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.UUID;

@Service
@Slf4j
public class Download extends BForsrc {

  private static final String sep_url = "/";
  private static final String http_protocol = "http://";
  private static final String https_protocol = "https://";

  private static final String host_default = "www.forsrc.com";
  private static final int port_default = 80;
  private static final String path_default = "forsrc/download";

  private static String host = ConfigForsrc.forsrc.server.host;
  private static int port = ConfigForsrc.forsrc.server.port;
  private static String path = ConfigForsrc.forsrc.server.path.download;

  private static final String saveSrcPath = ConfigForsrc.forsrc.generator.output.saveSrcPath;
  private static final String saveWebPath = ConfigForsrc.forsrc.generator.output.saveWebPath;
  private static final String saveSqlPath = ConfigForsrc.forsrc.generator.output.saveSqlPath;

  private static String url = null;

  @Resource
  private BeanOkHttp beanOkHttp;

  // <<----------------------- public -----------------------

  // <<<----------------------- normal -----------------------

  public void work(ResultGenerator resultGenerator) {
    if (resultGenerator == null) {
      throw new CommonException(Code.OBJECT_NULL.getCode(), "resultGenerator is null.");
    }
    initConfig();
    download(resultGenerator);
  }

  // >>>----------------------- normal -----------------------

  // >>----------------------- public -----------------------

  // <<----------------------- private -----------------------

  // <<<----------------------- download -----------------------

  private void download(ResultGenerator resultGenerator) {
    if (resultGenerator.getCodeSrc() != null) {
      downloadSrc(resultGenerator);
    }
    if (resultGenerator.getCodeWeb() != null) {
      downloadWeb(resultGenerator);
    }
    if (resultGenerator.getCodeSql() != null) {
      downloadSql(resultGenerator);
    }
  }

  private void downloadSrc(ResultGenerator resultGenerator) {
    ReqDownload reqDownload = createReqDownload(resultGenerator);
    reqDownload.setCode(resultGenerator.getCodeSrc());
    byte[] bytes = downloadFile(reqDownload);
    if (bytes == null) {
      return;
    }
    unzip(Enum.GeneratorItemType.src_, bytes, saveSrcPath);
  }

  private void downloadWeb(ResultGenerator resultGenerator) {
    ReqDownload reqDownload = createReqDownload(resultGenerator);
    reqDownload.setCode(resultGenerator.getCodeWeb());
    byte[] bytes = downloadFile(reqDownload);
    if (bytes == null) {
      return;
    }
    unzip(Enum.GeneratorItemType.web_, bytes, saveWebPath);
  }

  private void downloadSql(ResultGenerator resultGenerator) {
    ReqDownload reqDownload = createReqDownload(resultGenerator);
    reqDownload.setCode(resultGenerator.getCodeSql());
    byte[] bytes = downloadFile(reqDownload);
    if (bytes == null) {
      return;
    }
    unzip(Enum.GeneratorItemType.sql_, bytes, saveSqlPath);
  }

  // >>>----------------------- download -----------------------

  // <<<----------------------- downloadFile -----------------------

  private byte[] downloadFile(ReqDownload reqDownload) {
    String data = ToolJson.toJson(reqDownload);
    return sendPost(url, data);
  }

  private void unzip(Enum.GeneratorItemType generatorItemType, byte[] bytes, String savePath) {
    String filePath = System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID();
    savePath = Tool.toLocalPath(savePath);
    ToolZip.unzip(bytes, savePath);
    log.info("down {} ok. size: {}. savePath: {}", generatorItemType.getName(), bytes.length, savePath);
  }

  // >>>----------------------- downloadFile -----------------------

  // <<<----------------------- sendPost -----------------------

  private byte[] sendPost(String url, String data) {
    //    log.info("download start. {}", data);
    return beanOkHttp.down(url, data);
  }

  private ReqDownload createReqDownload(ResultGenerator resultGenerator) {
    ReqDownload reqDownload = new ReqDownload();
    reqDownload.setKey(resultGenerator.getKey());
    reqDownload.setRoute(resultGenerator.getRoute());
    reqDownload.setSign(resultGenerator.getSign());
    return reqDownload;
  }

  // >>>----------------------- sendPost -----------------------

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

  // >>>----------------------- tool -----------------------

  // >>----------------------- private -----------------------

}