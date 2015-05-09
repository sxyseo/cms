package com.calm.cms.api.entity;

import com.calm.framework.common.entity.BaseEntity;
import com.calm.framework.common.entity.TimeUpdate;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "CMS_TABLE_DEFINED")
public class TableDefined implements BaseEntity<Integer>,TimeUpdate {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	@TableGenerator(name = "CMS_TABLE_DEFINED_ID", pkColumnValue = "CMS_TABLE_DEFINED_ID", valueColumnName = "ID_VALUE", pkColumnName = "ID_GENERATOR_NAME", table = "ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(generator = "CMS_TABLE_DEFINED_ID", strategy = GenerationType.TABLE)
	private Integer id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "SQL_TEXT", length = 1000000000)
	private String sqlText;

	@OneToMany(fetch = FetchType.LAZY, cascade = { })
	@JoinColumn(name = "TABLE_ID")
	@OrderBy("orderIndex")
	private List<TableColumn> columns;
	@Column(name = "TABLE_TYPE")
	@Enumerated(EnumType.STRING)
	private TableType tableType;
	@Column(name = "ROW_ID")
	private Integer rowId;
	@Column(name = "CREATE_TIME")
	private Timestamp createTime;
	@Column(name = "LAST_UPDATE_TIME")
	private Timestamp lastUpdateTime;
	public TableDefined() {
	}

	public TableDefined(Integer id) {
		super();
		this.id = id;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		TableDefined other = (TableDefined) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getRowId() {
		return rowId;
	}

	public void setRowId(Integer rowId) {
		this.rowId = rowId;
	}

	@Override
	public String getObjectName() {
		return "数据模型";
	}

}
