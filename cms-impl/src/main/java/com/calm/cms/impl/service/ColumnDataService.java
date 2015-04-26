package com.calm.cms.impl.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.calm.cms.api.dao.IColumnDataDao;
import com.calm.cms.api.entity.ColumnData;
import com.calm.cms.api.entity.ColumnDataKey;
import com.calm.cms.api.service.IColumnDataService;
import com.calm.cms.api.service.ITableDefinedService;
import com.calm.framework.common.service.impl.BaseService;

@Service
public class ColumnDataService extends BaseService<ColumnDataKey,ColumnData> implements
		IColumnDataService {
	@Resource(name = "columnDataDao")
	private IColumnDataDao columnDataDao;
	@Resource
	private ITableDefinedService tableDefinedService;
	@Resource
	private EntityMapResultTransformer entityMapResultTransformer;

//	@SuppressWarnings("unchecked")
//	public Paging<BaseColumnData> paging(Integer currentPage, Integer pageSize,
//			Integer tableId) {
//		TableDefined loadById = tableDefinedService.loadById(tableId);
//		DefaultPaging<BaseColumnData> defaultPaging = new DefaultPaging<BaseColumnData>();
//		if (loadById == null) {
//			defaultPaging.setTotalCount(0);
//			defaultPaging.setData(Collections.<BaseColumnData>emptyList());
//			return defaultPaging;
//		}
//		SQLQuery createSQLQuery = columnDataDao.createSQLQuery("select count(1) from ("+loadById.getSqlText()+") c");
//		BigInteger count = (BigInteger) createSQLQuery.uniqueResult();
//		defaultPaging.setTotalCount(count.intValue());
//		if(count.intValue()<=0){
//			defaultPaging.setData(Collections.<BaseColumnData>emptyList());
//			return defaultPaging;
//		}
//		
//		createSQLQuery = columnDataDao.createSQLQuery("select * from ("+loadById.getSqlText()+") c LIMIT ?,?");
//		createSQLQuery.setInteger(0, (currentPage-1)*pageSize);
//		createSQLQuery.setInteger(1, pageSize);
//		createSQLQuery.setResultTransformer(entityMapResultTransformer);
//		List<BaseColumnData> list = createSQLQuery.list();
//		defaultPaging.setData(list);
//		return defaultPaging;
//	}
//	@SuppressWarnings("unchecked")
//	public List<BaseColumnData> listAll(Integer tableId) {
//		TableDefined loadById = tableDefinedService.loadById(tableId);
//		if (loadById == null) {
//			return Collections.emptyList();
//		}
//		SQLQuery createSQLQuery = columnDataDao.createSQLQuery(loadById.getSqlText());
//		createSQLQuery.setResultTransformer(entityMapResultTransformer);
//		return createSQLQuery.list();
//	}

	@Override
	public Class<ColumnData> getEntityClass() {
		return ColumnData.class;
	}
}
