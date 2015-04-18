package com.calm.cms.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.calm.cms.api.entity.ColumnData;
import com.calm.cms.api.service.IColumnDataService;
import com.calm.framework.common.entity.Paging;
import com.calm.framework.web.BaseAction;

@Controller
@RequestMapping("cms/tableData")
public class TableDataAction extends BaseAction{
	@Resource
	private IColumnDataService columnDataService;
	
	@RequestMapping("list")
	@ResponseBody
	public Object index(Integer tableId, Integer page, Integer limit, Model model) {
		Paging<ColumnData> paging = columnDataService.paging(page, limit, tableId);
		Map<String,Object> result=new HashMap<String, Object>();
//		List<BaseColumnData> listAll = columnDataService.listAll(tableId);
		result.put("list", paging.getData());
		result.put("total", paging.getTotalCount());
		return result;
	}
}
