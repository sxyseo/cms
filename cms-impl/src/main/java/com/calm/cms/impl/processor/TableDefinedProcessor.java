package com.calm.cms.impl.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Service;

import com.calm.cms.api.dao.QueryMapper;
import com.calm.cms.api.entity.Relation;
import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.entity.TableDefined;
import com.calm.cms.api.processor.ListableFieldProcessor;
import com.calm.cms.api.service.IColumnDataService;
import com.calm.cms.api.service.ITableDefinedService;

@Service
public class TableDefinedProcessor implements ListableFieldProcessor<Map<String, Object>> {
	private Integer tableId;
	@Resource
	private IColumnDataService columnDataService;
	@Resource
	private ITableDefinedService tdService;

	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

	@Override
	public List<Map<String, Object>> getList(final Integer id, final TableColumn tableColumn) {
		Relation relation = tableColumn.getRelation();
		final TableDefined loadById = tdService.loadById(tableId);
		List<Map<String, Object>> list = null;
		if (relation == Relation.ONE2MANY) {
			list = getListOne2Many(id, loadById);
		} else if (relation == Relation.MANY2MANY) {
			list = getListMany2Many(id, tableColumn, loadById);
		}
		return list;
	}

	private List<Map<String, Object>> getListMany2Many(final Integer id,
			final TableColumn tableColumn, final TableDefined loadById) {
		List<Map<String, Object>> list;
		TableDefined tableDefined = tableColumn.getId().getTableDefined();
		Integer id2 = loadById.getId();
		Integer id3 = tableDefined.getId();
		String name;
		if (id2 > id3) {
			name = loadById.getName() + "_" + tableDefined.getName();
		} else {
			name = tableDefined.getName() + "_" + loadById.getName();
		}
		final TableDefined relTable = tdService
				.loadByProperty("name", name);
		list = columnDataService.list(relTable.getId(), new QueryMapper() {

			@Override
			public String getSql() {
				StringBuilder sql = new StringBuilder();
				sql.append("SELECT TABLE_.* from (");
				sql.append(relTable.getSqlText());
				sql.append(" ) table_ WHERE 1=1 and table_.");
				//// TODO
//					sql.append(tableColumn.getRelationColumn());
				sql.append("=?");
				return sql.toString();
			}

			@Override
			public void setParameter(SQLQuery createSQLQuery) {
				createSQLQuery.setInteger(0, id);
			}

		});
		if (!list.isEmpty()) {
			List<TableColumn> columns = relTable.getColumns();
			List<String> columnStr = new ArrayList<>();
			for (TableColumn tc : columns) {
				columnStr
						.add(tc.getId().getId());
			}

			List<TableColumn> columns2 = loadById.getColumns();
			List<String> columnRel = new ArrayList<>();
			for (TableColumn tc : columns2) {
				String columnName = tc
						.getId().getId();
				if (columnStr.contains(columnName)) {
					columnRel.add(columnName);
				}
			}
			Map<String, Object> parameter = new HashMap<>();
			List<Map<String, Object>> result = new ArrayList<>();
			for (Map<String, Object> row : list) {
				parameter.clear();
				for (String s : columnRel) {
					parameter.put(s, row.get(s));
				}
				Map<String, Object> byId = getById(parameter, loadById);
				if (byId != null) {
					result.add(byId);
				}
			}
			list = result;
		}
		return list;
	}

	private List<Map<String, Object>> getListOne2Many(final Integer id,
			final TableDefined loadById) {
		return columnDataService.list(tableId, new QueryMapper() {

			@Override
			public String getSql() {
				StringBuilder sql = new StringBuilder();
				sql.append("SELECT TABLE_.* from (");
				sql.append(loadById.getSqlText());
				sql.append(" ) table_ WHERE 1=1 and table_.");
				//// TODO
//					sql.append(tableColumn.getRelationColumn());
				sql.append("=?");
				return sql.toString();
			}

			@Override
			public void setParameter(SQLQuery createSQLQuery) {
				createSQLQuery.setInteger(0, id);
			}

		});
	}
	
