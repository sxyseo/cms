package com.calm.cms.api.entity;

import com.calm.framework.common.entity.BaseEntity;
import com.calm.framework.common.entity.LogisticDeletable;

import javax.persistence.*;

@Entity
@Table(name = "FIELD_TYPE")
public class FieldType implements BaseEntity<Integer>, LogisticDeletable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@TableGenerator(name = "FIELD_TYPE_ID", pkColumnValue = "FIELD_TYPE_ID", valueColumnName = "ID_VALUE", pkColumnName = "ID_GENERATOR_NAME", table = "ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(generator = "FIELD_TYPE_ID", strategy = GenerationType.TABLE)
	private Integer id;
	@Column(name = "PROCESS_ID")
	private String processId;
	private String name;

	private String description;
	@Column(name = "PROCESSOR_TYPE")
	@Enumerated(EnumType.STRING)
	private ProcessorType type;

	@Column(name = "TABLE_DEFINED_ID")
	private Integer tableDefinedId;

	@Column(name = "DELETE_CLASS")
	private Boolean deleteClass;

	public FieldType() {
	}
	
	public FieldType(Integer id) {
		super();
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Boolean getDeleteClass() {
		return deleteClass;
	}

	public void setDeleteClass(Boolean deleteClass) {
		this.deleteClass = deleteClass;
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
		FieldType other = (FieldType) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public ProcessorType getType() {
		return type;
	}

	public void setType(ProcessorType type) {
		this.type = type;
	}

	public Integer getTableDefinedId() {
		return tableDefinedId;
	}

	public void setTableDefinedId(Integer tableDefinedId) {
		this.tableDefinedId = tableDefinedId;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

}
