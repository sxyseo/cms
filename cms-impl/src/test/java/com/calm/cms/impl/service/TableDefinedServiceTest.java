package com.calm.cms.impl.service;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.calm.cms.api.entity.TableDefined;
import com.calm.cms.api.service.ITableDefinedService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:META-INF/spring/applicationContext*.xml")
public class TableDefinedServiceTest {
	@Resource
	private ITableDefinedService definedService;

	@Test
	public void testAdd() {
		TableDefined td = new TableDefined();
		td.setName("老师");
		assertNotNull(definedService);
		definedService.add(td);
		td = new TableDefined();
		td.setName("老师_学生");
		assertNotNull(definedService);
		definedService.add(td);
		td = new TableDefined();
		td.setName("学生");
		assertNotNull(definedService);
		definedService.add(td);
	}

}
