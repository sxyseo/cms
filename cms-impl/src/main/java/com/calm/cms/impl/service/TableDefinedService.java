package com.calm.cms.impl.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.entity.TableDefined;
import com.calm.cms.api.entity.TableType;
import com.calm.cms.api.service.ITableDefinedService;
import com.calm.framework.common.dao.Query;
import com.calm.framework.common.exception.EntityAlreadyExistException;
import com.calm.framework.common.service.impl.BaseService;

@Service
public class TableDefinedService extends BaseService<Integer,TableDefined> implements
		ITableDefinedService {
	@Override
	protected void queryPaging(Query<Integer,TableDefined> query, TableDefined ui) {
		query.eq("tableType", TableType.DATA);
	}

	@Override
	public Class<TableDefined> getEntityClass() {
		return TableDefined.class;
	}

	@Override
	protected void preAdd(TableDefined newEntity) {
		Query<Integer,TableDefined> query = createQuery();
		query.eq("name", newEntity.getName());
		List<TableDefined> list = query.list();
		if (list.size() > 0) {
			throw new EntityAlreadyExistException(newEntity);
		}
	}

	@Override
	protected void preUpdate(TableDefined dbEentity, TableDefined newEntity) {
		dbEentity.setDescription(newEntity.getDescription());
		dbEentity.setName(newEntity.getName());
	}
	
	private void updateSqlText(TableDefined table,TableColumn temp,boolean deleteFlag) {
		TableDefined loadById = loadById(table.getId());
		List<TableColumn> columns = loadById.getColumns();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT CD.ID AS ID_,CD.TABLE_ID AS TABLE_ID_");
		for (TableColumn tct : columns) {
			if(deleteFlag && tct.equals(temp)){
				continue;
			}
//			ColumnDefined cd = tct.getId().getColumnDefined();
			sql.append(",MAX(CASE WHEN CD.COLUMN_ID = '" + tct.getId().getId()
					+ "' THEN CD.VALUE_TEXT ELSE NULL END ) "
					+ tct.getId().getId());
		}
		if(!deleteFlag){
			sql.append(",MAX(CASE WHEN CD.COLUMN_ID = '" + temp.getId().getId()
					+ "' THEN CD.VALUE_TEXT ELSE NULL END ) "
					+ temp.getId().getId());
		}
		sql.append(" FROM COLUMN_DATA CD WHERE CD.TABLE_ID=" + table.getId()
				+ " GROUP BY CD.ID,CD.TABLE_ID");
		loadById.setSqlText(sql.toString());
		baseDao.update(loadById);

	}

	@Override
	public List<TableDefined> listAllDataTable() {
		Query<Integer,TableDefined> query = createQuery();
		query.eq("tableType", TableType.DATA);
		return query.list();
	}

	@Override
	public void updateSqlTextForAddColumn(TableDefined table, TableColumn temp) {
		updateSqlText(table,temp,false);
	}

	@Override
	public void updateSqlTextForDeleteColumn(TableDefined table,
			TableColumn temp) {
		updateSqlText(table,temp,true);
	}

}
