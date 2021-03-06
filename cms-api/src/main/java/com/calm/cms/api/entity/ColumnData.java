package com.calm.cms.api.entity;

import com.calm.framework.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "COLUMN_DATA")
public class ColumnData implements BaseEntity<ColumnDataKey> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private ColumnDataKey id;
	@Column(name = "VALUE_TEXT")
	private String valueText;

	public ColumnDataKey getId() {
		return id;
	}

	public void setId(ColumnDataKey id) {
		this.id = id;
	}

	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public Object getDisplayValue() {
		return null;
	}

	public String getValueText() {
		return valueText;
	}

	public void setValueText(String valueText) {
		this.valueText = valueText;
	}

}
