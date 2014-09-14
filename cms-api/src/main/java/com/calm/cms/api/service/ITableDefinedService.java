package com.calm.cms.api.service;

import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.entity.TableDefined;
import com.calm.framework.common.service.IBaseService;

import java.util.List;

public interface ITableDefinedService extends IBaseService<Integer,TableDefined> {

	void updateSqlTextForAddColumn(TableDefined table,TableColumn... temp);
	
	void updateSqlTextForDeleteColumn(TableDefined table,TableColumn... temp);
	
	List<TableDefined> listAllDataTable();

}
