package com.calm.cms.api.service;

import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.entity.TableColumnKey;
import com.calm.framework.common.service.IBaseService;

public interface ITableColumnService extends IBaseService<TableColumnKey,TableColumn> {

	void order(String id, Integer tableId, boolean up);

}
