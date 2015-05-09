package com.calm.cms.web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.calm.cms.api.entity.BaseColumnData;
import com.calm.cms.api.entity.BaseColumnDataKey;
import com.calm.cms.api.entity.FieldType;
import com.calm.cms.api.entity.Relation;
import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.entity.TableColumnKey;
import com.calm.cms.api.entity.TableDefined;
import com.calm.cms.api.processor.ListableFieldProcessor;
import com.calm.cms.api.service.ITableColumnService;
import com.calm.cms.api.service.ITableDataService;
import com.calm.cms.api.service.ITableDefinedService;
import com.calm.framework.common.entity.BaseEntity;
import com.calm.framework.common.entity.Paging;
import com.calm.framework.web.BaseAction;

@Controller
@RequestMapping("cms/table/data")
public class TableDataAction extends BaseAction{
	@Resource
	private ITableDataService tableDataService;
	@Resource
	private ITableDefinedService tableDefinedService;
	@Resource
	private ITableColumnService tableColumnService;
	@Resource
	private ApplicationContext context;
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
	
	@RequestMapping(value="/add/{tableId}/",method={RequestMethod.POST})
	@ResponseBody
	public Object add(@PathVariable("tableId") Integer tableId,Model model,HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			TableDefined loadById = tableDefinedService.loadById(tableId);
			Map<String,String> data = loadFormData(loadById, request);
			tableDataService.add(loadById,data);
			addMessage("GLOBAL_I_00001");
			result.put("SUCCESS", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	@RequestMapping(value="/update/{tableId}/{id}",method={RequestMethod.POST})
	@ResponseBody
	public Object update(@PathVariable("tableId") Integer tableId,@PathVariable("id") Integer rowId,Model model,HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			TableDefined loadById = tableDefinedService.loadById(tableId);
			Map<String,String> data = loadFormData(loadById, request);
			tableDataService.update(loadById,rowId,data);
			addMessage("GLOBAL_I_00002");
			result.put("SUCCESS", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private Map<String,String> loadFormData(TableDefined loadById,
			HttpServletRequest request) {
		Map<String,String> data  =new HashMap<String, String>();
		List<TableColumn> columns = loadById.getColumns();
		
		for(TableColumn tc:columns){
			String id = tc.getId().getId();
			String dataId;
			if(tc.getRelation()==Relation.MANY2ONE){
				dataId = id+"_ID";
			}else{
				dataId = id;
			}
			String value = request.getParameter(dataId);
			data.put(id, value);
		}
		return data;
	}
	@RequestMapping("/delete/{tableId}/{id}")
	@ResponseBody
	public Object delete(@PathVariable("tableId") Integer tableId,
			@PathVariable("id") Integer id, Model model) {
		Map<String, Object> result = new HashMap<String, Object>();
		tableDataService.delete(new BaseColumnDataKey(id, tableId));
		return result;
	}
	
	@SuppressWarnings({"unchecked" })
	@RequestMapping("/store/{tableId}/{field}")
	@ResponseBody
	public Object store(@PathVariable("tableId") Integer tableId,
			@PathVariable("field") String field, Model model) {
		TableColumn loadById = tableColumnService.loadById(new TableColumnKey(new TableDefined(tableId), field));
		FieldType processor = loadById.getProcessor();
		String processId = processor.getProcessId();
		ListableFieldProcessor<? extends BaseEntity<? extends Serializable>> bean = context.getBean(processId, ListableFieldProcessor.class);
		return bean.getList(null,loadById);
	}
}
