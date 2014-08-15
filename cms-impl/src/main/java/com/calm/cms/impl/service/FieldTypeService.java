package com.calm.cms.impl.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calm.cms.api.entity.FieldType;
import com.calm.cms.api.service.IFieldTypeService;
import com.calm.framework.common.service.impl.BaseService;

@Service
@Transactional
public class FieldTypeService extends BaseService<Integer,FieldType> implements
		IFieldTypeService {

	@Override
	public Class<FieldType> getEntityClass() {
		return FieldType.class;
	}

}
