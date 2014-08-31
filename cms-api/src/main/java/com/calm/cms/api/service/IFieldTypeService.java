package com.calm.cms.api.service;

import com.calm.cms.api.entity.FieldType;
import com.calm.framework.common.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;
@Transactional
public interface IFieldTypeService extends IBaseService<Integer,FieldType> {

}
