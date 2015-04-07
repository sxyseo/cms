package com.calm.cms.api.processor;

import com.calm.cms.api.entity.TableColumn;

public interface FieldProcessor<T> {

	T get(Integer rowId, Object value, TableColumn tableColumn);

}
