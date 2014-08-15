package com.calm.cms.impl.extention;

import org.dom4j.Element;

import com.calm.cms.api.entity.FieldType;
import com.calm.cms.api.entity.ProcessorType;
import com.calm.framework.extention.ExtentionProcess;
import com.calm.framework.extention.NamespaceTag;

@NamespaceTag(uri = "http://www.calm.com/extention/cms/fieldProcessor", tag = "simple")
public class SimpleFieldProcessorExtentionParser extends
		AbstractFieldProcessorExtentionParser {
	@Override
	public void parser(Element el,ExtentionProcess process) {
		String id = el.attributeValue("id");
		String name = el.attributeValue("name");
		String description = el.attributeValue("description");
		FieldType ft = new FieldType();
		ft.setName(name);
		ft.setProcessId(id);
		ft.setType(ProcessorType.SIMPLE);
		ft.setDescription(description);
		FieldType loadById = fieldTypeService.loadByProperty("processId", id);
		if (loadById == null) {
			fieldTypeService.add(ft);
		} else {
			ft.setId(loadById.getId());
			ft.setDeleteClass(false);
			fieldTypeService.update(ft);
		}
	}

}
