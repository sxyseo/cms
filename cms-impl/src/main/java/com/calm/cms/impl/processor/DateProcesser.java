package com.calm.cms.impl.processor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.processor.FieldProcessor;

public class DateProcesser implements FieldProcessor<Date> {
	private String pattern;
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat();

	@Override
	public List<Date> getList(Integer id, TableColumn tableColumn) {
		return null;
	}

	@Override
	public Date get(Integer rowId, Object value, TableColumn tableColumn) {
		if (value == null) {
			return null;
		}
		FORMAT.applyPattern(pattern);
		String string = value.toString();
		try {
			return FORMAT.parse(string);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

}
