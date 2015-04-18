package com.calm.cms.impl.service;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Service;

import com.calm.cms.api.dao.IColumnDataDao;
import com.calm.cms.api.dao.QueryMapper;
import com.calm.cms.api.entity.BaseColumnData;
import com.calm.cms.api.entity.ColumnData;
import com.calm.cms.api.entity.ColumnDataKey;
import com.calm.cms.api.entity.TableDefined;
import com.calm.cms.api.service.IColumnDataService;
import com.calm.cms.api.service.ITableDefinedService;
import com.calm.framework.common.entity.DefaultPaging;
import com.calm.framework.common.entity.Paging;
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

	@SuppressWarnings("unchecked")
	public Paging<ColumnData> paging(Integer currentPage, Integer pageSize,
			Integer tableId) {
		TableDefined loadById = tableDefinedService.loadById(tableId);
		DefaultPaging<ColumnData> defaultPaging = new DefaultPaging<ColumnData>();
		if (loadById == null) {
			defaultPaging.setTotalCount(0);
			defaultPaging.setData(Collections.<ColumnData>emptyList());
			return defaultPaging;
		}
		SQLQuery createSQLQuery = columnDataDao.createSQLQuery("select count(1) from ("+loadById.getSqlText()+") c");
		BigInteger count = (BigInteger) createSQLQuery.uniqueResult();
		defaultPaging.setTotalCount(count.intValue());
		if(count.intValue()<=0){
			defaultPaging.setData(Collections.<ColumnData>emptyList());
			return defaultPaging;
		}
		
		createSQLQuery = columnDataDao.createSQLQuery("select * from ("+loadById.getSqlText()+") c LIMIT ?,?");
		createSQLQuery.setInteger(0, (currentPage-1)*pageSize);
		createSQLQuery.setInteger(1, pageSize);
		createSQLQuery.setResultTransformer(entityMapResultTransformer);
		List<ColumnData> list = createSQLQuery.list();
		defaultPaging.setData(list);
		return defaultPaging;
	}
	@SuppressWarnings("unchecked")
	public List<BaseColumnData> listAll(Integer tableId) {
		TableDefined loadById = tableDefinedService.loadById(tableId);
		if (loadById == null) {
			return Collections.emptyList();
		}
		SQLQuery createSQLQuery = columnDataDao.createSQLQuery(loadById.getSqlText());
		createSQLQuery.setResultTransformer(entityMapResultTransformer);
		return createSQLQuery.list();
	}

	@Override
	public Class<ColumnData> getEntityClass() {
		return ColumnData.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BaseColumnData> list(Integer tableId, QueryMapper mapper) {
		SQLQuery createSQLQuery = columnDataDao.createSQLQuery(mapper.getSql());
		mapper.setParameter(createSQLQuery);
		createSQLQuery.setResultTransformer(entityMapResultTransformer);
		return createSQLQuery.list();
		
//		sql.append("SELECT TABLE_.* from (");
//		sql.append(loadById.getSqlText());
//		sql.append(" ) table_ WHERE 1=1 ");
//		for (Map.Entry<String, Object> e : parameter.entrySet()) {
//			sql.append(" AND table_.");
//			sql.append(e.getKey());
//			sql.append("=");
//			Object value = e.getValue();
//			if (value instanceof Number) {
//				sql.append(value);
//			} else {
//				sql.append("'");
//				sql.append(value);
//				sql.append("'");
//			}
//		}
//		return listSql(sql.toString());
	}

//	private List<?> listSql(String sql) {
//		SQLQuery createSQLQuery = columnDataDao.createSQLQuery(sql);
//		createSQLQuery.setResultTransformer(entityMapResultTransformer);
//		return createSQLQuery.list();
//	}
}
