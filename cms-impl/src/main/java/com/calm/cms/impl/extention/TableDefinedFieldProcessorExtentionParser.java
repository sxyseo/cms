package com.calm.cms.impl.extention;

import java.util.List;

import javax.annotation.Resource;

import org.dom4j.Element;

import com.calm.cms.api.entity.FieldType;
import com.calm.cms.api.entity.TableDefined;
import com.calm.cms.api.service.ITableDefinedService;
import com.calm.framework.extention.NamespaceTag;

@NamespaceTag(uri = "http://www.calm.com/extention/cms/fieldProcessor", tag = "tableDefinedProcessor")
public class TableDefinedFieldProcessorExtentionParser extends
		AbstractFieldProcessorExtentionParser {
	@Resource
	private ITableDefinedService tableDefinedService;

	@Override
	public void parser(Element el) {
		List<TableDefined> listAll = tableDefinedService.listAllDataTable();
		String processorId = el.attributeValue("processor-id");
		FieldType type;
		for (TableDefined td : listAll) {
			Integer id = td.getId();
			type = new FieldType();
			type.setId(processorId + "?tableId=" + id);
			type.setName(td.getName());
			type.setDescription("");
			FieldType loadById = fieldTypeService.loadById(type.getId());
			if (loadById == null) {
				fieldTypeService.add(type);
			} else {
				type.setDeleteClass(false);
				fieldTypeService.update(type);
			}
		}
	}

}
