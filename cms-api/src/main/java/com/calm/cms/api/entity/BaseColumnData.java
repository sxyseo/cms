package com.calm.cms.api.entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.calm.framework.util.StringUtil;

public class BaseColumnData {
	private Integer id_;
	private Integer tableId_;
	public Integer getId_() {
		return id_;
	}
	public void setId_(Integer id_) {
		this.id_ = id_;
	}
	public Integer getTableId_() {
		return tableId_;
	}
	public void setTableId_(Integer tableId_) {
		this.tableId_ = tableId_;
	}
	
	public Object get(String key){
		Class<? extends BaseColumnData> clazz = this.getClass();
		try {
			Method method = clazz.getMethod("get" +StringUtil.upperFrist(key));
			return method.invoke(this);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return null;
		}
	}
}
