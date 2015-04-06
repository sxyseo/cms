package com.calm.cms.impl.processor;

import java.util.List;

import org.springframework.stereotype.Service;

import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.processor.FieldProcessor;

@Service
public class BooleanProcessor implements FieldProcessor<Boolean> {

	@Override
	public List<Boolean> getList(Integer id, TableColumn tableColumn) {
		return null;
	}

	@Override
	public Boolean get(Integer rowId, Object value, TableColumn tableColumn) {
		if (value == null) {
			return null;
		}
		return Boolean.valueOf(value.toString());
	}
}
