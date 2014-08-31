package com.calm.cms.api.service;

import com.calm.cms.api.dao.QueryMapper;
import com.calm.cms.api.entity.ColumnData;
import com.calm.cms.api.entity.ColumnDataKey;
import com.calm.framework.common.service.IBaseService;

import java.util.List;
import java.util.Map;

public interface IColumnDataService extends IBaseService<ColumnDataKey,ColumnData> {
	public List<Map<String,Object>> listAll(Integer tableId);

	public List<Map<String,Object>> list(Integer tableId, QueryMapper queryMapper);
}
