package com.calm.cms.impl.processor;

import org.springframework.stereotype.Service;

import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.processor.FieldProcessor;

@Service
public class BooleanProcessor implements FieldProcessor {

	@Override
	public Boolean get(Integer rowId, Object value, TableColumn tableColumn) {
		if (value == null) {
			return null;
		}
		return Boolean.valueOf(value.toString());
	}
}
