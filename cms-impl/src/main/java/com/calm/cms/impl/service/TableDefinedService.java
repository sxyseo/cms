package com.calm.cms.impl.service;

import java.util.ArrayList;
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
	public void add(TableDefined newEntity) {
		super.add(newEntity);
		//数据表需要添加为处理类型
		if(newEntity.getTableType()==TableType.DATA){
			FieldType ft=new FieldType();
			ft.setDeleteClass(false);
			ft.setName(newEntity.getName());
			ft.setProcessId("tableDefinedProcessor");
			ft.setTableDefinedId(newEntity.getId());
			ft.setType(ProcessorType.TABLE);
			fieldTypeService.add(ft);
		}
	}
	@Override
	protected void preAdd(TableDefined newEntity) {
		//判断名称重复
		Query<Integer,TableDefined> query = createQuery();
		query.eq("name", newEntity.getName());
		List<TableDefined> list = query.list();
		if (list.size() > 0) {
			throw new EntityAlreadyExistException(newEntity);
		}
	}
	@Override
	protected void preDelete(TableDefined dbEentity, TableDefined newEntity) {
		//判断表中定义有数据项目
		List<TableColumn> listByProperty = tableColumnService.listByProperty("id.tableDefined", dbEentity);
		
		if (listByProperty.size() > 0) {
			throw new FrameworkExceptioin("CMS_E_00002");
		}
		//删除类型处理
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
	
	private void updateSqlText(TableDefined table,boolean deleteFlag,TableColumn... temp) {
		TableDefined loadById = loadById(table.getId());
		List<TableColumn> columns = loadById.getColumns();
		if(columns==null){
			columns = new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT CD.ID AS ID_,CD.TABLE_ID AS TABLE_ID_");
		for(TableColumn t:temp){
			if(!deleteFlag){
				if(columns.contains(t)){
					continue;
				}else{
					columns.add(t);
				}
			}
		}
		b:for (TableColumn tct : columns) {
			if(deleteFlag){
				for(TableColumn tc:temp){
					if(tct.equals(tc)){
						continue b;
					}
				}
			}
			sql.append(",MAX(CASE WHEN CD.COLUMN_ID = '" + tct.getId().getId()
					+ "' THEN CD.VALUE_TEXT ELSE NULL END ) "
					+ tct.getId().getId());
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
	public void updateSqlTextForAddColumn(TableDefined table, TableColumn... temp) {
		updateSqlText(table, false, temp);
	}

	@Override
	public void updateSqlTextForDeleteColumn(TableDefined table,
			TableColumn... temp) {
		updateSqlText(table, true, temp);
	}

}
