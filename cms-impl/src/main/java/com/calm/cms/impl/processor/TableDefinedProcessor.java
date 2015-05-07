package com.calm.cms.impl.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Service;

import com.calm.cms.Constant;
import com.calm.cms.api.dao.QueryMapper;
import com.calm.cms.api.entity.BaseColumnData;
import com.calm.cms.api.entity.Relation;
import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.entity.TableDefined;
import com.calm.cms.api.processor.ListableFieldProcessor;
import com.calm.cms.api.service.ITableDataService;
import com.calm.cms.api.service.ITableDefinedService;

@Service
public class TableDefinedProcessor implements ListableFieldProcessor<BaseColumnData> {
	private Integer tableId;
	@Resource
	private ITableDataService tableDataService;
	@Resource
	private ITableDefinedService tdService;

	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

	public List<BaseColumnData> getList(final Integer id, final TableColumn tableColumn) {
		return tableDataService.listAll(tableId);
//		final TableDefined tableDefined = tdService.loadById(tableId);
//		List<BaseColumnData> list = null;
//		if (relation == Relation.ONE2MANY) {
//			list = getListOne2Many(tableId, tableDefined);
//		} else if (relation == Relation.MANY2MANY) {
//			list = getListMany2Many(tableId, tableColumn, tableDefined);
//		}
//		return list;
	}

	private List<BaseColumnData> getListMany2Many(final Integer id,
			final TableColumn tableColumn, final TableDefined loadById) {
		List<BaseColumnData> list;
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
		list = tableDataService.list(relTable.getId(), new QueryMapper() {

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
			List<BaseColumnData> result = new ArrayList<>();
			for (BaseColumnData row : list) {
				parameter.clear();
				for (String s : columnRel) {
					parameter.put(s, row.get(s));
				}
				BaseColumnData byId = getById(parameter, loadById);
				if (byId != null) {
					result.add(byId);
				}
			}
			list = result;
		}
		return list;
	}

	private List<BaseColumnData> getListOne2Many(final Integer id,
			final TableDefined loadById) {
		return tableDataService.list(tableId, new QueryMapper() {

			@Override
			public String getSql() {
				StringBuilder sql = new StringBuilder();
				sql.append("SELECT TABLE_.* from (");
				sql.append(loadById.getSqlText());
				sql.append(" ) table_ WHERE 1=1 and table_.");
				// TODO
				sql.append(Constant.ID);
				sql.append("=?");
				return sql.toString();
			}

			@Override
			public void setParameter(SQLQuery createSQLQuery) {
				createSQLQuery.setInteger(0, id);
			}

		});
	}
	
	public BaseColumnData getById(final Map<String, Object> parameter,
			final TableDefined loadById) {
		List<BaseColumnData> list = tableDataService.list(tableId,
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
	public Object get(Integer rowId, Object value, TableColumn tableColumn) {
		Relation relation = tableColumn.getRelation();
		switch (relation) {
		case ONE2ONE:
			break;
		case ONE2MANY:
			return loadOne2many(rowId, tableColumn);
		case MANY2MANY:
			return loadMany2many(rowId, tableColumn).get(0);
		case MANY2ONE:
			return loadMany2One(rowId,value, tableColumn);
		default:
			break;
		}
		return null;
	}
	private BaseColumnData loadMany2One(Integer rowId, final Object value,
			final TableColumn tableColumn) {
		if(value==null){
			return null;
		}
		final TableDefined loadById = tdService.loadById(tableId);
		List<BaseColumnData> list = tableDataService.list(tableId, new QueryMapper() {

			@Override
			public String getSql() {
				StringBuilder sql = new StringBuilder();
				sql.append("SELECT TABLE_.* from (");
				sql.append(loadById.getSqlText());
				sql.append(" ) table_ WHERE 1=1 and table_.");
				// TODO
				sql.append(Constant.ID);
				sql.append("=?");
				return sql.toString();
			}

			@Override
			public void setParameter(SQLQuery createSQLQuery) {
				createSQLQuery.setString(0, value.toString());
			}

		});
		if(list.isEmpty()){
			return null;
		}
		return list.get(0);
	}

	private List<BaseColumnData> loadOne2many(final Integer id,final TableColumn tableColumn){
		final TableDefined loadById = tdService.loadById(tableId);
		List<BaseColumnData> list = tableDataService.list(tableId, new QueryMapper() {

			@Override
			public String getSql() {
				StringBuilder sql = new StringBuilder();
				sql.append("SELECT TABLE_.* from (");
				sql.append(loadById.getSqlText());
				sql.append(" ) table_ WHERE 1=1 and table_.");
				// TODO
				sql.append(tableColumn.getRelationColumn());
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
	private List<BaseColumnData> loadMany2many(final Integer id,final TableColumn tableColumn){
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
		List<BaseColumnData> 
		list = tableDataService.list(relTable.getId(), new QueryMapper() {

			@Override
			public String getSql() {
				StringBuilder sql = new StringBuilder();
				sql.append("SELECT TABLE_.* from (");
				sql.append(relTable.getSqlText());
				sql.append(" ) table_ WHERE 1=1 and table_.");
				//// TODO
				sql.append(tableColumn.getRelationColumn());
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
			List<BaseColumnData> result = new ArrayList<>();
			for (BaseColumnData row : list) {
				parameter.clear();
				for (String s : columnRel) {
					parameter.put(s, row.get(s));
				}
				BaseColumnData byId = getById(parameter, loadById);
				if (byId != null) {
					result.add(byId);
				}
			}
			list = result;
		}
		return list;
	}

	@Override
	public Object getDisplayValue(Object value) {
		return null;
	}

	@Override
	public Class<?> getType() {
		return BaseColumnData.class;
	}
}