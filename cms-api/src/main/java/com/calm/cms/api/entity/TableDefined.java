package com.calm.cms.api.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.calm.framework.common.entity.BaseEntity;

@Entity
@Table(name = "TABLE_DEFINED")
public class TableDefined implements BaseEntity<Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	@TableGenerator(name = "TABLE_DEFINED_ID", pkColumnValue = "TABLE_DEFINED_ID", valueColumnName = "ID_VALUE", pkColumnName = "ID_GENERATOR_NAME", table = "ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(generator = "TABLE_DEFINED_ID", strategy = GenerationType.TABLE)
	private Integer id;
	@Column(name = "NAME")
	private String name;
	@Column(name = "DESCRIPTION")
	private String description;
	@Column(name = "SQL_TEXT", length = 1000000000)
	private String sqlText;
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "TABLE_ID")
	private List<TableColumn> columns;
	@Column(name = "TABLE_TYPE")
	@Enumerated(EnumType.STRING)
	private TableType tableType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String getDisplayName() {
		return "数据表";
	}

	@Override
	public Object getDisplayValue() {
		return getName();
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

	public List<TableColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<TableColumn> columns) {
		this.columns = columns;
	}

	public String getSqlText() {
		return sqlText;
	}

	public void setSqlText(String sqlText) {
		this.sqlText = sqlText;
	}

	public TableType getTableType() {
		return tableType;
	}

	public void setTableType(TableType tableType) {
		this.tableType = tableType;
	}

}
