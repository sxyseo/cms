package com.calm.cms.api.processor;

import com.calm.cms.api.entity.TableColumn;

public interface FieldProcessor {

	Object get(Integer rowId, Object value, TableColumn tableColumn);

}
