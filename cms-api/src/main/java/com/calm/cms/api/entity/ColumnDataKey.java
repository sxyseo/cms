package com.calm.cms.api.entity;

import javax.persistence.*;
import java.io.Serializable;
/**
 * 列数据联合主键
 * @author dingqihui
 *
 */
@Embeddable
public class ColumnDataKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**主键*/
	@Column(name="ID")
	private Integer id;
	/**所属列*/
	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "TABLE_ID",referencedColumnName="TABLE_ID"),
			@JoinColumn(name = "COLUMN_ID",referencedColumnName="ID") })
	private TableColumn tableColumn;

	public TableColumn getTableColumn() {
		return tableColumn;
	}

	public void setTableColumn(TableColumn tableColumn) {
		this.tableColumn = tableColumn;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
