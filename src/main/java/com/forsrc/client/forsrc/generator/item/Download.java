package com.forsrc.client.forsrc.generator.item;

import com.forsrc.client.common.constant.ConfigForsrc;
import com.forsrc.client.forsrc.generator.base.BForsrc;
import com.forsrc.common.configure.okhttp.BeanOkHttp;
import com.forsrc.common.constant.Code;
import com.forsrc.data.common.constant.Enum;
import com.forsrc.common.exception.CommonException;
import com.forsrc.common.tool.Tool;
import com.forsrc.common.tool.ToolFile;
import com.forsrc.common.tool.ToolJson;
import com.forsrc.common.tool.ToolZip;
import com.forsrc.data.common.bean.ReqDownload;
import com.forsrc.data.common.bean.ResultGenerator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileFilter;
import java.util.Set;
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

  private static final String path_temp = System.getProperty("java.io.tmpdir");

  private static final String saveAppPath = ConfigForsrc.forsrc.generator.output.saveAppPath;
  private static final String saveWebPath = ConfigForsrc.forsrc.generator.output.saveWebPath;
  private static final String saveSqlPath = ConfigForsrc.forsrc.generator.output.saveSqlPath;

  private static String host = ConfigForsrc.forsrc.server.host;
  private static int port = ConfigForsrc.forsrc.server.port;
  private static String path = ConfigForsrc.forsrc.server.path.download;

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
    if (resultGenerator.getCodeApp() != null) {
      downloadApp(resultGenerator);
    }
    if (resultGenerator.getCodeWeb() != null) {
      downloadWeb(resultGenerator);
    }
    if (resultGenerator.getCodeSql() != null) {
      downloadSql(resultGenerator);
    }
  }

  private void downloadApp(ResultGenerator resultGenerator) {
    ReqDownload reqDownload = createReqDownload(resultGenerator);
    reqDownload.setCode(resultGenerator.getCodeApp());
    byte[] bytes = downloadFile(reqDownload);
    if (bytes == null) {
      log.warn("download app fail!");
      return;
    }
    unzip(Enum.GeneratorItemType.app_, bytes, saveAppPath);
  }

  private void downloadWeb(ResultGenerator resultGenerator) {
    ReqDownload reqDownload = createReqDownload(resultGenerator);
    reqDownload.setCode(resultGenerator.getCodeWeb());
    byte[] bytes = downloadFile(reqDownload);
    if (bytes == null) {
      log.warn("download web fail!");
      return;
    }
    unzip(Enum.GeneratorItemType.web_, bytes, saveWebPath);
  }

  private void downloadSql(ResultGenerator resultGenerator) {
    ReqDownload reqDownload = createReqDownload(resultGenerator);
    reqDownload.setCode(resultGenerator.getCodeSql());
    byte[] bytes = downloadFile(reqDownload);
    if (bytes == null) {
      log.warn("download sql fail!");
      return;
    }
    unzip(Enum.GeneratorItemType.sql_, bytes, saveSqlPath);
  }

  // >>>----------------------- download -----------------------

  // <<<----------------------- unzip -----------------------

  private void unzip(Enum.GeneratorItemType generatorItemType, byte[] bytes, String savePath) {
    if (!hasProtectedPath()) {
      unzipDest(generatorItemType, bytes, savePath);
    } else {
      unzipCopy(generatorItemType, bytes, savePath);
    }
    String sizeText = ToolFile.getFileSizeText(bytes.length);
    log.info("down {} ok. size: {}. savePath: {}", generatorItemType.getName(), sizeText, savePath);
  }

  private void unzipDest(Enum.GeneratorItemType generatorItemType, byte[] bytes, String savePath) {
    ToolZip.unzip(bytes, savePath);
  }

  private void unzipCopy(Enum.GeneratorItemType generatorItemType, byte[] bytes, String savePath) {
    String tempPath = getTempPath();
    try {
      String path = tempPath + File.separator + generatorItemType.getName();
      //      log.info("unzip start. name: {}", generatorItemType.getName());
      ToolZip.unzip(bytes, path);
      //      log.info("copyFile start. name: {}", generatorItemType.getName());
      savePath = Tool.toLocalPath(savePath);
      copyFile(path, savePath);
    } finally {
      //      log.info("copyFile ok. name: {}", generatorItemType.getName());
      deletePath(tempPath);
    }
  }

  @SneakyThrows
  private void copyFile(String srcPath, String dstPath) {
    srcPath = initPath(srcPath);
    dstPath = initPath(dstPath);
    FileFilter fileFilter = getFileFilter(srcPath, dstPath);
    FileUtils.copyDirectory(new File(srcPath), new File(dstPath), fileFilter);  //fileFilter.accept ??? true ?????????false ??????
  }

  private FileFilter getFileFilter(String srcPath, String dstPath) {
    FileFilter fileFilter = new FileFilter() {
      @SneakyThrows
      @Override
      public boolean accept(File pathname) {  //fileFilter.accept ??? true ?????????false ??????
        String srcFile = pathname.getCanonicalPath();
        String dstFile = toTargetFile(srcPath, dstPath, srcFile);
        if (!ToolFile.existFile(dstFile)) {
          return true;
        }
        return acceptMatch(dstFile);
      }
    };
    return fileFilter;
  }

  private boolean acceptMatch(String dstFile) {
    Set<String> skipFiles = ConfigForsrc.getProtectedFilePath();
    for (String skipFile : skipFiles) {
      if (FilenameUtils.wildcardMatch(dstFile, skipFile)) {
        return false;
      }
    }
    return true;
  }

  private String toTargetFile(String srcPath, String dstPath, String srcFile) {
    String tail = srcFile.substring(srcPath.length());
    return ToolFile.joinFileName(dstPath, tail);
  }

  private String initPath(String fileName) {
    if (Tool.isNull(fileName)) {
      return fileName;
    }
    fileName = Tool.replace(fileName, "/", File.separator);
    fileName = Tool.replace(fileName, "\\", File.separator);
    if (fileName.endsWith(File.separator)) {
      fileName = fileName.substring(0, fileName.length() - 1);
    }
    return fileName;
  }

  // >>>----------------------- unzip -----------------------

  // <<<----------------------- downloadFile -----------------------

  private byte[] downloadFile(ReqDownload reqDownload) {
    String data = ToolJson.toJson(reqDownload);
    return sendPost(url, data);
  }

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

  // >>>----------------------- downloadFile -----------------------

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

  private String getTempPath() {
    return ToolFile.joinFileName(path_temp, Tool.toString(UUID.randomUUID()));
  }

  private boolean hasProtectedPath() {
    Set<String> sets = ConfigForsrc.getProtectedFilePath();
    return sets != null && sets.size() > 0;
  }

  @SneakyThrows
  private void deletePath(String filePath) {
    File file = new File(filePath);
    if (file.exists()) {
      FileUtils.forceDelete(file);
    }
  }

  // >>>----------------------- tool -----------------------

  // >>----------------------- private -----------------------

}