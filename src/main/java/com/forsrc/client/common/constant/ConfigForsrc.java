package com.forsrc.client.common.constant;

import com.forsrc.common.tool.Tool;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class ConfigForsrc {

  @Resource
  private ConfigProtected configProtected;

  @Getter
  private static Set<String> protectedFilePath;

  public static class forsrc {

    public static class authorization {
      public static String appid;
      public static String secret;
      public static String username;
      public static String password;
    }

    public static class server {
      public static String host;
      public static int port;

      public static class path {
        public static String generator;
        public static String download;
      }
    }

    public static class datasource {
      public static String type;
      public static String host;
      public static int port;
      public static String database;
      public static String username;
      public static String password;
    }

    public static class filesource {
      public static String docFile;
      public static boolean tableHasHeader;
      public static boolean checkReserveWord;

      public static class fieldPropertyColumn {
        public static int title;
        public static int name;
        public static int type;
        public static int notnull;
        public static int defaultValue;
        public static int description;
      }
    }

    public static class generator {
      public static boolean fromFile;

      public static class application {
        public static String language;
        public static String type;
        public static String framework;
        public static String databaseType;
        public static String web;
        public static String branch;
        public static String version;
      }

      public static class filter {
        public static class table {
          public static String namePrefix;
          public static String nameContain;
          public static String tableRegex;
          public static String tables;
        }
      }

      public static class option {
        public static class database {
          public static boolean ridTablePrefix;
          public static int selectFieldNum;
        }
      }

      public static class project {
        public static String name;
        public static String title;
        public static String srcPack;
      }

      public static class output {
//        public static boolean rewriteOnExist;
        public static boolean generatorSrc;
        public static boolean generatorWeb;
        public static boolean generatorSql;
        public static String saveSrcPath;
        public static String saveWebPath;
        public static String saveSqlPath;
      }
    }
  }

  @PostConstruct
  private void setValue() {
    setProtectedFileOrPath();
  }

  private void setProtectedFileOrPath() {
    protectedFilePath = new HashSet<>();
    List<String> filePaths = configProtected.getProtectedFilePath();
    if (filePaths == null) {
      return;
    }
    for (String filePath: filePaths) {
      filePath = Tool.replace(filePath, "/", File.separator);
      filePath = Tool.replace(filePath, "\\", File.separator);
//      filePath = Tool.replace(filePath, ".", File.separator);
//      filePath = Tool.replace(filePath, ":", ".");
      protectedFilePath.add(filePath);
    }
  }

  //forsrc-authorization
  @Value("${forsrc.authorization.appid:}")
  public void setForsrc_authorization_appid(String value) {
    forsrc.authorization.appid = Tool.toString(value);
  }

  @Value("${forsrc.authorization.secret:}")
  public void setForsrc_authorization_secret(String value) {
    forsrc.authorization.secret = Tool.toString(value);
  }

  @Value("${forsrc.authorization.username:}")
  public void setForsrc_authorization_username(String value) {
    forsrc.authorization.username = Tool.toString(value);
  }

  @Value("${forsrc.authorization.password:}")
  public void setForsrc_authorization_password(String value) {
    forsrc.authorization.password = Tool.toString(value);
  }

  //forsrc-server
  @Value("${forsrc.server.host:www.forsrc.com}")
  public void setForsrc_server_host(String value) {
    forsrc.server.host = Tool.toString(value);
  }

  @Value("${forsrc.server.port:80}")
  public void setForsrc_server_port(String value) {
    forsrc.server.port = Tool.toInt(value);
  }

  @Value("${forsrc.server.path.generator:forsrc/generator}")
  public void setForsrc_server_path_generator(String value) {
    forsrc.server.path.generator = Tool.toString(value);
  }

  @Value("${forsrc.server.path.download:forsrc/download}")
  public void setForsrc_server_path_download(String value) {
    forsrc.server.path.download = Tool.toString(value);
  }

  //forsrc-datasource
  @Value("${forsrc.datasource.type:mysql}")
  public void setForsrc_datasource_type(String value) {
    forsrc.datasource.type = Tool.toString(value);
  }

  @Value("${forsrc.datasource.host:127.0.0.1}")
  public void setForsrc_datasource_host(String value) {
    forsrc.datasource.host = Tool.toString(value);
  }

  @Value("${forsrc.datasource.port:3306}")
  public void setForsrc_datasource_port(String value) {
    forsrc.datasource.port = Tool.toInt(value);
  }

  @Value("${forsrc.datasource.database}")
  public void setForsrc_datasource_database(String value) {
    forsrc.datasource.database = Tool.toString(value);
  }

  @Value("${forsrc.datasource.username}")
  public void setForsrc_datasource_username(String value) {
    forsrc.datasource.username = Tool.toString(value);
  }

  @Value("${forsrc.datasource.password}")
  public void setForsrc_datasource_password(String value) {
    forsrc.datasource.password = Tool.toString(value);
  }

  //forsrc-filesource
  @Value("${forsrc.filesource.doc-file:false}")
  public void setForsrc_filesource_doc_file(String value) {
    forsrc.filesource.docFile = Tool.toString(value);
  }

  @Value("${forsrc.filesource.table-has-header:}")
  public void setForsrc_filesource_tableHasHeader(String value) {
    forsrc.filesource.tableHasHeader = Tool.toBoolean(value);
  }

  @Value("${forsrc.filesource.check-reserve-word:true}")
  public void setForsrc_filesource_checkReserveWord(String value) {
    forsrc.filesource.checkReserveWord = Tool.toBoolean(value);
  }

  //forsrc-filesource-field-property-column
  @Value("${forsrc.filesource.field-property-column.title:1}")
  public void setForsrc_filesource_field_property_column_title(String value) {
    forsrc.filesource.fieldPropertyColumn.title = Tool.toInt(value);
  }

  @Value("${forsrc.filesource.field-property-column.name:2}")
  public void setForsrc_filesource_field_property_column_name(String value) {
    forsrc.filesource.fieldPropertyColumn.name = Tool.toInt(value);
  }

  @Value("${forsrc.filesource.field-property-column.type:3}")
  public void setForsrc_filesource_field_property_column_type(String value) {
    forsrc.filesource.fieldPropertyColumn.type = Tool.toInt(value);
  }

  @Value("${forsrc.filesource.field-property-column.notnull:4}")
  public void setForsrc_filesource_field_property_column_notnull(String value) {
    forsrc.filesource.fieldPropertyColumn.notnull = Tool.toInt(value);
  }

  @Value("${forsrc.filesource.field-property-column.default-value:5}")
  public void setForsrc_filesource_field_property_column_defaultValue(String value) {
    forsrc.filesource.fieldPropertyColumn.defaultValue = Tool.toInt(value);
  }

  @Value("${forsrc.filesource.field-property-column.description:6}")
  public void setForsrc_filesource_field_property_column_description(String value) {
    forsrc.filesource.fieldPropertyColumn.description = Tool.toInt(value);
  }

  //forsrc-generator
  @Value("${forsrc.generator.from-file:false}")
  public void setForsrc_generator_fromFile(String value) {
    forsrc.generator.fromFile = Tool.toBoolean(value);
  }

  //forsrc-generator-application
  @Value("${forsrc.generator.application.language:java}")
  public void setForsrc_generator_application_language(String value) {
    forsrc.generator.application.language = Tool.toString(value);
  }

  @Value("${forsrc.generator.application.type:springboot}")
  public void setForsrc_generator_application_type(String value) {
    forsrc.generator.application.type = Tool.toString(value);
  }

  @Value("${forsrc.generator.application.framework:spring}")
  public void setForsrc_generator_application_framework(String value) {
    forsrc.generator.application.framework = Tool.toString(value);
  }

  @Value("${forsrc.generator.application.database-type:mysql}")
  public void setForsrc_generator_application_databaseType(String value) {
    forsrc.generator.application.databaseType = Tool.toString(value);
  }

  @Value("${forsrc.generator.application.web:vue}")
  public void setForsrc_generator_application_web(String value) {
    forsrc.generator.application.web = Tool.toString(value);
  }

  @Value("${forsrc.generator.application.branch:base}")
  public void setForsrc_generator_application_branch(String value) {
    forsrc.generator.application.branch = Tool.toString(value);
  }

  @Value("${forsrc.generator.application.version:1.0}")
  public void setForsrc_generator_application_version(String value) {
    forsrc.generator.application.version = Tool.toString(value);
  }

  //forsrc-generator-filter
  @Value("${forsrc.generator.filter.table.name-prefix}")
  public void setForsrc_generator_filter_table_name_prefix(String value) {
    forsrc.generator.filter.table.namePrefix = Tool.toString(value);
  }

  @Value("${forsrc.generator.filter.table.name-contain}")
  public void setForsrc_generator_filter_table_name_contain(String value) {
    forsrc.generator.filter.table.nameContain = Tool.toString(value);
  }

  @Value("${forsrc.generator.filter.table.table-regex}")
  public void setForsrc_generator_filter_table_table_regex(String value) {
    forsrc.generator.filter.table.tableRegex = Tool.toString(value);
  }

  @Value("${forsrc.generator.filter.table.tables}")
  public void setForsrc_generator_filter_table_tables(String value) {
    forsrc.generator.filter.table.tables = Tool.toString(value);
  }

  //forsrc-generator-option-database
  @Value("${forsrc.generator.option.database.rid-table-prefix:false}")
  public void setForsrc_generator_option_database_ridTablePrefix(String value) {
    forsrc.generator.option.database.ridTablePrefix = Tool.toBoolean(value);
  }

  @Value("${forsrc.generator.option.database.select-field-num:10}")
  public void setForsrc_generator_option_database_selectFieldNum(String value) {
    forsrc.generator.option.database.selectFieldNum = Tool.toInt(value);
  }

  //forsrc-generator-project
  @Value("${forsrc.generator.project.name:}")
  public void setForsrc_generator_project_name(String value) {
    forsrc.generator.project.name = Tool.toString(value);
  }

  @Value("${forsrc.generator.project.title:}")
  public void setForsrc_generator_project_title(String value) {
    forsrc.generator.project.title = Tool.toString(value);
  }

  @Value("${forsrc.generator.project.src-pack}")
  public void setForsrc_generator_project_srcPack(String value) {
    forsrc.generator.project.srcPack = Tool.toString(value);
  }

  //forsrc-generator-output
//  @Value("${forsrc.generator.output.rewrite-on-exist:false}")
//  public void setForsrc_generator_output_rewriteOnExist(String value) {
//    forsrc.generator.output.rewriteOnExist = Tool.toBoolean(value);
//  }

  @Value("${forsrc.generator.output.generator-src:true}")
  public void setForsrc_generator_output_generatorSrc(String value) {
    forsrc.generator.output.generatorSrc = Tool.toBoolean(value);
  }

  @Value("${forsrc.generator.output.generator-web:false}")
  public void setForsrc_generator_output_generatorWeb(String value) {
    forsrc.generator.output.generatorWeb = Tool.toBoolean(value);
  }

  @Value("${forsrc.generator.output.generator-sql:false}")
  public void setForsrc_generator_output_generatorSql(String value) {
    forsrc.generator.output.generatorSql = Tool.toBoolean(value);
  }

  @Value("${forsrc.generator.output.save-src-path}")
  public void setForsrc_generator_output_saveSrcPath(String value) {
    forsrc.generator.output.saveSrcPath = Tool.toString(value);
  }

  @Value("${forsrc.generator.output.save-web-path}")
  public void setForsrc_generator_output_saveWebPath(String value) {
    forsrc.generator.output.saveWebPath = Tool.toString(value);
  }

  @Value("${forsrc.generator.output.save-sql-path}")
  public void setForsrc_generator_output_saveSqlPath(String value) {
    forsrc.generator.output.saveSqlPath = Tool.toString(value);
  }

}

