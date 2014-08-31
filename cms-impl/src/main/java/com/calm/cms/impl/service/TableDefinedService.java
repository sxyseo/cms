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

	@Override
	public void updateSqlText(TableDefined table) {
		TableDefined loadById = loadById(table.getId());
		List<TableColumn> columns = loadById.getColumns();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT CD.ID,CD.TABLE_ID ");
		for (TableColumn tct : columns) {
//			ColumnDefined cd = tct.getId().getColumnDefined();
			sql.append(",MAX(CASE WHEN CD.COLUMN_ID = " + tct.getId().getId()
					+ " THEN CD.VALUE_TEXT ELSE NULL END ) "
					+ tct.getId().getId());
		}

		sql.append(" FROM COLUMN_DATA CD WHERE CD.TABLE_ID=" + table.getId()
				+ " GROUP BY CD.ID,CD.TABLE_ID");
		loadById.setSqlText(sql.toString());
		update(loadById);

	}

	@Override
	public List<TableDefined> listAllDataTable() {
		Query<Integer,TableDefined> query = createQuery();
		query.eq("tableType", TableType.DATA);
		return query.list();
	}

}
