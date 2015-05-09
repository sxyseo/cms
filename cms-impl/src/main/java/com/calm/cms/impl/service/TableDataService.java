package com.calm.cms.impl.service;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calm.cms.api.dao.IColumnDataDao;
import com.calm.cms.api.dao.QueryMapper;
import com.calm.cms.api.entity.BaseColumnData;
import com.calm.cms.api.entity.BaseColumnDataKey;
import com.calm.cms.api.entity.ColumnData;
import com.calm.cms.api.entity.ColumnDataKey;
import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.entity.TableDefined;
import com.calm.cms.api.service.IColumnDataService;
import com.calm.cms.api.service.ITableDataService;
import com.calm.cms.api.service.ITableDefinedService;
import com.calm.framework.common.dao.Query;
import com.calm.framework.common.entity.DefaultPaging;
import com.calm.framework.common.entity.Paging;
import com.calm.framework.common.service.impl.BaseService;

@Service
public class TableDataService extends BaseService<BaseColumnDataKey,BaseColumnData> implements
		ITableDataService {
	@Resource(name = "columnDataDao")
	private IColumnDataDao columnDataDao;
	@Resource
	private IColumnDataService columnDataService;
	@Resource
	private ITableDefinedService tableDefinedService;
	@Resource
	private EntityMapResultTransformer entityMapResultTransformer;

	@SuppressWarnings("unchecked")
	public Paging<BaseColumnData> paging(Integer currentPage, Integer pageSize,
			Integer tableId) {
		TableDefined loadById = tableDefinedService.loadById(tableId);
		
		DefaultPaging<BaseColumnData> defaultPaging = new DefaultPaging<BaseColumnData>();
		if (loadById == null) {
			defaultPaging.setTotalCount(0);
			defaultPaging.setData(Collections.<BaseColumnData>emptyList());
			return defaultPaging;
		}
		SQLQuery createSQLQuery = columnDataDao.createSQLQuery("select count(1) from ("+loadById.getSqlText()+") c");
		BigInteger count = (BigInteger) createSQLQuery.uniqueResult();
		defaultPaging.setTotalCount(count.intValue());
		if(count.intValue()<=0){
			defaultPaging.setData(Collections.<BaseColumnData>emptyList());
			return defaultPaging;
		}
		
		createSQLQuery = columnDataDao.createSQLQuery("select * from ("+loadById.getSqlText()+") c LIMIT ?,?");
		createSQLQuery.setInteger(0, (currentPage-1)*pageSize);
		createSQLQuery.setInteger(1, pageSize);
		createSQLQuery.setResultTransformer(entityMapResultTransformer);
		List<BaseColumnData> list = createSQLQuery.list();
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
	public Class<BaseColumnData> getEntityClass() {
		return BaseColumnData.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BaseColumnData> list(Integer tableId, QueryMapper mapper) {
		SQLQuery createSQLQuery = columnDataDao.createSQLQuery(mapper.getSql());
		mapper.setParameter(createSQLQuery);
		createSQLQuery.setResultTransformer(entityMapResultTransformer);
		return createSQLQuery.list();
	}
	
	@Override
	public void delete(BaseColumnDataKey i) {
		Query<ColumnDataKey, ColumnData> query = columnDataService.createQuery();
		query.eq("id.id", i.getId()).eq("id.tableColumn.id.tableDefined.id", i.getTableId());
		List<ColumnData> list = query.list();
		for(ColumnData cd:list){
			columnDataService.delete(cd);
		}
	}
	@Override
	@Transactional
	public void add(TableDefined tableDefined, Map<String, String> data) {
		Query<ColumnDataKey, ColumnData> query = columnDataService.createQuery();
		query.eq("id.tableColumn.id.tableDefined.id", tableDefined.getId());
		query.max("id.id");
		Integer rowId = query.load(Integer.class);
		Integer rowId2 = tableDefined.getRowId();
		int id=0;
		if(rowId!=null){
			id = rowId;
		}
		if(rowId2!=null && id<rowId2){
			id = rowId2;
		}
		id++;
		tableDefined.setRowId(id);
		for(Map.Entry<String, String >e :data.entrySet()){
			
			ColumnDataKey key=new ColumnDataKey();
			key.setId(id);
			key.setTableColumn(new TableColumn(tableDefined, e.getKey()));
			
			ColumnData loadById = columnDataService.loadById(key);
			if(loadById==null){
				ColumnData ui = new ColumnData();
				ui.setId(key);
				ui.setValueText(e.getValue());
				columnDataService.add(ui);
			}else{
				loadById.setValueText(e.getValue());
				columnDataService.update(loadById);				
			}
		}
	}
	@Override
	public void update(TableDefined tableDefined, Integer rowId,
			Map<String, String> data) {
		for(Map.Entry<String, String >e :data.entrySet()){
			
			ColumnDataKey key=new ColumnDataKey();
			key.setId(rowId);
			key.setTableColumn(new TableColumn(tableDefined, e.getKey()));
			
			ColumnData loadById = columnDataService.loadById(key);
			if(loadById==null){
				ColumnData ui = new ColumnData();
				ui.setId(key);
				ui.setValueText(e.getValue());
				columnDataService.add(ui);
			}else{
				loadById.setValueText(e.getValue());
				columnDataService.update(loadById);				
			}
		}
	}
}
