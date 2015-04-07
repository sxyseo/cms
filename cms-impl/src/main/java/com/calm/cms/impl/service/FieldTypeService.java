package com.calm.cms.impl.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calm.cms.api.entity.FieldType;
import com.calm.cms.api.entity.ProcessorType;
import com.calm.cms.api.service.IFieldTypeService;
import com.calm.framework.common.dao.Query;
import com.calm.framework.common.service.impl.BaseService;

/**
 * 属性处理器
 * @author dingqihui
 *
 */
@Service
@Transactional
public class FieldTypeService extends BaseService<Integer,FieldType> implements
		IFieldTypeService {

	/* (non-Javadoc)
	 * @see com.calm.framework.common.service.IBaseService#getEntityClass()
	 */
	@Override
	public Class<FieldType> getEntityClass() {
		return FieldType.class;
	}

	/* (non-Javadoc)
	 * @see com.calm.cms.api.service.IFieldTypeService#listByType(com.calm.cms.api.entity.ProcessorType, boolean)
	 */
	@Override
	public List<FieldType> listByType(ProcessorType type,boolean normal) {
		Query<Integer,FieldType> createQuery = createQuery();
		createQuery.eq("type", type);
		if(normal){
			createQuery.normal();
		}
		createQuery.asc("type");
		return createQuery.list();
	}

}
