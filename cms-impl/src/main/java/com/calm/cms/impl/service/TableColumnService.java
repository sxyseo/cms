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
	protected void preAdd(TableColumn newEntity) {
		Relation relation = newEntity.getRelation();
		if (relation == null) {
			relation = Relation.ONE2ONE;
			newEntity.setRelation(relation);
		}
	}

	// private void makeRelation(TableColumn t) {
	// TableColumnKey id = t.getId();
	// TableDefined tableDefined = id.getTableDefined();
	// ColumnDefined columnDefined = id.getColumnDefined();
	// String processor = columnDefined.getProcessor();
	// FieldProcessor fieldProcessor = ProcessorUtils.getFieldProcessor(
	// processor, context);
	// if (fieldProcessor instanceof TableDefinedProcessor) {
	// Integer tableId = ((TableDefinedProcessor) fieldProcessor)
	// .getTableId();
	// TableDefined loadById = tableDefinedService.loadById(tableId);
	// TableDefined td = new TableDefined();
	// Integer id2 = tableDefined.getId();
	// if (id2 > tableId) {
	// td.setName(tableDefined.getName() + "_" + loadById.getName());
	// } else {
	// td.setName(loadById.getName() + "_" + tableDefined.getName());
	// }
	// tableDefinedService.add(td);
	// } else {
	//
	// }
	// }
}
