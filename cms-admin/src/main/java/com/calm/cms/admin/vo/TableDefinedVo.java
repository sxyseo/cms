package com.calm.cms.admin.vo;

import java.util.List;

import com.calm.cms.api.entity.ColumnDefined;
import com.calm.cms.api.entity.TableDefined;

public class TableDefinedVo extends TableDefined{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ColumnDefined> cds;

	public List<ColumnDefined> getCds() {
		return cds;
	}

	public void setCds(List<ColumnDefined> cds) {
		this.cds = cds;
	}
}
