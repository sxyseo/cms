package com.calm.cms.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.calm.cms.api.entity.Relation;
import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.entity.TableDefined;
import com.calm.cms.api.service.ITableDefinedService;
@Service("/dynamic/com/calm/cms/ui/DataTableWindow")
public class TableDataProcessor implements StaticDataBuilder {
	@Resource
	private ITableDefinedService tableDefinedService;
	@Override
	public Object builder(String id) {
		TableDefined loadById = tableDefinedService.loadById(Integer.valueOf(id));
		return buildData(loadById);
	}
	/**
	 * 构建数据
	 * @param loadById
	 * @return
	 */
	private Map<String,Object> buildData(TableDefined loadById){
		Map<String,Object> result=new HashMap<String, Object>();
		result.put("id", loadById.getId());
		result.put("title", loadById.getName());
		getColumnFields(loadById,result);
		return result;
	}
	/**
	 * 根据列信息获得页面展示列和数据字段
	 * @param loadById
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	private void getColumnFields(TableDefined loadById,Map<String,Object> map){
		List<TableColumn> columns = loadById.getColumns();
		
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		
		//设置编号
		Map<String,Object> result=new HashMap<String, Object>();
		result.put("text", "编号");
		result.put("width", 200);
		result.put("dataIndex", "proxyId");
		items.add(result);
		List<String> fields =new ArrayList<String>();
		List<String> many2ones=new ArrayList<String>();
		fields.add("proxyId");
		for(TableColumn tc:columns){
			Map<String,Object> item=column2Items(tc);
			if(item!=null){
				items.add(item);
				//如果是多对一展示数据ID
				if(tc.getRelation()==Relation.MANY2ONE){
					fields.add(tc.getId().getId()+"_ID");
					many2ones.add(tc.getId().getId());
    			}else{
    				fields.add(tc.getId().getId());
    			}
			}
		}
		List<Map<String,Object>> editorColumns = new ArrayList<Map<String,Object>>();
		Map<String,Object> row = null;
		for(int i=0;i<columns.size();i++){
			TableColumn tc=columns.get(i);
			if(i%2==0){
				row=new HashMap<String, Object>();
				row.put("layout", "column");
				editorColumns.add(row);
			}
			Map<String, Object> column2EditorColumn = column2EditorColumn(tc);
			Object object = row.get("items");
			if(object==null){
				object = new ArrayList<Map<String, Object>>();
				row.put("items", object);
			}
			List<Map<String, Object>> r=(List<Map<String, Object>>) object;
			r.add(column2EditorColumn);
		}
		
		map.put("fields", JSON.toJSONString(fields));
		map.put("columns", items);
		map.put("stores", many2ones);
		map.put("editorColumns", editorColumns);
		map.put("width",""+( fields.size()*200+71));
	}

	/**
	 * 转换编辑列
	 * @param tc
	 * @return
	 */
	private Map<String, Object> column2EditorColumn(TableColumn tc) {
		Map<String,Object> result=new HashMap<String, Object>();
		//列占比
		result.put("columnWidth", 0.5);
		//布局
		result.put("layout", "form");

		Map<String,String> field=new HashMap<String, String>();
		field.put("fieldLabel", tc.getName());
		if(tc.getRelation()==Relation.MANY2ONE){
			field.put("xtype", "combobox");
			field.put("store", "me.fieldStores.store_"+tc.getId().getId());
			field.put("displayField", "displayName");
			field.put("valueField", "displayValue");
			field.put("editable", "false");
		}else{
			switch (tc.getProcessor().getProcessId()) {
			case "booleanProcessor":
				field.put("xtype", "checkbox");
				field.put("inputValue", "true");
				break;
			case "integerProcessor":
				field.put("vtype", "alphanum");
			default:
				field.put("xtype", "textfield");
			}
		}
		field.put("name", tc.getId().getId());
		List<Map<String,String>> items=new ArrayList<Map<String,String>>();
		items.add(field);
		result.put("items", items);
		return result;
	}
	/**
	 * 转换查询列表
	 * @param tc
	 * @return
	 */
	private Map<String, Object> column2Items(TableColumn tc) {
		Map<String,Object> result=new HashMap<String, Object>();
		result.put("text", tc.getName());
		result.put("width", 200);
		String dataIndex ;
		switch (tc.getRelation()) {
		case ONE2MANY:
			return null;
		case MANY2ONE:
			dataIndex =  tc.getId().getId()+"_ID";
			break;
		default:
			dataIndex = tc.getId().getId();
			break;
		}
		
//		if(tc.getProcessor().getType()==ProcessorType.TABLE){
			result.put("dataIndex", dataIndex);
			return result;
//		}else{
//			
//		}
//    	if(c.processor.type='table'){
//    		return {
//                text: c.name,
//                dataIndex: c.id.id,
//                width:200
//           }
//    	}else{
//    		return {
//                text: c.name,
//                dataIndex: c.id.id,
//                width:200
//           }
//    	}
//		return null;
	}
}
