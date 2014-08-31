package com.calm.cms.api.processor;

import com.calm.cms.api.entity.TableColumn;

import java.util.List;

public interface FieldProcessor {

	List<?> getList(Integer id, TableColumn tableColumn);

	Object get(Integer rowId, Object value, TableColumn tableColumn);

}
