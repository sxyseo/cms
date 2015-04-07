package com.calm.cms.impl.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.calm.cms.api.processor.FieldProcessor;
import com.calm.cms.impl.processor.TableDefinedProcessor;

public class ProcessorUtils {
	private ProcessorUtils(){}
	public static FieldProcessor<?> getFieldProcessor(String name,
			ApplicationContext context) {
		int indexOf = name.indexOf("?");
		String beanName;
		if (indexOf > 0) {
			beanName = name.substring(0, indexOf);
		} else {
			beanName = name;
		}
		FieldProcessor<?> bean = context.getBean(beanName, FieldProcessor.class);
		if(indexOf>0){
			Map<String, String> param = getProcessorParameter(name
					.substring(indexOf + 1));
			if (bean instanceof TableDefinedProcessor) {
				String tableId = param.get("tableId");
				((TableDefinedProcessor) bean)
						.setTableId(Integer.parseInt(tableId));
			}
		}
		return bean;
	}

	private static Map<String, String> getProcessorParameter(String parameterStr) {
		String[] split = parameterStr.split("&");
		Map<String, String> result = new HashMap<>(split.length);
		for (String sp : split) {
			String[] split2 = sp.split("=");
			result.put(split2[0], split2[1]);
		}
		return result;
	}
}
