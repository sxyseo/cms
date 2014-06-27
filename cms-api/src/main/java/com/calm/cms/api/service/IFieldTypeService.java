package com.calm.cms.api.service;

import org.springframework.transaction.annotation.Transactional;

import com.calm.cms.api.entity.FieldType;
import com.calm.framework.common.service.IBaseService;
@Transactional
public interface IFieldTypeService extends IBaseService<FieldType> {

}
