package com.calm.cms.impl.processor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.processor.FieldProcessor;

/**
 * 日期类型处理器
 * @author dingqihui
 *
 */
public class DateProcesser implements FieldProcessor{
	/**
	 * 日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DateProcesser.class);
	/**
	 * 日期格式
	 */
	private String pattern;
	/**
	 * 日期格式处理器
	 */
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat();

	public Date get(Integer rowId, Object value, TableColumn tableColumn) {
		if (value == null) {
			return null;
		}
		FORMAT.applyPattern(pattern);
		String string = value.toString();
		try {
			return FORMAT.parse(string);
		} catch (ParseException e) {
			LOGGER.error(e.getMessage(), e);
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
