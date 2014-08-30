package com.calm.cms.vo;

import com.calm.cms.api.entity.Relation;

public class TableColumnVo {
	private Integer columnId;
	private String columnName;
	private String defaultValue;
	private Relation relation;
	private String relationColumn;
	private Boolean required;

	public Integer getColumnId() {
		return columnId;
	}

	public void setColumnId(Integer columnId) {
		this.columnId = columnId;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Relation getRelation() {
		return relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	public String getRelationColumn() {
		return relationColumn;
	}

	public void setRelationColumn(String relationColumn) {
		this.relationColumn = relationColumn;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}
	
}
