package com.calm.cms.api.processor;

import java.io.Serializable;
import java.util.List;

import com.calm.cms.api.entity.TableColumn;
import com.calm.framework.common.entity.BaseEntity;

public interface ListableFieldProcessor<T extends BaseEntity<? extends Serializable>> extends FieldProcessor{
	List<T> getList(Integer id, TableColumn tableColumn);
}
