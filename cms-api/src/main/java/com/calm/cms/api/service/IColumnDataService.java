package com.calm.cms.api.service;

import java.util.List;
import java.util.Map;

import com.calm.cms.api.dao.QueryMapper;
import com.calm.cms.api.entity.ColumnData;
import com.calm.framework.common.service.IBaseService;

public interface IColumnDataService extends IBaseService<ColumnData> {
	public List<Map<String,Object>> listAll(Integer tableId);

	public List<Map<String,Object>> list(Integer tableId, QueryMapper queryMapper);
}
