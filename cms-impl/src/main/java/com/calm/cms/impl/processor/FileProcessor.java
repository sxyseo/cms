package com.calm.cms.impl.processor;

import java.util.List;

import org.springframework.stereotype.Service;

import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.processor.FieldProcessor;

@Service
public class FileProcessor implements FieldProcessor<String> {

	@Override
	public List<String> getList(Integer id, TableColumn tableColumn) {
		return null;
	}

	@Override
	public String get(Integer rowId, Object value, TableColumn tableColumn) {
		if (value == null) {
			return null;
		}
		return value.toString();
	}
}
