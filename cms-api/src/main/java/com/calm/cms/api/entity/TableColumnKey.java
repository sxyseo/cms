package com.calm.cms.api.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.calm.framework.annotation.Json;

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
	@Json(serialize=false)
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((tableDefined == null) ? 0 : tableDefined.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableColumnKey other = (TableColumnKey) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (tableDefined == null) {
			if (other.tableDefined != null)
				return false;
		} else if (!tableDefined.equals(other.tableDefined))
			return false;
		return true;
	}

//	public ColumnDefined getColumnDefined() {
//		return columnDefined;
//	}
//
//	public void setColumnDefined(ColumnDefined columnDefined) {
//		this.columnDefined = columnDefined;
//	}
	
}
