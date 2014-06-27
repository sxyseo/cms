package com.calm.cms.impl.service;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;

import com.calm.cms.api.entity.ColumnDefined;
import com.calm.cms.api.service.IColumnDefinedService;
import com.calm.framework.test.FrameworkTest;

public class ColumnDefinedServiceTest extends FrameworkTest {
	@Resource
	private IColumnDefinedService columnDefinedService;

	@Test
	public void testAdd() {
		Assert.assertNotNull(columnDefinedService);
		ColumnDefined cd = new ColumnDefined();
		cd.setName("老师编号");
		cd.setColumnName("LAO_SHI_BIAN_HAO");
//		cd.setProcessor("integerProcessor");
		columnDefinedService.add(cd);
	}
}
