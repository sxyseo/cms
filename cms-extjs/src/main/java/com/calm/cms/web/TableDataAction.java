package com.calm.cms.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.calm.cms.api.entity.BaseColumnData;
import com.calm.cms.api.service.IColumnDataService;
import com.calm.framework.web.BaseAction;

@Controller
@RequestMapping("cms/tableData")
public class TableDataAction extends BaseAction{
	@Resource
	private IColumnDataService columnDataService;
	
	@RequestMapping("list")
	@ResponseBody
	public Object index(Integer tableId, Integer currentPage,
			Integer pageSize, Model model) {
		Map<String,Object> result=new HashMap<String, Object>();
		List<BaseColumnData> listAll = columnDataService.listAll(tableId);
		result.put("list", listAll);
		return result;
	}
}
