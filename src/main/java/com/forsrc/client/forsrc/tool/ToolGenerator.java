package com.forsrc.client.forsrc.tool;

import com.forsrc.client.common.constant.ConfigForsrc;
import com.forsrc.common.cipher.tool.ToolBase64;
import com.forsrc.common.constant.Code;
import com.forsrc.data.common.constant.Enum;
import com.forsrc.common.exception.CommonException;
import com.forsrc.common.tool.Tool;
import com.forsrc.common.tool.ToolFile;
import com.forsrc.common.tool.ToolZip;
import com.forsrc.data.common.tool.ToolData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class ToolGenerator {

  private static final String pack_forbid = "com.forsrc";
  private static final String sep_pack = ".";
  private static final String sep_table = ",";

  // <<----------------------- public -----------------------

  // <<<----------------------- normal -----------------------

  public static String zipData(String source) {
    long start = System.currentTimeMillis();
    byte[] bytes = ToolZip.zip(Tool.toBytes(source));
    String target = ToolBase64.encode(bytes);
    long end = System.currentTimeMillis();
    int cost = (int) (end - start);
    int sizeSource = source == null ? 0 : source.length();
    int sizeTarget = target == null ? 0 : target.length();
    double ratio = Tool.toScale(sizeTarget * 100.0 / sizeSource, 1);
    log.info("zip source: {}. target: {}. ratio: {}%. cost: {} ms.", sizeSource, sizeTarget, ratio, cost);
    return target;
  }

  // >>>----------------------- normal -----------------------

  // >>----------------------- public -----------------------

  // <<----------------------- private -----------------------

  // <<<----------------------- normal -----------------------

  // >>>----------------------- normal -----------------------

  // <<<----------------------- checkConfig -----------------------

  public static void checkConfig() {
    checkFilesource();
    checkDatasource();
    checkGenerator();
    checkTables();
    checkAuthorization();
  }

  public static boolean isGeneratorFromFile() {
    return ConfigForsrc.forsrc.generator.fromFile;
  }

  // >>>----------------------- checkConfig -----------------------

  // <<<----------------------- checkFilesource -----------------------

  private static void checkFilesource() {
    if (!isGeneratorFromFile()) {
      return;
    }
    checkFilesourceDocFile();
  }

  private static void checkFilesourceDocFile() {
    String docFile = ConfigForsrc.forsrc.filesource.docFile;
    Assert.notNull(docFile, "????????? 'forsrc.filesource.doc-file' ????????????.");
    if (!ToolFile.existFile(docFile)) {
      throw new CommonException(Code.PARAM_EMPTY.getCode(), "????????? 'forsrc.filesource.doc-file' ????????????????????????.");
    }
  }

  // >>>----------------------- checkFilesource -----------------------

  // <<<----------------------- checkDatasource -----------------------

  private static void checkDatasource() {
    checkDatasourceDatabase();
    if (isGeneratorFromFile()) {
      return;
    }
    checkDatasourceType();
    checkDatasourceHost();
    checkDatasourcePort();
    checkDatasourceUsername();
    checkDatasourcePassword();
  }

  private static void checkDatasourceType() {
    String type = ConfigForsrc.forsrc.datasource.type;
    Assert.notNull(type, "????????? 'forsrc.datasource.type' ????????????.");
    Enum.DatabaseType databaseType = Enum.DatabaseType.get(type);
    if (databaseType == null) {
      throw new CommonException(Code.PARAM_INVALID.getCode(), "?????????????????????. ????????? 'forsrc.datasource.type': " + type);
    }
  }

  private static void checkDatasourceDatabase() {
    String database = ConfigForsrc.forsrc.datasource.database;
    Assert.notNull(database, "????????? 'forsrc.datasource.database' ????????????.");
  }

  private static void checkDatasourceHost() {
    String host = ConfigForsrc.forsrc.datasource.host;
    Assert.notNull(host, "????????? 'forsrc.datasource.host' ????????????.");
  }

  private static void checkDatasourcePort() {
    int port = ConfigForsrc.forsrc.datasource.port;
    if (port <= 0) {
      throw new CommonException(Code.PARAM_INVALID.getCode(), "????????? 'forsrc.datasource.port' ????????????.");
    }
  }

  private static void checkDatasourceUsername() {
    String username = ConfigForsrc.forsrc.datasource.username;
    Assert.notNull(username, "????????? 'forsrc.datasource.username' ????????????.");
  }

  private static void checkDatasourcePassword() {
    String password = ConfigForsrc.forsrc.datasource.password;
    Assert.notNull(password, "????????? 'forsrc.datasource.password' ????????????.");
  }

  // >>>----------------------- checkDatasource -----------------------

  // <<<----------------------- checkGenerator -----------------------

  private static void checkGenerator() {
    checkApplication();
    checkDatabase();
    checkOption();
    checkProject();
    checkOutput();
  }

  // >>>----------------------- checkGenerator -----------------------

  // <<<----------------------- checkApplication -----------------------

  private static void checkApplication() {
    checkAppKey();
    checkVersion();
  }

  private static void checkAppKey() {
    String value = ConfigForsrc.forsrc.generator.application.appKey;
    Assert.notNull(value, "????????? 'forsrc.generator.application.appKey' ????????????.");
  }

  private static void checkVersion() {
    String value = ConfigForsrc.forsrc.generator.application.version;
    Assert.notNull(value, "????????? 'forsrc.generator.application.version' ????????????.");
  }

  // >>>----------------------- checkApplication -----------------------

  // <<<----------------------- checkDatabase -----------------------

  private static void checkDatabase() {
    checkDatabaseType();
    if (!ConfigForsrc.forsrc.generator.output.generatorSql) {
      return;
    }
    checkSqlVersion();
    checkSqlComment();
  }

  private static void checkDatabaseType() {
    String value = ConfigForsrc.forsrc.generator.database.type;
    Assert.notNull(value, "????????? 'forsrc.generator.database.type' ????????????.");
  }

  private static void checkSqlVersion() {
    String value = ConfigForsrc.forsrc.generator.database.sqlVersion;
    Assert.notNull(value, "????????? 'forsrc.generator.database.sql-version' ????????????.");
  }

  private static void checkSqlComment() {

  }

  // >>>----------------------- checkDatabase -----------------------

  // <<<----------------------- checkOption -----------------------

  private static void checkOption() {

  }

  // >>>----------------------- checkOption -----------------------

  // <<<----------------------- checkProject -----------------------

  private static void checkProject() {
    String projectName = ConfigForsrc.forsrc.generator.project.name;
    Assert.notNull(projectName, "????????? 'forsrc.generator.project.name' ????????????.");
    String projectTitle = ConfigForsrc.forsrc.generator.project.title;
    Assert.notNull(projectTitle, "????????? 'forsrc.generator.project.title' ????????????.");
    checkAppPack();
  }

  private static void checkAppPack() {
    String appPack = ConfigForsrc.forsrc.generator.project.appPack;
    Assert.notNull(appPack, "????????? 'forsrc.generator.project.app-pack' ????????????.");
    String[] packs = Tool.split(appPack, sep_pack);
    for (String pack : packs) {
      if (!ToolData.isParamName(pack)) {
        throw new CommonException(Code.PARAM_INVALID.getCode(), "????????? 'forsrc.generator.project.app-pack' ??????????????? '" + pack + "' ??????.");
      }
    }
    appPack = Tool.toLower(appPack);
    if (appPack.equals(pack_forbid) || appPack.startsWith(pack_forbid + sep_pack)) {
      throw new CommonException(Code.PARAM_INVALID.getCode(), "????????? 'forsrc.generator.project.app-pack' ???????????????????????? '" + pack_forbid + "' ??????.");
    }
  }

  // >>>----------------------- checkProject -----------------------

  // <<<----------------------- checkOutput -----------------------

  private static void checkOutput() {
    checkOutputSaveAppPath();
    checkOutputSaveWebPath();
    checkOutputSaveSqlPath();
    checkEnableGenerator();
  }

  private static void checkOutputSaveAppPath() {
    if (!ConfigForsrc.forsrc.generator.output.generatorApp) {
      return;
    }
    String saveAppPath = ConfigForsrc.forsrc.generator.output.saveAppPath;
    Assert.notNull(saveAppPath, "????????? 'forsrc.generator.output.save-app-path' ????????????.");
    if (!ToolFile.existPath(saveAppPath)) {
      throw new CommonException(Code.PARAM_INVALID.getCode(), "????????? 'forsrc.generator.output.save-app-path' ????????????????????????.");
    }
  }

  private static void checkOutputSaveWebPath() {
    if (!ConfigForsrc.forsrc.generator.output.generatorWeb) {
      return;
    }
    String saveWebPath = ConfigForsrc.forsrc.generator.output.saveWebPath;
    Assert.notNull(saveWebPath, "????????? 'forsrc.generator.output.save-web-path' ????????????.");
    if (!ToolFile.existPath(saveWebPath)) {
      throw new CommonException(Code.PARAM_INVALID.getCode(), "????????? 'forsrc.generator.output.save-web-path' ????????????????????????.");
    }
  }

  private static void checkOutputSaveSqlPath() {
    if (!ConfigForsrc.forsrc.generator.output.generatorSql) {
      return;
    }
  }

  private static void checkEnableGenerator() {
    if (ConfigForsrc.forsrc.generator.output.generatorApp) {
      return;
    }
    if (ConfigForsrc.forsrc.generator.output.generatorWeb) {
      return;
    }
    if (ConfigForsrc.forsrc.generator.output.generatorSql) {
      return;
    }
    throw new CommonException(Code.PARAM_INVALID.getCode(), "??????????????????????????????????????????????????????????????????????????? 'forsrc.generator.output.generator-src' ?????? 'true'.");
  }

  // >>>----------------------- checkOutput -----------------------

  // <<<----------------------- checkTables -----------------------

  private static void checkTables() {
    String tables = ConfigForsrc.forsrc.generator.filter.table.tables;
    if (Tool.isNull(tables)) {
      return;
    }
    tables = Tool.toLower(tables);
    String[] names = Tool.split(tables, sep_table);
    if (names == null || names.length <= 1) {
      return;
    }
    Set<String> set = new HashSet<>();
    for (String name : names) {
      if (set.contains(name)) {
        throw new CommonException(Code.PARAM_INVALID.getCode(), "??????????????????. table: " + name + ". ????????? 'forsrc.generator.filter.table.tables'.");
      }
      set.add(name);
    }
  }

  // >>>----------------------- checkTables -----------------------

  // <<<----------------------- checkAuthorization -----------------------

  private static void checkAuthorization() {
    String username = ConfigForsrc.forsrc.authorization.username;
    String secret = ConfigForsrc.forsrc.authorization.secret;
    if (Tool.isNull(username)) {
      throw new CommonException(Code.PARAM_INVALID.getCode(), "???????????? username. ????????? 'forsrc.authorization.username'.");
    }
    if (Tool.isNull(secret)) {
      throw new CommonException(Code.PARAM_INVALID.getCode(), "???????????? secret. ????????? 'forsrc.authorization.secret'.");
    }
  }

  // >>>----------------------- checkAuthorization -----------------------

  // <<<----------------------- tool -----------------------

  private static String getRandom() {
    long ms = System.currentTimeMillis();
    long rand = Tool.getRandom();
    return ms + "." + rand;
  }

  // >>>----------------------- tool -----------------------

  // >>----------------------- private -----------------------

}