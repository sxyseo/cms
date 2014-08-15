package com.calm.cms.impl.service;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.calm.cms.api.entity.ColumnData;
import com.calm.cms.api.entity.ColumnDataKey;
import com.calm.cms.api.entity.ColumnDefined;
import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.entity.TableDefined;
import com.calm.cms.api.service.IColumnDataService;
import com.calm.cms.api.service.IColumnDefinedService;
import com.calm.cms.api.service.ITableColumnService;
import com.calm.cms.api.service.ITableDefinedService;
import com.calm.framework.test.FrameworkTest;

public class ColumnDataServiceTest extends FrameworkTest {
	@Resource
	private IColumnDataService columnDataService;
	@Resource
	private ITableColumnService tcService;
	@Resource
	private IColumnDefinedService cdService;
	@Resource
	private ITableDefinedService tdService;

	@Test
	public void testListAllInteger() {
		assertNotNull(columnDataService);
		ColumnData cd = new ColumnData();
		ColumnDataKey cdk = new ColumnDataKey();
		cdk.setId(1);
		TableDefined table = tdService.loadByProperty("name", "老师_学生");
		ColumnDefined column = cdService.loadByProperty("columnName", "XUE_SHENG_BIAN_HAO");

		cdk.setTableColumn(new TableColumn(table, column));
		cd.setId(cdk);
		cd.setValueText("1");
		columnDataService.add(cd);
		
		List<?> listAll = columnDataService.listAll(1);
		System.out.println(listAll);
	}
}
