package com.calm.cms.impl.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.calm.cms.api.entity.Relation;
import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.entity.TableColumnKey;
import com.calm.cms.api.service.ITableColumnService;
import com.calm.cms.api.service.ITableDefinedService;
import com.calm.framework.common.dao.Query;
import com.calm.framework.common.service.impl.BaseService;

@Service
public class TableColumnService extends BaseService<TableColumnKey, TableColumn> implements ITableColumnService {
	@Resource
	private ITableDefinedService tableDefinedService;
	@Resource
	private ApplicationContext context;

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
		com.calm.framework.util.BeanUtils.copyProperties(newEntity, dbEentity,"id","orderIndex");
	}
	
	@Override
	public void add(TableColumn t) {
		super.add(t);
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
			break;
		default:
			break;
		}
	}

	@Override
	public void delete(TableColumn t) {
		super.delete(t);
		tableDefinedService.updateSqlTextForDeleteColumn(t.getId()
				.getTableDefined(), t);
	}
}
