package com.calm.cms.impl.service;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;

import com.calm.cms.api.entity.ColumnDefined;
import com.calm.cms.api.entity.FieldType;
import com.calm.cms.api.service.IColumnDefinedService;
import com.calm.cms.api.service.IFieldTypeService;
import com.calm.framework.test.FrameworkTest;
@Deprecated
public class ColumnDefinedServiceTest extends FrameworkTest {
	@Resource
	private IColumnDefinedService columnDefinedService;
	@Resource
	private IFieldTypeService typeService;
	@Test
	public void testAdd() {
		Assert.assertNotNull(columnDefinedService);
		ColumnDefined cd = columnDefinedService.loadByProperty("name", "老师编号");
//		ColumnDefined cd = new ColumnDefined();
//		cd.setName("老师编号");
//		cd.setColumnName("LAO_SHI_BIAN_HAO");
		FieldType loadByProperty = typeService.loadByProperty("name", "老师");
		cd.setProcessor(loadByProperty);
		columnDefinedService.update(cd);
//		columnDefinedService.add(cd);
	}
}
