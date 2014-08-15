package com.calm.cms.impl.extention;

import java.util.List;

import javax.annotation.Resource;

import org.dom4j.Element;

import com.calm.cms.api.entity.FieldType;
import com.calm.cms.api.entity.ProcessorType;
import com.calm.cms.api.entity.TableDefined;
import com.calm.cms.api.service.ITableDefinedService;
import com.calm.framework.extention.ExtentionProcess;
import com.calm.framework.extention.NamespaceTag;

@NamespaceTag(uri = "http://www.calm.com/extention/cms/fieldProcessor", tag = "tableDefinedProcessor")
public class TableDefinedFieldProcessorExtentionParser extends
		AbstractFieldProcessorExtentionParser {
	@Resource
	private ITableDefinedService tableDefinedService;

	@Override
	public void parser(Element el, ExtentionProcess process) {
		List<TableDefined> listAll = tableDefinedService.listAllDataTable();
		String processorId = el.attributeValue("processor-id");
		FieldType type;
		for (TableDefined td : listAll) {
			Integer id = td.getId();
			type = fieldTypeService.loadByProperty("tableDefinedId", id);
			if (type == null) {
				type = new FieldType();
			}
			type.setTableDefinedId(id);
			type.setType(ProcessorType.TABLE);
			type.setProcessId(processorId);
			type.setName(td.getName());
			type.setDescription("");
			type.setDeleteClass(false);
			if (type.getId() == null) {
				fieldTypeService.add(type);
			} else {
				fieldTypeService.update(type);
			}
		}
	}

}
