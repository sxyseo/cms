package com.calm.cms.impl.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.transform.AliasedTupleSubsetResultTransformer;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.calm.cms.api.entity.FieldType;
import com.calm.cms.api.entity.ProcessorType;
import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.entity.TableColumnKey;
import com.calm.cms.api.entity.TableDefined;
import com.calm.cms.api.processor.FieldProcessor;
import com.calm.cms.api.service.ITableColumnService;
import com.calm.cms.api.service.ITableDefinedService;
import com.calm.cms.impl.processor.TableDefinedProcessor;

@Component
public class EntityMapResultTransformer extends
		AliasedTupleSubsetResultTransformer {
	public static final String TABLE_ID ="TABLE_ID";
	public static final String ID ="ID";
	private ThreadLocal<Map<String, Map<String, Object>>> cache = new ThreadLocal<Map<String, Map<String, Object>>>() {
		@Override
		protected Map<String, Map<String, Object>> initialValue() {
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
		Map<String, Object> result = new HashMap<>(tuple.length);
		for (int i = 0; i < tuple.length; i++) {
			String alias = aliases[i];
			if (alias == null) {
				continue;
			}
			result.put(alias, tuple[i]);
		}

		Integer id = (Integer) result.get(ID);
		Integer tableId = (Integer) result.get(TABLE_ID);
		Map<String, Map<String, Object>> map = cache.get();
		String cacheKey = tableId + ":" + id;
		Map<String, Object> map2 = map.get(cacheKey);
		if (map2 == null) {
			map.put(cacheKey, result);
			TableDefined table = tdService.loadById(tableId);
			for (Map.Entry<String, Object> e : result.entrySet()) {
				String key = e.getKey();
				if (key.equals(ID)||key.equals(TABLE_ID)) {
					continue;
				}
				processField(table,key,id,e.getValue(),result);
			}
			return result;
		} else {
			return map2;
		}
	}
	
	private void processField(TableDefined table,String key,Integer id,Object value ,Map<String, Object> result){
		TableColumn tableColumn = tcService.loadById(new TableColumnKey(table, key));
		FieldType processor = tableColumn.getProcessor();
		ProcessorType type = processor.getType();
		String processId = processor.getProcessId();
		FieldProcessor<?> bean = context.getBean(processId,FieldProcessor.class);
		if (type == ProcessorType.TABLE && bean instanceof TableDefinedProcessor) {
			((TableDefinedProcessor) bean).setTableId(processor
					.getTableDefinedId());
		}
		Object realValue = bean.get(id, value, tableColumn);
		result.put(key, realValue);
	}
	
	@Override
	public boolean isTransformedValueATupleElement(String[] aliases,
			int tupleLength) {
		return false;
	}

}
