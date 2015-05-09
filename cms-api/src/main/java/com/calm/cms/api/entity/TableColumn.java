package com.calm.cms.api.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.calm.framework.annotation.Json;
import com.calm.framework.common.entity.BaseEntity;

@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "CMS_TABLE_COLUMN")
public class TableColumn implements BaseEntity<TableColumnKey> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	@EmbeddedId
	private TableColumnKey id;

	@Column(name = "DEFAULT_VALUE")
	private String defaultValue;

	@Enumerated(EnumType.STRING)
	@Column(name = "RELATION")
	private Relation relation;

	@Column(name = "NAME")
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;

	@ManyToOne
	@JoinColumn(name = "PROCESSOR")
	private FieldType processor;

	@Column(name = "REQUIRED")
	private Boolean required;

	@Column(name = "ORDER_INDEX")
	private Integer orderIndex;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RELATION_TABLE_ID")
	private TableDefined relationTableDefined;

	@Column(name = "RELATION_COLUMN")
	private String relationColumn;
	
	@Column(name = "SHOW_NAME")
	private Boolean showName;
	public TableColumn() {
	}

	public TableColumn(TableDefined tableDefined, String id) {
		this.id = new TableColumnKey(tableDefined, id);
	}

	@Override
	public String getDisplayName() {
		return name;
	}

	@Override
	public Object getDisplayValue() {
		return getId().getId();
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

	public Boolean getRequired() {
		if (required == null) {
			required = false;
		}
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
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

	public FieldType getProcessor() {
		return processor;
	}

	public void setProcessor(FieldType processor) {
		this.processor = processor;
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
		TableColumn other = (TableColumn) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	public String getRelationColumn() {
		return relationColumn;
	}

	public void setRelationColumn(String relationColumn) {
		this.relationColumn = relationColumn;
	}
	@Json(serialize=false)
	public TableDefined getRelationTableDefined() {
		return relationTableDefined;
	}

	public void setRelationTableDefined(TableDefined relationTableDefined) {
		this.relationTableDefined = relationTableDefined;
	}

	public Boolean getShowName() {
		return showName;
	}

	public void setShowName(Boolean showName) {
		this.showName = showName;
	}

	@Override
	public String getObjectName() {
		return "模型属性";
	}
}
