package com.forsrc.client.forsrc.tool;

import com.forsrc.client.common.constant.ConfigForsrc;
import com.forsrc.common.cipher.tool.ToolBase64;
import com.forsrc.common.constant.Code;
import com.forsrc.common.constant.Enum;
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

  private static final String pack_forbid = "com.forsrc.";
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
    Assert.notNull(docFile, "配置项 'forsrc.filesource.doc-file' 没有指定.");
    if (!ToolFile.existFile(docFile)) {
      throw new CommonException(Code.PARAM_EMPTY.getCode(), "配置项 'forsrc.filesource.doc-file' 指定的文件不存在.");
    }
  }

  // >>>----------------------- checkFilesource -----------------------

  // <<<----------------------- checkDatasource -----------------------

  private static void checkDatasource() {
    checkDatasourceType();
    checkDatasourceDatabase();
    if (isGeneratorFromFile()) {
      return;
    }
    checkDatasourceHost();
    checkDatasourcePort();
    checkDatasourceUsername();
    checkDatasourcePassword();
  }

  private static void checkDatasourceType() {
    String type = ConfigForsrc.forsrc.datasource.type;
    Assert.notNull(type, "配置项 'forsrc.datasource.type' 没有指定.");
    Enum.DatabaseType databaseType = Enum.DatabaseType.get(type);
    if (databaseType == null) {
      throw new CommonException(Code.PARAM_INVALID.getCode(), "数据库类型无效. 配置项 'forsrc.datasource.type': " + type);
    }
  }

  private static void checkDatasourceDatabase() {
    String database = ConfigForsrc.forsrc.datasource.database;
    Assert.notNull(database, "配置项 'forsrc.datasource.database' 没有指定.");
  }

  private static void checkDatasourceHost() {
    String host = ConfigForsrc.forsrc.datasource.host;
    Assert.notNull(host, "配置项 'forsrc.datasource.host' 没有指定.");
  }

  private static void checkDatasourcePort() {
    int port = ConfigForsrc.forsrc.datasource.port;
    if (port <= 0) {
      throw new CommonException(Code.PARAM_INVALID.getCode(), "配置项 'forsrc.datasource.port' 没有指定.");
    }
  }

  private static void checkDatasourceUsername() {
    String username = ConfigForsrc.forsrc.datasource.username;
    Assert.notNull(username, "配置项 'forsrc.datasource.username' 没有指定.");
  }

  private static void checkDatasourcePassword() {
    String password = ConfigForsrc.forsrc.datasource.password;
    Assert.notNull(password, "配置项 'forsrc.datasource.password' 没有指定.");
  }

  // >>>----------------------- checkDatasource -----------------------

  // <<<----------------------- checkGenerator -----------------------

  private static void checkGenerator() {
    checkApplication();
    checkOption();
    checkProject();
    checkOutput();
  }

  // >>>----------------------- checkGenerator -----------------------

  // <<<----------------------- checkApplication -----------------------

  private static void checkApplication() {
    checkLanguage();
    checkDatabaseType();
    checkApp();
  }

  private static void checkLanguage() {
    String value = ConfigForsrc.forsrc.generator.application.language;
    Assert.notNull(value, "配置项 'forsrc.generator.application.language' 没有指定.");
    //    Enum.LanguageType languageType = Enum.LanguageType.get(value);
    //    if (languageType == null) {
    //      throw new CommonException(Code.PARAM_INVALID.getCode(), "语言类型无效. 配置项 'forsrc.generator.application.language': " + value);
    //    }
  }

  private static void checkDatabaseType() {
    String value = ConfigForsrc.forsrc.generator.application.databaseType;
    Assert.notNull(value, "配置项 'forsrc.generator.application.database-type' 没有指定.");
    Enum.DatabaseType databaseType = Enum.DatabaseType.get(value);
    if (databaseType == null) {
      throw new CommonException(Code.PARAM_INVALID.getCode(), "数据库类型无效. 配置项 'forsrc.generator.application.database-type': " + value);
    }
  }

  // >>>----------------------- checkApplication -----------------------

  // <<<----------------------- checkApp -----------------------

  private static void checkApp() {
    if (!assignAppKey()) {
      checkType();
      checkFramework();
      checkWeb();
      checkBranch();
    }
    checkVersion();
  }

  private static boolean assignAppKey() {
    String appKey = ConfigForsrc.forsrc.generator.application.appKey;
    return !Tool.isNull(appKey);
  }

  private static void checkType() {
    String type = ConfigForsrc.forsrc.generator.application.type;
    Assert.notNull(type, "配置项 'forsrc.generator.application.type' 没有指定.");
    //    Enum.ApplicationType applicationType = Enum.ApplicationType.get(type);
    //    if (applicationType == null) {
    //      throw new CommonException(Code.PARAM_INVALID.getCode(), "应用类型无效. 配置项 'forsrc.generator.application.type': " + type);
    //    }
  }

  private static void checkFramework() {
    String framework = ConfigForsrc.forsrc.generator.application.framework;
    Assert.notNull(framework, "配置项 'forsrc.generator.application.framework' 没有指定.");
    //    Enum.FrameworkType frameworkType = Enum.FrameworkType.get(framework);
    //    if (frameworkType == null) {
    //      throw new CommonException(Code.PARAM_INVALID.getCode(), "框架类型无效. 配置项 'forsrc.generator.application.framework': " + framework);
    //    }
  }

  private static void checkWeb() {
    String value = ConfigForsrc.forsrc.generator.application.web;
    Assert.notNull(value, "配置项 'forsrc.generator.application.web' 没有指定.");
  }

  private static void checkBranch() {
    String value = ConfigForsrc.forsrc.generator.application.branch;
    Assert.notNull(value, "配置项 'forsrc.generator.application.branch' 没有指定.");
  }

  private static void checkVersion() {
    String value = ConfigForsrc.forsrc.generator.application.version;
    Assert.notNull(value, "配置项 'forsrc.generator.application.version' 没有指定.");
  }

  // >>>----------------------- checkApp -----------------------

  // <<<----------------------- checkOption -----------------------

  private static void checkOption() {

  }

  // >>>----------------------- checkOption -----------------------

  // <<<----------------------- checkProject -----------------------

  private static void checkProject() {
    String projectName = ConfigForsrc.forsrc.generator.project.name;
    Assert.notNull(projectName, "配置项 'forsrc.generator.project.name' 没有指定.");
    String projectTitle = ConfigForsrc.forsrc.generator.project.title;
    Assert.notNull(projectTitle, "配置项 'forsrc.generator.project.title' 没有指定.");
    checkOutputSrcPack();
  }

  private static void checkOutputSrcPack() {
    String srcPack = ConfigForsrc.forsrc.generator.project.srcPack;
    Assert.notNull(srcPack, "配置项 'forsrc.generator.project.src-pack' 没有指定.");
    String[] packs = Tool.split(srcPack, sep_pack);
    for (String pack : packs) {
      if (!ToolData.isParamName(pack)) {
        throw new CommonException(Code.PARAM_INVALID.getCode(), "配置项 'forsrc.generator.project.src-pack' 中的包名称 '" + pack + "' 无效.");
      }
    }
    if (Tool.toLower(srcPack).startsWith(pack_forbid)) {
      throw new CommonException(Code.PARAM_INVALID.getCode(), "配置项 'forsrc.generator.project.src-pack' 指定的包名不能以 '" + pack_forbid + "' 开头.");
    }
  }

  // >>>----------------------- checkProject -----------------------

  // <<<----------------------- checkOutput -----------------------

  private static void checkOutput() {
    checkOutputSaveSrcPath();
    checkOutputSaveWebPath();
    checkOutputSaveSqlPath();
    checkEnableGenerator();
  }

  private static void checkOutputSaveSrcPath() {
    if (!ConfigForsrc.forsrc.generator.output.generatorSrc) {
      return;
    }
    String saveSrcPath = ConfigForsrc.forsrc.generator.output.saveSrcPath;
    Assert.notNull(saveSrcPath, "配置项 'forsrc.generator.output.save-src-path' 没有指定.");
    if (!ToolFile.existPath(saveSrcPath)) {
      throw new CommonException(Code.PARAM_INVALID.getCode(), "配置项 'forsrc.generator.output.save-src-path' 指定的路径不存在.");
    }
  }

  private static void checkOutputSaveWebPath() {
    if (!ConfigForsrc.forsrc.generator.output.generatorWeb) {
      return;
    }
    String saveWebPath = ConfigForsrc.forsrc.generator.output.saveWebPath;
    Assert.notNull(saveWebPath, "配置项 'forsrc.generator.output.save-web-path' 没有指定.");
    if (!ToolFile.existPath(saveWebPath)) {
      throw new CommonException(Code.PARAM_INVALID.getCode(), "配置项 'forsrc.generator.output.save-web-path' 指定的路径不存在.");
    }
  }

  private static void checkOutputSaveSqlPath() {
    if (!ConfigForsrc.forsrc.generator.output.generatorSql) {
      return;
    }
    //    String saveSqlPath = ConfigForsrc.forsrc.generator.output.saveSqlPath;
    //    Assert.notNull(saveSqlPath, "配置项 'forsrc.generator.output.save-sql-path' 没有指定.");
    //    if (!ToolFile.existPath(saveSqlPath)) {
    //      throw new CommonException(Code.PARAM_INVALID.getCode(), "配置项 'forsrc.generator.output.save-sql-path' 指定的路径不存在.");
    //    }
  }

  private static void checkEnableGenerator() {
    if (ConfigForsrc.forsrc.generator.output.generatorSrc) {
      return;
    }
    if (ConfigForsrc.forsrc.generator.output.generatorWeb) {
      return;
    }
    if (ConfigForsrc.forsrc.generator.output.generatorSql) {
      return;
    }
    throw new CommonException(Code.PARAM_INVALID.getCode(), "没有需要生成的栏目，至少需要开启一个栏目，如配置项 'forsrc.generator.output.generator-src' 设为 'true'.");
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
        throw new CommonException(Code.PARAM_INVALID.getCode(), "重复指定表名. table: " + name + ". 配置项 'forsrc.generator.filter.table.tables'.");
      }
      set.add(name);
    }
  }

  // >>>----------------------- checkTables -----------------------

  // <<<----------------------- checkAuthorization -----------------------

  private static void checkAuthorization() {
    String appid = ConfigForsrc.forsrc.authorization.appid;
    String secret = ConfigForsrc.forsrc.authorization.secret;
    String username = ConfigForsrc.forsrc.authorization.username;
    String userkey = ConfigForsrc.forsrc.authorization.userkey;
    if (!Tool.isNull(appid)) {
      if (Tool.isNull(secret)) {
        throw new CommonException(Code.PARAM_INVALID.getCode(), "没有指定 secret. 配置项 'forsrc.authorization.secret'.");
      }
    } else {
      if (!Tool.isNull(username)) {
        if (Tool.isNull(userkey)) {
          throw new CommonException(Code.PARAM_INVALID.getCode(), "没有指定 userkey. 配置项 'forsrc.authorization.userkey'.");
        }
      }
    }
  }

  private static void checkAuthorizationAppid() {

  }

  private static void checkAuthorizationUsername() {

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