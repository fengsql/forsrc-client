package com.forsrc.client.forsrc.tool;

import com.forsrc.common.tool.Tool;
import com.forsrc.data.common.constant.Code;
import com.forsrc.data.common.exception.LocalException;
import com.forsrc.data.common.tool.ToolData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ToolDb {

  private static final String table_all = "*";
  private static final String table_separator = ",";
  private static final String table_type = "base table";

  // <<----------------------- public -----------------------

  // <<<----------------------- normal -----------------------

  // >>>----------------------- normal -----------------------

  // >>----------------------- public -----------------------

  // <<----------------------- private -----------------------

  // <<<----------------------- normal -----------------------

  private static void checkDatabase(String databaseName) {
    if (Tool.isNull(databaseName)) {
      return;
    }
    String name = databaseName.replace('-', '_');
    if (!ToolData.isParamName(name)) {
      throw new LocalException(Code.database_error.getCode(), "invalid database name. please check config 'forsrc.datasource.database'. database: " + databaseName);
    }
  }

  private static void checkTableNamePrefix(String tableNamePrefix) {
    if (!Tool.isNull(tableNamePrefix) && !ToolData.isParamName(tableNamePrefix)) {
      throw new LocalException(Code.database_error.getCode(), "invalid name prefix. please check config 'forsrc.generator.table.name-prefix'. value: " + tableNamePrefix);
    }
  }

  private static void checkTableNameContain(String tableNameContain) {
    if (!Tool.isNull(tableNameContain) && !ToolData.isParamName(tableNameContain)) {
      throw new LocalException(Code.database_error.getCode(), "invalid name contain. please check config 'forsrc.generator.table.name-contain'. value: " + tableNameContain);
    }
  }

  private static void checkTableName(String tableName) {
    if (Tool.isNull(tableName) || !ToolData.isParamName(tableName)) {
      throw new LocalException(Code.database_error.getCode(), "invalid table name. please check config 'forsrc.generator.table.tables'. table: " + tableName);
    }
  }

  // >>>----------------------- normal -----------------------

  // <<<----------------------- tool -----------------------

  // >>>----------------------- tool -----------------------

  // >>----------------------- private -----------------------

}