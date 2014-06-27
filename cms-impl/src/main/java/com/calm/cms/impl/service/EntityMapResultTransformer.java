package com.calm.cms.impl.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.transform.AliasedTupleSubsetResultTransformer;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.calm.cms.api.entity.ColumnDefined;
import com.calm.cms.api.entity.FieldType;
import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.entity.TableColumnKey;
import com.calm.cms.api.entity.TableDefined;
import com.calm.cms.api.processor.FieldProcessor;
import com.calm.cms.api.service.IColumnDefinedService;
import com.calm.cms.api.service.ITableColumnService;
import com.calm.cms.api.service.ITableDefinedService;

@Component
public class EntityMapResultTransformer extends
		AliasedTupleSubsetResultTransformer {
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
	private IColumnDefinedService cdService;
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
			if (alias != null) {
				result.put(alias, tuple[i]);
			}
		}

		Integer id = (Integer) result.get("ID");
		Integer tableId = (Integer) result.get("TABLE_ID");
		Map<String, Map<String, Object>> map = cache.get();
		String cacheKey = tableId + ":" + id;
		Map<String, Object> map2 = map.get(cacheKey);
		if (map2 == null) {
			map.put(cacheKey, result);
			TableDefined table = tdService.loadById(tableId);
			for (Map.Entry<String, Object> e : result.entrySet()) {
				String key = e.getKey();
				if (key.equals("ID")) {
					continue;
				}
				if (key.equals("TABLE_ID")) {
					continue;
				}
				ColumnDefined column = cdService.loadByProperty("columnName",
						key);
				TableColumn tableColumn = tcService
						.loadById(new TableColumnKey(table, column));
				FieldType processor = column.getProcessor();
				FieldProcessor fieldProcessor = ProcessorUtils
						.getFieldProcessor(processor.getId(), context);
				Object realValue = fieldProcessor.get(id, e.getValue(),
						tableColumn);
				result.put(key, realValue);
			}
			return result;
		} else {
			return map2;
		}

	}

	@Override
	public boolean isTransformedValueATupleElement(String[] aliases,
			int tupleLength) {
		return false;
	}

}
