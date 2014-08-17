package com.calm.cms.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.calm.cms.api.entity.TableDefined;
import com.calm.cms.api.entity.TableType;
import com.calm.cms.api.service.ITableDefinedService;
import com.calm.extjs.common.BaseCurdAction;

@Controller
@RequestMapping("cms/tableDefined")
public class TableDefinedAction extends
		BaseCurdAction<Integer, TableDefined, ITableDefinedService> {

	@RequestMapping("list")
	@Override
	@ResponseBody
	public Object index(TableDefined entity, Integer currentPage,
			Integer pageSize, Model model) {
		return super.index(entity, currentPage, pageSize, model);
	}

	@RequestMapping(value="add",method={RequestMethod.POST})
	@Override
	@ResponseBody
	public Object add(TableDefined entity, Model model) {
		entity.setTableType(TableType.DATA);
		return super.add(entity, model);
	}

	@RequestMapping(value="update",method={RequestMethod.POST})
	@Override
	@ResponseBody
	public Object update(Integer id, TableDefined entity, Model model) {
		return super.update(id, entity, model);
	}
	
	@RequestMapping("delete")
	@Override
	@ResponseBody
	public Object delete(Integer id, Model model) {
		return super.delete(id, model);
	}
	@Resource
	@Override
	public void setService(ITableDefinedService service) {
		super.setService(service);
	}
}
