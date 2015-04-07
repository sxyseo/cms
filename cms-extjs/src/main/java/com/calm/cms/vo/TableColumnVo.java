package com.calm.cms.vo;

import com.calm.cms.api.entity.ProcessorType;


public class TableColumnVo {
	private Integer tableId;
	private String columnName;
	private String name;
	private String defaultValue;
	private String relation;
	private String relationName;
	private Boolean required;
	private Boolean primaryKey;
	private Integer processorId;
	private String processorName;
	private ProcessorType processorType;
	public TableColumnVo() {
	}
	public TableColumnVo(String columnName, Integer tableId) {
		super();
		this.columnName = columnName;
		this.tableId = tableId;
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

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public Integer getProcessorId() {
		return processorId;
	}

	public void setProcessorId(Integer processorId) {
		this.processorId = processorId;
	}

	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getProcessorName() {
		return processorName;
	}
	public void setProcessorName(String processorName) {
		this.processorName = processorName;
	}
	public String getRelationName() {
		return relationName;
	}
	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}
	public ProcessorType getProcessorType() {
		return processorType;
	}
	public void setProcessorType(ProcessorType processorType) {
		this.processorType = processorType;
	}
	public Boolean getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(Boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
	
}
