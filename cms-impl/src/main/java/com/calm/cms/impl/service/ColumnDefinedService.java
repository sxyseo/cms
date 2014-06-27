package com.calm.cms.impl.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.calm.cms.api.entity.ColumnDefined;
import com.calm.cms.api.service.IColumnDefinedService;
import com.calm.framework.common.dao.Query;
import com.calm.framework.common.exception.EntityAlreadyExistException;
import com.calm.framework.common.service.impl.BaseService;
import com.calm.framework.util.BeanUtils;

@Service
public class ColumnDefinedService extends BaseService<ColumnDefined> implements
		IColumnDefinedService {

	@Override
	public Class<ColumnDefined> getEntityClass() {
		return ColumnDefined.class;
	}
	@Override
	protected void preUpdate(ColumnDefined dbEentity, ColumnDefined newEntity) {
		BeanUtils.copyProperties(newEntity, dbEentity,"id");
	}
	@Override
	protected void preAdd(ColumnDefined newEntity) {
		Query<ColumnDefined> query = createQuery();
		query.eq("name", newEntity.getName());
		List<ColumnDefined> list = query.list();
		if (list.size() > 0) {
			throw new EntityAlreadyExistException(newEntity);
		}
	}
}
