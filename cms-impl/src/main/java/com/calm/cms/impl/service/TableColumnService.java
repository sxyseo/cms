package com.calm.cms.impl.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.calm.cms.api.entity.FieldType;
import com.calm.cms.api.entity.Relation;
import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.entity.TableColumnKey;
import com.calm.cms.api.entity.TableDefined;
import com.calm.cms.api.entity.TableType;
import com.calm.cms.api.service.IFieldTypeService;
import com.calm.cms.api.service.ITableColumnService;
import com.calm.cms.api.service.ITableDefinedService;
import com.calm.framework.common.dao.Query;
import com.calm.framework.common.service.impl.BaseService;

@Service
public class TableColumnService extends
		BaseService<TableColumnKey, TableColumn> implements ITableColumnService {
	@Resource
	private ITableDefinedService tableDefinedService;
	@Resource
	private IFieldTypeService fieldTypeService;

	@Override
	protected void queryPaging(Query<TableColumnKey, TableColumn> query,
			TableColumn ui) {
		query.asc("orderIndex");
	}

	@Override
	public Class<TableColumn> getEntityClass() {
		return TableColumn.class;
	}

	@Override
	protected void preUpdate(TableColumn dbEentity, TableColumn newEntity) {
		com.calm.framework.util.BeanUtils.copyProperties(newEntity, dbEentity,
				"id", "orderIndex","relationTableDefined");
		if(dbEentity.getRelation() !=newEntity.getRelation()){
			if(dbEentity.getRelation()==Relation.MANY2MANY){
				removeMany2ManyRelation(dbEentity);
			}
			if(newEntity.getRelation()==Relation.MANY2MANY){
				addMany2ManyRelation(newEntity);
			}
			
		}
	}

	@Override
	public void add(TableColumn t) {
		super.add(t);
		//更新对应表中的数据查询sql
		tableDefinedService.updateSqlTextForAddColumn(t.getId()
				.getTableDefined(), t);

	}
	@Override
	protected void preAdd(TableColumn newEntity) {
		
		Query<TableColumnKey, TableColumn> query = createQuery();
		query.eq("id.tableDefined", newEntity.getId().getTableDefined());
		query.desc("orderIndex");
		List<TableColumn> list = query.list();
		int order = 0;
		if (list.isEmpty()) {
			order = 0;
		} else {
			TableColumn column = list.get(0);
			Integer orderIndex = column.getOrderIndex();
			if (orderIndex == null) {
				orderIndex = 0;
			} else {
				order = column.getOrderIndex();
			}
		}
		order++;
		newEntity.setOrderIndex(order);

		Relation relation = newEntity.getRelation();
		if (relation == null) {
			relation = Relation.ONE2ONE;
			newEntity.setRelation(relation);
		}
		switch (relation) {
		case ONE2ONE:
			break;
		case ONE2MANY:
			break;
		case MANY2MANY:
			addMany2ManyRelation(newEntity);
			break;
		default:
			break;
		}
	}

	/**
	 * 处理多对多关联关系
	 * @param newEntity
	 */
	private void addMany2ManyRelation(TableColumn newEntity) {
		FieldType processor = newEntity.getProcessor();
		processor = fieldTypeService.loadById(processor.getId());
		//获得关联的表
		Integer relationDefinedId = processor.getTableDefinedId();
		//获得当前列的表ID
		Integer tableId = newEntity.getId().getTableDefined().getId();
		//查询当前表对应的处理器
		FieldType loadByProperty = fieldTypeService.loadByProperty("tableDefinedId", tableId);
		//判断与当前列关联的表是否已经建立了多对多关联
		Query<TableColumnKey, TableColumn> createQuery = createQuery();

		createQuery.eq("processor", loadByProperty).eq("id.tableDefined.id",relationDefinedId)
			.eq("relation", Relation.MANY2MANY);
		
		TableColumn re = createQuery.load();
		//如果没有关联，创建关联关系
		if (re == null) {
			String name;
			TableDefined table = tableDefinedService.loadById(tableId);
			TableDefined relationDefined = tableDefinedService.loadById(relationDefinedId);
			if (tableId > relationDefinedId) {
				name = table.getName() + "-" + relationDefined.getName();
			} else {
				name = relationDefined.getName() + "-" + table.getName();
			}
			//创建关系表
			TableDefined rel = new TableDefined();
			rel.setName(name);
			rel.setTableType(TableType.RELATION);
			tableDefinedService.add(rel);
			//绑定关联表
			newEntity.setRelationTableDefined(rel);
			//创建关联列
			TableColumn col1 = new TableColumn(rel, "RELATION" + tableId);
			col1.setRelation(Relation.MANY2ONE);
			loadByProperty = fieldTypeService.loadByProperty("tableDefinedId",	tableId);
			col1.setProcessor(loadByProperty);
			baseDao.insert(col1);
			//创建关联列
			TableColumn col2 = new TableColumn(rel, "RELATION"	+ relationDefinedId);
			col2.setRelation(Relation.MANY2ONE);
			loadByProperty = fieldTypeService.loadByProperty("tableDefinedId",
					relationDefinedId);
			col2.setProcessor(loadByProperty);
			baseDao.insert(col2);
			//更新关联表中的数据查询sql
			tableDefinedService.updateSqlTextForAddColumn(rel, col1, col2);
		} else {
			//已经创建关系的，绑定关系
			newEntity.setRelationTableDefined(re.getRelationTableDefined());
		}

	}
	/**
	 * 删除多对多关联关系
	 * @param dbEentity
	 */
	private void removeMany2ManyRelation(TableColumn dbEentity) {
		FieldType processor = dbEentity.getProcessor();
		processor = fieldTypeService.loadById(processor.getId());
		//获得关联的表
		Integer relationDefinedId = processor.getTableDefinedId();
		//获得当前列的表ID
		Integer tableId = dbEentity.getId().getTableDefined().getId();
		//查询当前表对应的处理器
		FieldType loadByProperty = fieldTypeService.loadByProperty("tableDefinedId", tableId);
		//判断与当前列关联的表是否已经建立了多对多关联
		Query<TableColumnKey, TableColumn> createQuery = createQuery();

		createQuery.eq("processor", loadByProperty).eq("id.tableDefined.id",relationDefinedId)
			.eq("relation", Relation.MANY2MANY);
		
		TableColumn re = createQuery.load();
		//如果关系列存在，不做处理 ，如果已经不存在
		if (re == null) {
			TableDefined relationTableDefined = dbEentity.getRelationTableDefined();
			//删除关系表中的列
			List<TableColumn> columns = relationTableDefined.getColumns();
			for(TableColumn tc:columns){
				baseDao.delete(tc);
			}
			//删除关系表
			
			tableDefinedService.delete(relationTableDefined);
			
		}
		//清除关联关系
		dbEentity.setRelationTableDefined(null);
	}

	@Override
	public void delete(TableColumn t) {
		super.delete(t);
		//删除数据查询sql
		tableDefinedService.updateSqlTextForDeleteColumn(t.getId()
				.getTableDefined(), t);
		
	}
	@Override
	protected void preDelete(TableColumn dbEentity, TableColumn newEntity) {
		//删除多对多关系
		if(dbEentity.getRelation()==Relation.MANY2MANY){
			removeMany2ManyRelation(dbEentity);
		}
		
	}

	/* (non-Javadoc)
	 * @see com.calm.cms.api.service.ITableColumnService#order(java.lang.String, java.lang.Integer, boolean)
	 */
	@Override
	public void order(String id, Integer tableId, boolean up) {
		TableColumn loadById = loadById(new TableColumnKey(new TableDefined(
				tableId), id));
		Integer orderIndex = loadById.getOrderIndex();
		TableColumn next;
		if (up) {
			Query<TableColumnKey, TableColumn> createQuery = createQuery();
			createQuery.eq("id.tableDefined.id", tableId)
					.lt("orderIndex", orderIndex).desc("orderIndex");
			List<TableColumn> list = createQuery.list();
			if (list.isEmpty()) {
				return;
			}
			next = list.get(0);
			next.setOrderIndex(next.getOrderIndex() + 1);
			orderIndex--;

		} else {
			Query<TableColumnKey, TableColumn> createQuery = createQuery();
			createQuery.eq("id.tableDefined.id", tableId)
					.gt("orderIndex", orderIndex).asc("orderIndex");
			List<TableColumn> list = createQuery.list();
			if (list.isEmpty()) {
				return;
			}
			next = list.get(0);
			next.setOrderIndex(next.getOrderIndex() - 1);
			orderIndex++;
		}
		loadById.setOrderIndex(orderIndex);
		baseDao.update(loadById);
		baseDao.update(next);

	}
}
