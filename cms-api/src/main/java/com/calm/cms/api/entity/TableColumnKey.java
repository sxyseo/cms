package com.calm.cms.api.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class TableColumnKey implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ManyToOne
	@JoinColumn(name = "TABLE_ID")
	private TableDefined tableDefined;
	@ManyToOne
	@JoinColumn(name = "COLUMN_ID")
	private ColumnDefined columnDefined;

	public TableColumnKey() {
	}
	
	public TableColumnKey(TableDefined tableDefined, ColumnDefined columnDefined) {
		super();
		this.tableDefined = tableDefined;
		this.columnDefined = columnDefined;
	}

	public TableDefined getTableDefined() {
		return tableDefined;
	}

	public void setTableDefined(TableDefined tableDefined) {
		this.tableDefined = tableDefined;
	}

	public ColumnDefined getColumnDefined() {
		return columnDefined;
	}

	public void setColumnDefined(ColumnDefined columnDefined) {
		this.columnDefined = columnDefined;
	}

}
