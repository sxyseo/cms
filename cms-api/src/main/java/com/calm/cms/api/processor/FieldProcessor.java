package com.calm.cms.api.processor;

import java.util.List;

import com.calm.cms.api.entity.TableColumn;

public interface FieldProcessor {

	List<?> getList(Integer id, TableColumn tableColumn);

	Object get(Integer rowId, Object value, TableColumn tableColumn);

}
