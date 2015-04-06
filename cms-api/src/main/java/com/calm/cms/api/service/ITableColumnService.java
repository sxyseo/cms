package com.calm.cms.api.service;

import org.springframework.transaction.annotation.Transactional;

import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.entity.TableColumnKey;
import com.calm.framework.common.service.IBaseService;
@Transactional
public interface ITableColumnService extends IBaseService<TableColumnKey,TableColumn> {

	/**
	 * 对数据项目排序
	 * @param id
	 * @param tableId
	 * @param up
	 */
	void order(String id, Integer tableId, boolean up);

}
