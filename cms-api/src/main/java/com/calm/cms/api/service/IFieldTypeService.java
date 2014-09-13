package com.calm.cms.api.service;

import java.util.List;

import com.calm.cms.api.entity.FieldType;
import com.calm.cms.api.entity.ProcessorType;
import com.calm.framework.common.service.IBaseService;

import org.springframework.transaction.annotation.Transactional;
@Transactional
public interface IFieldTypeService extends IBaseService<Integer,FieldType> {

	List<FieldType> listByType(ProcessorType type,boolean normal);

}
