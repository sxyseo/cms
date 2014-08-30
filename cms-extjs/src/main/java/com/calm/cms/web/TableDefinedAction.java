package com.calm.cms.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.entity.TableDefined;
import com.calm.cms.api.entity.TableType;
import com.calm.cms.api.service.ITableColumnService;
import com.calm.cms.api.service.ITableDefinedService;
import com.calm.cms.vo.TableColumnVo;
import com.calm.extjs.common.BaseCurdAction;

@Controller
@RequestMapping("cms/tableDefined")
public class TableDefinedAction extends
		BaseCurdAction<Integer, TableDefined, ITableDefinedService> {
	@Resource
	private ITableColumnService tableColumnService;
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
	@RequestMapping("listAllTableColumn")
	@ResponseBody
	public Object listAllTableColumn(Integer tableId){
		Map<String,Object> result=new HashMap<String, Object>();
		List<TableColumn> columns = tableColumnService.listByProperty("id.tableDefined.id", tableId);
		List<TableColumnVo> list=new ArrayList<>(columns.size());
		
		result.put(LIST, list);
		return result;
	}
	
	@Resource
	@Override
	public void setService(ITableDefinedService service) {
		super.setService(service);
	}
}
