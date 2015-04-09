package com.calm.cms.impl.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.calm.cms.api.processor.FieldProcessor;
import com.calm.cms.impl.processor.TableDefinedProcessor;
/**
 * 处理器工具类
 * @author dingqihui
 *
 */
public abstract class ProcessorUtils {
	private ProcessorUtils(){
	}
	
	/**
	 * 根据名称获得属性处理器
	 * @param name 处理器名称
	 * @param context spring上下文
	 * @return 处理器
	 */
	public static FieldProcessor getFieldProcessor(String name,
			ApplicationContext context) {
		int indexOf = name.indexOf("?");
		String beanName;
		if (indexOf > 0) {
			beanName = name.substring(0, indexOf);
		} else {
			beanName = name;
		}
		FieldProcessor bean = context.getBean(beanName, FieldProcessor.class);
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

	/**
	 * 解析名称上的参数
	 * @param parameterStr 包含参数的字符串
	 * @return 参数的名值对
	 */
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
