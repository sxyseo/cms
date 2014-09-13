package com.calm.cms.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.calm.cms.api.entity.FieldType;
import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.entity.TableColumnKey;
import com.calm.cms.api.entity.TableDefined;
import com.calm.cms.api.service.ITableColumnService;
import com.calm.cms.vo.TableColumnVo;
import com.calm.extjs.common.BaseCurdAction;

@Controller
@RequestMapping("cms/tableColumn")
public class TableColumnAction extends
		BaseCurdAction<TableColumnKey, TableColumn, ITableColumnService> {

	@RequestMapping(value="add",method={RequestMethod.POST})
	@ResponseBody
	public Object add(TableColumnVo tcv,TableColumn entity, Model model) {
		TableColumnKey tableColumnKey = new TableColumnKey(new TableDefined(tcv.getTableId()), tcv.getColumnName());
		entity.setId(tableColumnKey);
		entity.setProcessor(new FieldType(tcv.getProcessorId()));
		return super.add(entity, model);
	}

	@RequestMapping(value="update",method={RequestMethod.POST})
	@ResponseBody
	public Object update(TableColumnVo tcv, TableColumn entity, Model model) {
		TableColumnKey tableColumnKey = new TableColumnKey(new TableDefined(tcv.getTableId()), tcv.getColumnName());
		entity.setId(tableColumnKey);
		entity.setProcessor(new FieldType(tcv.getProcessorId()));
		return super.update(tableColumnKey, entity, model);
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Object delete(String id,Integer tableId, Model model) {
		return super.delete(new TableColumnKey(new TableDefined(tableId), id), model);
	}
	
	@RequestMapping("order")
	@ResponseBody
	public Object order(String id,Integer tableId,boolean up, Model model) {
		Map<String,Object> result=new HashMap<>();
		getService().order(id,tableId,up);
		return result;
	}
	
	@Resource
	@Override
	public void setService(ITableColumnService service) {
		super.setService(service);
	}
}
