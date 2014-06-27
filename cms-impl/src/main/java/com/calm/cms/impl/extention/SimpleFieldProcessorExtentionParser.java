package com.calm.cms.impl.extention;

import org.dom4j.Element;

import com.calm.cms.api.entity.FieldType;
import com.calm.framework.extention.NamespaceTag;

@NamespaceTag(uri = "http://www.calm.com/extention/cms/fieldProcessor", tag = "simple")
public class SimpleFieldProcessorExtentionParser extends
		AbstractFieldProcessorExtentionParser {
	@Override
	public void parser(Element el) {
		String id = el.attributeValue("id");
		String name = el.attributeValue("name");
		String description = el.attributeValue("description");
		FieldType ft = new FieldType();
		ft.setId(id);
		ft.setName(name);
		ft.setDescription(description);
		FieldType loadById = fieldTypeService.loadById(id);
		if (loadById == null) {
			fieldTypeService.add(ft);
		} else {
			ft.setDeleteClass(false);
			fieldTypeService.update(ft);
		}
	}

}
