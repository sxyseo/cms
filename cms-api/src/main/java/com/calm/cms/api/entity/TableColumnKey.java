package com.calm.cms.api.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class TableColumnKey implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name="ID")
	private String id;
	@ManyToOne
	@JoinColumn(name = "TABLE_ID")
	private TableDefined tableDefined;
//	@ManyToOne
//	@JoinColumn(name = "COLUMN_ID")
//	private ColumnDefined columnDefined;

	public TableColumnKey() {
	}
	
	public TableColumnKey(TableDefined tableDefined, String id) {
		super();
		this.tableDefined = tableDefined;
		this.id = id;
	}
	
	public TableDefined getTableDefined() {
		return tableDefined;
	}

	public void setTableDefined(TableDefined tableDefined) {
		this.tableDefined = tableDefined;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

//	public ColumnDefined getColumnDefined() {
//		return columnDefined;
//	}
//
//	public void setColumnDefined(ColumnDefined columnDefined) {
//		this.columnDefined = columnDefined;
//	}

}
