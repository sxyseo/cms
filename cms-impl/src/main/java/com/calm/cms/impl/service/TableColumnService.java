package com.calm.cms.impl.service;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.calm.cms.api.entity.Relation;
import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.entity.TableColumnKey;
import com.calm.cms.api.service.ITableColumnService;
import com.calm.cms.api.service.ITableDefinedService;
import com.calm.framework.common.service.impl.BaseService;

@Service
public class TableColumnService extends BaseService<TableColumnKey,TableColumn> implements
		ITableColumnService {
	@Resource
	private ITableDefinedService tableDefinedService;
	@Resource
	private ApplicationContext context;

	@Override
	public Class<TableColumn> getEntityClass() {
		return TableColumn.class;
	}
	@Override
	public void add(TableColumn t) {
		super.add(t);
		tableDefinedService.updateSqlTextForAddColumn(t.getId().getTableDefined(),t);
	}
	@Override
	protected void preAdd(TableColumn newEntity) {
		Relation relation = newEntity.getRelation();
		if (relation == null) {
			relation = Relation.ONE2ONE;
			newEntity.setRelation(relation);
		}
	}
	@Override
	public void delete(TableColumn t) {
		super.delete(t);
		tableDefinedService.updateSqlTextForDeleteColumn(t.getId().getTableDefined(),t);
	}
}
