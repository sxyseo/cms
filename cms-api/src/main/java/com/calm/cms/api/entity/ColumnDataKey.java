package com.calm.cms.api.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

@Embeddable
public class ColumnDataKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "TABLE_ID",referencedColumnName="TABLE_ID"),
			@JoinColumn(name = "COLUMN_ID",referencedColumnName="COLUMN_ID") })
	private TableColumn tableColumn;

	public TableColumn getTableColumn() {
		return tableColumn;
	}

	public void setTableColumn(TableColumn tableColumn) {
		this.tableColumn = tableColumn;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
