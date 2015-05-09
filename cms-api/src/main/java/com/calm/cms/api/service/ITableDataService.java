package com.calm.cms.api.service;

import java.util.List;
import java.util.Map;

import com.calm.cms.api.dao.QueryMapper;
import com.calm.cms.api.entity.BaseColumnData;
import com.calm.cms.api.entity.BaseColumnDataKey;
import com.calm.cms.api.entity.TableDefined;
import com.calm.framework.common.entity.Paging;
import com.calm.framework.common.service.IBaseService;

public interface ITableDataService extends IBaseService<BaseColumnDataKey,BaseColumnData> {
	public List<BaseColumnData> listAll(Integer tableId);

	public List<BaseColumnData> list(Integer tableId, QueryMapper queryMapper);
	public Paging<BaseColumnData> paging(Integer currentPage, Integer pageSize, Integer tableId);

	public void add(TableDefined loadById, Map<String, String> data);

	public void update(TableDefined loadById, Integer rowId,
			Map<String, String> data);
}
