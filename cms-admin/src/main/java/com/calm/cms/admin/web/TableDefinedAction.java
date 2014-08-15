package com.calm.cms.admin.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.calm.cms.api.entity.ColumnDefined;
import com.calm.cms.api.entity.Relation;
import com.calm.cms.api.entity.TableDefined;
import com.calm.cms.api.service.IColumnDefinedService;
import com.calm.cms.api.service.ITableDefinedService;
import com.calm.framework.web.BaseCurdAction;

@Controller
@RequestMapping("cms/tableDefined")
public class TableDefinedAction extends
		BaseCurdAction<Integer,TableDefined, ITableDefinedService> {
	@Resource
	private IColumnDefinedService columnDefinedService;

	@RequestMapping("/")
	public String index(
			TableDefined entity,
			Integer currentPage,
			@CookieValue(value = "_cookie_page_size", defaultValue = "20") Integer pageSize,
			Model model) {
		return super.index(entity, currentPage, pageSize, model);
	}

	@RequestMapping("/edit/")
	public String add(Model model) {
		Relation[] relations = Relation.values();
		model.addAttribute("relations", relations);
		List<ColumnDefined> listAll = columnDefinedService.listAll(true);
		model.addAttribute("cols", listAll);
		return super.add(model);
	}

	@RequestMapping(value = "/edit/", method = RequestMethod.POST)
	public String add(TableDefined entity, ArrayList<ColumnDefined> columns,
			Model model) {
		if (StringUtils.isEmpty(entity.getName())) {
			addFieldError("name", "GLOBAL_E_00001");
		}
		return super.add(entity, model);
	}

	@RequestMapping("/edit/{id}")
	public String update(@PathVariable("id") Integer id, Model model) {
		Relation[] relations = Relation.values();
		model.addAttribute("relations", relations);
		List<ColumnDefined> listAll = columnDefinedService.listAll(true);
		model.addAttribute("cols", listAll);
		return super.update(id, model);
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
	public String update(@PathVariable("id") Integer id, TableDefined entity,
			Model model) {
		if (StringUtils.isEmpty(entity.getName())) {
			addFieldError("name", "GLOBAL_E_00001");
		}
		return super.update(id, entity, model);
	}

	@RequestMapping(value = "/delete/{id}")
	public String delete(@PathVariable("id") Integer id, Model model) {
		return super.delete(id, model);
	}

	@Resource
	@Override
	public void setService(ITableDefinedService service) {
		super.setService(service);
	}

	@Override
	public String getContext() {
		return "/cms/tableDefined/";
	}

}
