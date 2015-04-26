package com.calm.cms.web;

import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import com.calm.cms.api.service.IColumnDataService;
import com.calm.cms.api.service.ITableDefinedService;
import com.calm.framework.test.FrameworkTest;

public class TableDataActionTest extends FrameworkTest{
	@Resource
	private ITableDefinedService tableDefinedService;
	@Resource
	private IColumnDataService columnDataService ;
	@Test
	@Transactional
	public void testIndex() {
		assertNotNull(tableDefinedService);
//		TableDefined defined = tableDefinedService.loadById(2);
//		assertNotNull(defined);
//		try {
//			Class<?> clazz = com.calm.cms.api.compile.Compiler.getClass(defined);
//			Object newInstance = clazz.newInstance();
//			assertNotNull(newInstance);
//		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//			e.printStackTrace();
//		}
//		List<BaseColumnData> listAll = columnDataService.listAll(2);
//		assertNotNull(listAll);
	}

}
