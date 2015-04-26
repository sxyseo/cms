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
	public Map<String,Object> buildData(TableDefined loadById){
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
		fields.add("proxyId");
		for(TableColumn tc:columns){
			Map<String,Object> item=column2Items(tc);
			if(item!=null){
				items.add(item);
				//如果是多对一展示数据ID
				if(tc.getRelation()==Relation.MANY2ONE){
					fields.add(tc.getId().getId()+"_ID");
    			}else{
    				fields.add(tc.getId().getId());
    			}
			}
		}
		map.put("fields", JSON.toJSONString(fields));
		map.put("columns", items);
		map.put("width",""+( fields.size()*200+71));
	}

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
