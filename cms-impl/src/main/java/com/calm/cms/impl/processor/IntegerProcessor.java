package com.calm.cms.impl.processor;

import org.springframework.stereotype.Service;

import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.processor.FieldProcessor;

@Service
public class IntegerProcessor implements FieldProcessor{

	@Override
	public Integer get(Integer rowId, Object value, TableColumn tableColumn) {
		if (value == null) {
			return null;
		}
		if(value.toString().trim().length()==0){
			return null;
		}
		return Integer.valueOf(value.toString());
	}

	@Override
	public Object getDisplayValue(Object value) {
		return null;
	}

	@Override
	public Class<?> getType() {
		return Integer.class;
	}
}
