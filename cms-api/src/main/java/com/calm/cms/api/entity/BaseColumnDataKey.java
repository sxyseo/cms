package com.calm.cms.api.entity;

import java.io.Serializable;

public class BaseColumnDataKey implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer tableId;
	public BaseColumnDataKey() {
	}
	
	public BaseColumnDataKey(Integer id, Integer tableId) {
		super();
		this.id = id;
		this.tableId = tableId;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTableId() {
		return tableId;
	}
	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}
	
}
