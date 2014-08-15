package com.calm.cms.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.calm.framework.common.entity.BaseEntity;

@Entity
@Table(name = "COLUMN_DEFINED")
public class ColumnDefined implements BaseEntity<Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@TableGenerator(name = "COLUMN_DEFINED_ID", pkColumnValue = "COLUMN_DEFINED_ID", valueColumnName = "ID_VALUE", pkColumnName = "ID_GENERATOR_NAME", table = "ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(generator = "COLUMN_DEFINED_ID", strategy = GenerationType.TABLE)
	private Integer id;
	@Column(name = "COLUMN_NAME")
	private String columnName;
	private String name;
	private String description;
	@ManyToOne
	@JoinColumn(name = "PROCESSOR")
	private FieldType processor;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public FieldType getProcessor() {
		return processor;
	}

	public void setProcessor(FieldType processor) {
		this.processor = processor;
	}

}
