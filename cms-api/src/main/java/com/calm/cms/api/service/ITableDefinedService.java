package com.calm.cms.api.service;

import java.util.List;

import com.calm.cms.api.entity.TableDefined;
import com.calm.framework.common.service.IBaseService;

public interface ITableDefinedService extends IBaseService<Integer,TableDefined> {

	void updateSqlText(TableDefined table);

	List<TableDefined> listAllDataTable();

}
