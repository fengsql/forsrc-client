package com.forsrc.client.forsrc.generator.item;

import com.forsrc.client.common.constant.ConfigForsrc;
import com.forsrc.client.forsrc.generator.base.BForsrc;
import com.forsrc.client.forsrc.tool.ToolGenerator;
import com.forsrc.common.constant.Code;
import com.forsrc.common.tool.Tool;
import com.forsrc.common.tool.ToolJson;
import com.forsrc.data.common.exception.LocalException;
import com.forsrc.data.common.param.ParamDocument;
import com.forsrc.data.document.bean.DocTable;
import com.forsrc.data.document.load.item.LoadDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LoadFile extends BForsrc {

  // <<----------------------- public -----------------------

  // <<<----------------------- normal -----------------------

  public String load() {
    return loadFile();
  }

  // >>>----------------------- normal -----------------------

  // >>----------------------- public -----------------------

  // <<----------------------- private -----------------------

  // <<<----------------------- normal -----------------------

  // >>>----------------------- normal -----------------------

  // <<<----------------------- loadDatabase -----------------------

  private String loadFile() {
    List<DocTable> list = loadDocument();
    return getData(list);
  }

  private List<DocTable> loadDocument() {
    ParamDocument paramDocument = createParamDocument();
    log.info("准备载入文档. tableHasHeader: {}. checkReserveWord: {}. file: {}", paramDocument.isTableHasHeader(), paramDocument.isCheckReserveWord(), paramDocument.getDocFile());
    log.info("databaseType: {}. databaseName: {}. tableNamePrefix: '{}'. tableNameContain: '{}'. tables: '{}'.", paramDocument.getDatabaseType(), paramDocument.getDatabaseName(), paramDocument.getNamePrefix(), paramDocument.getNameContain(), paramDocument.getTables());
    LoadDocument loadDocument = new LoadDocument();
    List<DocTable> list = loadDocument.load(paramDocument);
    if (list == null || list.size() == 0) {
      throw new LocalException(Code.ERROR.getCode(), "没有需要生成的表信息. file: " + paramDocument.getDocFile());
    }
    String tables = getTablesDebug(list);
    log.info("载入文档完毕. 共 {} 个表. tables: \n{}.", list.size(), tables);
    return list;
  }

  private String getData(List<DocTable> list) {
    String json = ToolJson.toJson(list);
    return ToolGenerator.zipData(json);
  }

  // >>>----------------------- loadDatabase -----------------------

  // <<<----------------------- outDebug -----------------------

  private String getTablesDebug(List<DocTable> list) {
    StringBuilder stringBuilder = new StringBuilder();
    int size = list.size();
    for (int i = 0; i < size; i++) {
      DocTable docTable = list.get(i);
      stringBuilder.append(docTable.getName());
      if (i < size - 1) {
        stringBuilder.append(", ");
      }
      if ((i + 1) % 5 == 0) {
        stringBuilder.append("\n");
      }
    }
    return stringBuilder.toString();
  }

  // >>>----------------------- outDebug -----------------------

  // <<<----------------------- tool -----------------------

  private ParamDocument createParamDocument() {
    ParamDocument paramDocument = new ParamDocument();

    paramDocument.setDocFile(ConfigForsrc.forsrc.filesource.docFile);
    paramDocument.setTableHasHeader(ConfigForsrc.forsrc.filesource.tableHasHeader);
    paramDocument.setCheckReserveWord(ConfigForsrc.forsrc.filesource.checkReserveWord);

    paramDocument.setDatabaseType(Tool.toLower(ConfigForsrc.forsrc.datasource.type));
    paramDocument.setDatabaseName(Tool.toLower(ConfigForsrc.forsrc.datasource.database));
    paramDocument.setNamePrefix(Tool.toLower(ConfigForsrc.forsrc.generator.filter.table.namePrefix));
    paramDocument.setNameContain(Tool.toLower(ConfigForsrc.forsrc.generator.filter.table.nameContain));
    paramDocument.setTableRegex(Tool.toLower(ConfigForsrc.forsrc.generator.filter.table.tableRegex));
    paramDocument.setTables(Tool.toLower(ConfigForsrc.forsrc.generator.filter.table.tables));

    paramDocument.setColumnTitle(ConfigForsrc.forsrc.filesource.fieldPropertyColumn.title);
    paramDocument.setColumnName(ConfigForsrc.forsrc.filesource.fieldPropertyColumn.name);
    paramDocument.setColumnType(ConfigForsrc.forsrc.filesource.fieldPropertyColumn.type);
    paramDocument.setColumnNotnull(ConfigForsrc.forsrc.filesource.fieldPropertyColumn.notnull);
    paramDocument.setColumnDefaultValue(ConfigForsrc.forsrc.filesource.fieldPropertyColumn.defaultValue);
    paramDocument.setColumnDescription(ConfigForsrc.forsrc.filesource.fieldPropertyColumn.description);
    return paramDocument;
  }

  // >>>----------------------- tool -----------------------

  // >>----------------------- private -----------------------

}