package com.calm.cms.impl.processor;

import java.util.List;

import org.springframework.stereotype.Service;

import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.processor.FieldProcessor;

@Service
public class DoubleProcessor implements FieldProcessor<Double> {

	@Override
	public List<Double> getList(Integer id, TableColumn tableColumn) {
		return null;
	}

	@Override
	public Double get(Integer rowId, Object value, TableColumn tableColumn) {
		if (value == null) {
			return null;
		}
		return Double.valueOf(value.toString());
	}
}
