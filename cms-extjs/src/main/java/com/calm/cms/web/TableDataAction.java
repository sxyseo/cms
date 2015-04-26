package com.calm.cms.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.calm.cms.api.entity.BaseColumnData;
import com.calm.cms.api.entity.BaseColumnDataKey;
import com.calm.cms.api.service.ITableDataService;
import com.calm.framework.common.entity.Paging;
import com.calm.framework.web.BaseAction;

@Controller
@RequestMapping("cms/table/data")
public class TableDataAction extends BaseAction{
	@Resource
	private ITableDataService tableDataService;
	
	@RequestMapping("/list/{tableId}")
	@ResponseBody
	public Object index(@PathVariable("tableId") Integer tableId, Integer page,
			Integer limit, Model model) {
		Paging<BaseColumnData> paging = tableDataService.paging(page, limit,tableId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("list", paging.getData());
		result.put("total", paging.getTotalCount());
		return result;
	}

	@RequestMapping("/load/{tableId}/{id}")
	@ResponseBody
	public Object load(@PathVariable("tableId") Integer tableId,
			@PathVariable("id") Integer id, Model model) {
		Map<String, Object> result = new HashMap<String, Object>();
		// columnDataService.loadById(id);
		return result;
	}
	
	@RequestMapping("/delete/{tableId}/{id}")
	@ResponseBody
	public Object delete(@PathVariable("tableId") Integer tableId,
			@PathVariable("id") Integer id, Model model) {
		Map<String, Object> result = new HashMap<String, Object>();
		tableDataService.delete(new BaseColumnDataKey(id, tableId));
		return result;
	}
	
}
