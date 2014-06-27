package com.calm.cms.api.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.calm.framework.common.entity.BaseEntity;

@Entity
@Table(name = "TABLE_COLUMN")
public class TableColumn implements BaseEntity {
	/**
	 * 
	 */
	@EmbeddedId
	private TableColumnKey id;
	@Column(name = "DEFAULT_VALUE")
	private String defaultValue;
	@Column(name = "RELATION")
	private Relation relation;
	@Column(name = "RELATION_COLUMN")
	private String relationColumn;
	public TableColumn() {
	}

	public TableColumn(TableDefined tableDefined, ColumnDefined columnDefined) {
		this.id = new TableColumnKey(tableDefined, columnDefined);
	}

	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public Object getDisplayValue() {
		return null;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public TableColumnKey getId() {
		return id;
	}

	public void setId(TableColumnKey id) {
		this.id = id;
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

}
