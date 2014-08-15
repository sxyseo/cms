package com.calm.cms.impl.service;

import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.junit.Test;

import com.calm.cms.api.entity.ColumnDefined;
import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.entity.TableDefined;
import com.calm.cms.api.service.IColumnDefinedService;
import com.calm.cms.api.service.ITableColumnService;
import com.calm.cms.api.service.ITableDefinedService;
import com.calm.framework.test.FrameworkTest;

public class TableColumnServiceTest extends FrameworkTest {
	@Resource
	private ITableColumnService tableColumnService;
	@Resource
	private IColumnDefinedService columnDefinedService;
	@Resource
	private ITableDefinedService tableDefinedService;

	@Test
	public void testAdd() {
		assertNotNull(tableColumnService);
		TableDefined table = tableDefinedService.loadByProperty("name", "老师_学生");
		ColumnDefined column = columnDefinedService.loadByProperty(
				"columnName", "LAO_SHI_BIAN_HAO");
		TableColumn tc = new TableColumn(table, column);
		tableColumnService.add(tc);
		tableDefinedService.updateSqlText(table);
	}
}
