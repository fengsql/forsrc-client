package com.forsrc.client.forsrc.generator.item;

import com.forsrc.client.common.constant.ConfigForsrc;
import com.forsrc.client.forsrc.generator.base.BForsrc;
import com.forsrc.client.forsrc.tool.ToolGenerator;
import com.forsrc.common.tool.Tool;
import com.forsrc.common.tool.ToolJson;
import com.forsrc.data.common.param.ParamDatabase;
import com.forsrc.data.database.bean.DbDatabase;
import com.forsrc.data.database.load.item.LoadDatabase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoadDb extends BForsrc {

  // <<----------------------- public -----------------------

  // <<<----------------------- normal -----------------------

  public String load() {
    return loadDatabase();
  }

  // >>>----------------------- normal -----------------------

  // >>----------------------- public -----------------------

  // <<----------------------- private -----------------------

  // <<<----------------------- normal -----------------------

  // >>>----------------------- normal -----------------------

  // <<<----------------------- loadDatabase -----------------------

  private String loadDatabase() {
    String databaseName = ConfigForsrc.forsrc.datasource.database;
    String namePrefix = ConfigForsrc.forsrc.generator.filter.table.namePrefix;
    String nameContain = ConfigForsrc.forsrc.generator.filter.table.nameContain;
    String tableRegex = ConfigForsrc.forsrc.generator.filter.table.tableRegex;
    String tables = ConfigForsrc.forsrc.generator.filter.table.tables;

    log.info("load database start. database: {}. namePrefix: '{}'. nameContain: '{}'. tableRegex: '{}'. tables: '{}'", databaseName, namePrefix, nameContain, tableRegex, tables);
    ParamDatabase paramDatabase = createParamDatabase();
    LoadDatabase loadDatabase = new LoadDatabase();
    loadDatabase.load(paramDatabase);
    DbDatabase dbDatabase = loadDatabase.getDbDatabase();
    int tableCount = getTableCount(dbDatabase);
    if (tableCount == 0) {
      log.warn("load database fail. tableCount: {}.", tableCount);
      return null;
    }
    log.info("load database ok. tableCount: {}.", tableCount);
    return getDatabase(dbDatabase);
  }

  private String getDatabase(DbDatabase dbDatabase) {
    String json = ToolJson.toJson(dbDatabase);
    return ToolGenerator.zipData(json);
  }

  // >>>----------------------- loadDatabase -----------------------

  // <<<----------------------- tool -----------------------

  private ParamDatabase createParamDatabase() {
    ParamDatabase paramDatabase = new ParamDatabase();
    paramDatabase.setDatabaseName(Tool.toLower(ConfigForsrc.forsrc.datasource.database));
    paramDatabase.setNamePrefix(Tool.toLower(ConfigForsrc.forsrc.generator.filter.table.namePrefix));
    paramDatabase.setNameContain(Tool.toLower(ConfigForsrc.forsrc.generator.filter.table.nameContain));
    paramDatabase.setTableRegex(Tool.toLower(ConfigForsrc.forsrc.generator.filter.table.tableRegex));
    paramDatabase.setTables(Tool.toLower(ConfigForsrc.forsrc.generator.filter.table.tables));
    return paramDatabase;
  }

  private int getTableCount(DbDatabase dbDatabase) {
    if (dbDatabase == null || dbDatabase.getDbTables() == null) {
      return 0;
    }
    return dbDatabase.getDbTables().size();
  }

  // >>>----------------------- tool -----------------------

  // >>----------------------- private -----------------------

}