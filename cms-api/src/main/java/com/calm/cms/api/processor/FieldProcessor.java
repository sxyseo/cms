package com.calm.cms.api.processor;

import com.calm.cms.api.entity.TableColumn;

import java.util.List;

public interface FieldProcessor<T> {

	List<T> getList(Integer id, TableColumn tableColumn);

	T get(Integer rowId, Object value, TableColumn tableColumn);

}
