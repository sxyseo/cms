package com.calm.cms.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.calm.framework.common.entity.BaseEntity;
import com.calm.framework.common.entity.LogisticDeletable;

@Entity
@Table(name = "FIELD_TYPE")
public class FieldType implements BaseEntity, LogisticDeletable {
	@Id
	private String id;

	private String name;

	private String description;
	
	@Column(name="DELETE_CLASS")
	private Boolean deleteClass;

	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public Object getDisplayValue() {
		return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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
	
}
