package com.calm.cms.impl.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.calm.cms.api.entity.FieldType;
import com.calm.cms.api.entity.ProcessorType;
import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.entity.TableDefined;
import com.calm.cms.api.entity.TableType;
import com.calm.cms.api.service.IFieldTypeService;
import com.calm.cms.api.service.ITableColumnService;
import com.calm.cms.api.service.ITableDefinedService;
import com.calm.framework.common.dao.Query;
import com.calm.framework.common.exception.EntityAlreadyExistException;
import com.calm.framework.common.exception.FrameworkExceptioin;
import com.calm.framework.common.service.impl.BaseService;

@Service
public class TableDefinedService extends BaseService<Integer,TableDefined> implements
		ITableDefinedService {
	@Resource
	private IFieldTypeService fieldTypeService;
	
	@Resource
	private ITableColumnService tableColumnService;
	
	@Override
	protected void queryPaging(Query<Integer,TableDefined> query, TableDefined ui) {
		query.eq("tableType", TableType.DATA);
	}

	@Override
	public Class<TableDefined> getEntityClass() {
		return TableDefined.class;
	}
	@Override
	public void add(TableDefined t) {
		super.add(t);
		FieldType ft=new FieldType();
		ft.setDeleteClass(false);
		ft.setName(t.getName());
		ft.setProcessId("tableDefinedProcessor");
		ft.setTableDefinedId(t.getId());
		ft.setType(ProcessorType.TABLE);
		fieldTypeService.add(ft);
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
	protected void preDelete(TableDefined dbEentity, TableDefined newEntity) {
		
		List<TableColumn> listByProperty = tableColumnService.listByProperty("id.tableDefined", dbEentity);
		
		if (listByProperty.size() > 0) {
			throw new FrameworkExceptioin("CMS_E_00002");
		}
		
		Query<Integer, FieldType> query = fieldTypeService.createQuery();
		query.eq("processId", "tableDefinedProcessor");
		query.eq("tableDefinedId", dbEentity.getId());
		FieldType load = query.load();
		if (load != null) {
			fieldTypeService.delete(load);
		}
	}
	@Override
	protected void preUpdate(TableDefined dbEentity, TableDefined newEntity) {
		dbEentity.setDescription(newEntity.getDescription());
		dbEentity.setName(newEntity.getName());
		
		Query<Integer, FieldType> query = fieldTypeService.createQuery();
		query.eq("processId", "tableDefinedProcessor");
		query.eq("tableDefinedId", dbEentity.getId());
		FieldType load = query.load();
		if (load != null) {
			load.setName(newEntity.getName());
			fieldTypeService.update(load);
		}
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
		Query<Integer, TableDefined> query = createQuery();
		query.eq("tableType", TableType.DATA);
		return query.list();
	}

	@Override
	public void updateSqlTextForAddColumn(TableDefined table, TableColumn temp) {
		updateSqlText(table, temp, false);
	}

	@Override
	public void updateSqlTextForDeleteColumn(TableDefined table,
			TableColumn temp) {
		updateSqlText(table, temp, true);
	}

}
