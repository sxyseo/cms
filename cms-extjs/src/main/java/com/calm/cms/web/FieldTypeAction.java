package com.calm.cms.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.calm.cms.api.entity.FieldType;
import com.calm.cms.api.entity.ProcessorType;
import com.calm.cms.api.service.IFieldTypeService;
import com.calm.extjs.common.BaseCurdAction;

@Controller
@RequestMapping("cms/fieldType")
public class FieldTypeAction extends
		BaseCurdAction<Integer, FieldType, IFieldTypeService> {

	@RequestMapping("listWithFilterType")
	@ResponseBody
	public Object listWithFilter(ProcessorType type) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<FieldType> list = getService().listByType(type,true);
		result.put(LIST, list);
		return result;
	}

	@Resource
	@Override
	public void setService(IFieldTypeService service) {
		super.setService(service);
	}
}
