package com.calm.cms.api.service;

import java.util.List;

import com.calm.cms.api.dao.QueryMapper;
import com.calm.cms.api.entity.BaseColumnData;
import com.calm.cms.api.entity.ColumnData;
import com.calm.cms.api.entity.ColumnDataKey;
import com.calm.framework.common.entity.Paging;
import com.calm.framework.common.service.IBaseService;

public interface IColumnDataService extends IBaseService<ColumnDataKey,ColumnData> {
	public List<BaseColumnData> listAll(Integer tableId);

	public List<BaseColumnData> list(Integer tableId, QueryMapper queryMapper);
	public Paging<ColumnData> paging(Integer currentPage, Integer pageSize, Integer tableId);
}
