package com.calm.cms.impl.extention;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;

import com.calm.cms.api.entity.FieldType;
import com.calm.cms.api.service.IFieldTypeService;
import com.calm.framework.extention.ExtentionParser;
public abstract class AbstractFieldProcessorExtentionParser implements
		ExtentionParser, InitializingBean {
	public static boolean resetField = false;

	@Resource
	protected IFieldTypeService fieldTypeService;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (!resetField) {
			List<FieldType> listAll = fieldTypeService.listAll(true);
			for (FieldType ft : listAll) {
				fieldTypeService.delete(ft);
			}
			resetField = true;
		}
	}
}