	public Map<String, Object> getById(final Map<String, Object> parameter,
			final TableDefined loadById) {
		List<Map<String, Object>> list = columnDataService.list(tableId,
				new QueryMapper() {

					@Override
					public String getSql() {
						StringBuilder sql = new StringBuilder();
						sql.append("SELECT TABLE_.* from (");
						sql.append(loadById.getSqlText());
						sql.append(" ) table_ WHERE 1=1");
						for (Map.Entry<String, Object> e : parameter.entrySet()) {
							sql.append(" AND table_.");
							sql.append(e.getKey());
							sql.append("=");
							Object value = e.getValue();
							if (value instanceof Number) {
								sql.append(value);
							} else {
								sql.append("'");
								sql.append(value);
								sql.append("'");
							}
						}
						return sql.toString();
					}

					@Override
					public void setParameter(SQLQuery createSQLQuery) {
					}

				});
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public Map<String, Object> get(Integer rowId, Object value, TableColumn tableColumn) {
		Relation relation = tableColumn.getRelation();
		switch (relation) {
		case ONE2ONE:
			break;
		case ONE2MANY:
			return loadOne2many(rowId, tableColumn).get(0);
		case MANY2MANY:
			return loadMany2many(rowId, tableColumn).get(0);
		case MANY2ONE:
			break;
		default:
			break;
		}
		return null;
	}
	private List<Map<String, Object>> loadOne2many(final Integer id,final TableColumn tableColumn){
		final TableDefined loadById = tdService.loadById(tableId);
		List<Map<String, Object>> 
		list = columnDataService.list(tableId, new QueryMapper() {

			@Override
			public String getSql() {
				StringBuilder sql = new StringBuilder();
				sql.append("SELECT TABLE_.* from (");
				sql.append(loadById.getSqlText());
				sql.append(" ) table_ WHERE 1=1 and table_.");
				// TODO
//				sql.append(tableColumn.getRelationColumn());
				sql.append("=?");
				return sql.toString();
			}

			@Override
			public void setParameter(SQLQuery createSQLQuery) {
				createSQLQuery.setInteger(0, id);
			}

		});
		return list;
	}
	private List<Map<String, Object>> loadMany2many(final Integer id,final TableColumn tableColumn){
		final TableDefined loadById = tdService.loadById(tableId);
		TableDefined tableDefined = tableColumn.getId().getTableDefined();
		Integer id2 = loadById.getId();
		Integer id3 = tableDefined.getId();
		String name;
		if (id2 > id3) {
			name = loadById.getName() + "_" + tableDefined.getName();
		} else {
			name = tableDefined.getName() + "_" + loadById.getName();
		}
		final TableDefined relTable = tdService
				.loadByProperty("name", name);
		List<Map<String, Object>> 
		list = columnDataService.list(relTable.getId(), new QueryMapper() {

			@Override
			public String getSql() {
				StringBuilder sql = new StringBuilder();
				sql.append("SELECT TABLE_.* from (");
				sql.append(relTable.getSqlText());
				sql.append(" ) table_ WHERE 1=1 and table_.");
				//// TODO
//				sql.append(tableColumn.getRelationColumn());
				sql.append("=?");
				return sql.toString();
			}

			@Override
			public void setParameter(SQLQuery createSQLQuery) {
				createSQLQuery.setInteger(0, id);
			}

		});
		if (!list.isEmpty()) {
			List<TableColumn> columns = relTable.getColumns();
			List<String> columnStr = new ArrayList<>();
			for (TableColumn tc : columns) {
				columnStr
						.add(tc.getId().getId());
			}

			List<TableColumn> columns2 = loadById.getColumns();
			List<String> columnRel = new ArrayList<>();
			for (TableColumn tc : columns2) {
				String columnName = tc
						.getId().getId();
				if (columnStr.contains(columnName)) {
					columnRel.add(columnName);
				}
			}
			Map<String, Object> parameter = new HashMap<>();
			List<Map<String, Object>> result = new ArrayList<>();
			for (Map<String, Object> row : list) {
				parameter.clear();
				for (String s : columnRel) {
					parameter.put(s, row.get(s));
				}
				Map<String, Object> byId = getById(parameter, loadById);
				if (byId != null) {
					result.add(byId);
				}
			}
			list = result;
		}
		return list;
	}
}