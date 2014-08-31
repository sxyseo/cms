package com.calm.cms.api.service;

import com.calm.cms.api.entity.TableDefined;
import com.calm.framework.common.service.IBaseService;

import java.util.List;

public interface ITableDefinedService extends IBaseService<Integer,TableDefined> {

	void updateSqlText(TableDefined table);

	List<TableDefined> listAllDataTable();

}
