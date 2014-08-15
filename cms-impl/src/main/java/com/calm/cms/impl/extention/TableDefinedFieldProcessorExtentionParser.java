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
	public void parser(Element el,ExtentionProcess process) {
		List<TableDefined> listAll = tableDefinedService.listAllDataTable();
		String processorId = el.attributeValue("processor-id");
		FieldType type;
		for (TableDefined td : listAll) {
			type = new FieldType();
			Integer id = td.getId();
			type.setTableDefinedId(id);
			type.setType(ProcessorType.TABLE);
			type.setProcessId(processorId);
			type.setName(td.getName());
			type.setDescription("");
			FieldType loadById = fieldTypeService.loadByProperty("tableDefinedId", id);
			if (loadById == null) {
				fieldTypeService.add(type);
			} else {
				type.setDeleteClass(false);
				fieldTypeService.update(type);
			}
		}
	}

}
