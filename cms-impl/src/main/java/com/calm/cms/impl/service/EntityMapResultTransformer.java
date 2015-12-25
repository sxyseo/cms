package com.calm.cms.impl.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import javax.annotation.Resource;

import org.hibernate.transform.AliasedTupleSubsetResultTransformer;
import org.springframework.stereotype.Component;

import com.calm.cms.Constant;
import com.calm.cms.api.compile.Compiler;
import com.calm.cms.api.entity.BaseColumnData;
import com.calm.cms.api.entity.BaseColumnDataKey;
import com.calm.cms.api.entity.FieldType;
import com.calm.cms.api.entity.ProcessorType;
import com.calm.cms.api.entity.Relation;
import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.entity.TableColumnKey;
import com.calm.cms.api.entity.TableDefined;
import com.calm.cms.api.processor.FieldProcessor;
import com.calm.cms.api.processor.ListableFieldProcessor;
import com.calm.cms.api.service.ITableColumnService;
import com.calm.cms.api.service.ITableDefinedService;
import com.calm.cms.impl.processor.TableDefinedProcessor;
import com.calm.framework.ApplicationContext;
import com.calm.framework.common.exception.FrameworkExceptioin;
import com.calm.framework.util.StringUtil;

@Component
public class EntityMapResultTransformer extends
		AliasedTupleSubsetResultTransformer {
	
	private ThreadLocal<Map<String, BaseColumnData>> cache = new ThreadLocal<Map<String, BaseColumnData>>() {
		@Override
		protected Map<String, BaseColumnData> initialValue() {
			return new HashMap<>();
		}
	};
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Resource
	private ITableColumnService tcService;
	@Resource
	private ITableDefinedService tdService;
	@Resource
	private ApplicationContext context;

	/**
	 * {@inheritDoc}
	 */
	public Object transformTuple(Object[] tuple, String[] aliases) {
		//数组转化为名值对
		Map<String, Object> result = new HashMap<>(tuple.length);
		for (int i = 0; i < tuple.length; i++) {
			String alias = aliases[i];
			if (alias == null) {
				continue;
			}
			result.put(alias, tuple[i]);
		}

		Integer rowId = (Integer) result.get(Constant.ID);
		Integer tableId = (Integer) result.get(Constant.TABLE_ID);
		
		Map<String, BaseColumnData> map = cache.get();
		String cacheKey = tableId + ":" + rowId;
		//获得缓存对象
		BaseColumnData map2 = map.get(cacheKey);
		if (map2 == null) {
			//如果缓存不存在,去数据库查询
			TableDefined table = tdService.loadById(tableId);
			//初始化数据对象
			BaseColumnData columnData = null;
			try {
				Class<? extends BaseColumnData> clazz = Compiler.getClass(table,EntityMapResultTransformer.class.getClassLoader(),context);
				columnData = clazz.newInstance();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e1) {
				throw new FrameworkExceptioin(e1.getMessage());
			} catch (CannotCompileException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//构建KEY
			BaseColumnDataKey columnDataKey=new BaseColumnDataKey(rowId,tableId);
			columnData.setId(columnDataKey);
			map.put(cacheKey, columnData);
			//处理各个属性
			for (Map.Entry<String, Object> e : result.entrySet()) {
				String key = e.getKey();
				if (key.equals(Constant.ID)||key.equals(Constant.TABLE_ID)) {
					continue;
				}
				processField(table,key,rowId,e.getValue(),columnData);
			}
			return columnData;
		} else {
			return map2;
		}
	}
	
	/**
	 * 处理属性
	 * @param table
	 * @param key
	 * @param rowId
	 * @param value
	 * @param columnData
	 */
	private void processField(TableDefined table,String key,Integer rowId,Object value , BaseColumnData columnData){
		TableColumn tableColumn = tcService.loadById(new TableColumnKey(table, key));
		FieldType processor = tableColumn.getProcessor();
		ProcessorType type = processor.getType();
		String processId = processor.getProcessId();
		FieldProcessor bean = context.getBean(processId,FieldProcessor.class);
		Class<?> fieldType = null;
		Object realValue = null;
		if(type==ProcessorType.SIMPLE){
			realValue = bean.get(rowId, value, tableColumn);
			fieldType = bean.getType();
		}else{
			if(bean instanceof TableDefinedProcessor){
				((TableDefinedProcessor) bean).setTableId(processor.getTableDefinedId());
			}
			Relation relation = tableColumn.getRelation();
			ListableFieldProcessor<?> listable=(ListableFieldProcessor<?>) bean;
			String string = value.toString();
			if(Relation.ONE2MANY==relation){
				fieldType = List.class;
				realValue = listable.getOne2Many(string, tableColumn);
			}else if(Relation.MANY2MANY==relation){
				fieldType = List.class;
				realValue = listable.getOne2Many(string, tableColumn);
			}else if(Relation.MANY2ONE==relation){
				fieldType = bean.getType();
				realValue = bean.get(rowId, value, tableColumn);
			}
		
		}

		Class<? extends BaseColumnData> clazz = columnData.getClass();
		
		try {
			Method method = clazz.getMethod("set" +StringUtil.upperFrist(key), fieldType);
			method.invoke(columnData, realValue);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
//		result.put(key, realValue);
	}
	
	@Override
	public boolean isTransformedValueATupleElement(String[] aliases,
			int tupleLength) {
		return false;
	}

}
