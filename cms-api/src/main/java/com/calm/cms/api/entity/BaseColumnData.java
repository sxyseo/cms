package com.calm.cms.api.entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.calm.framework.common.entity.BaseEntity;
import com.calm.framework.util.StringUtil;

public abstract class BaseColumnData implements BaseEntity<BaseColumnDataKey>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BaseColumnDataKey id;
	
	public Object get(String key){
		Class<? extends BaseColumnData> clazz = this.getClass();
		try {
			Method method = clazz.getMethod("get" +StringUtil.upperFrist(key));
			return method.invoke(this);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return null;
		}
	}

	public BaseColumnDataKey getId() {
		return id;
	}

	public void setId(BaseColumnDataKey id) {
		this.id = id;
	}

	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public Object getDisplayValue() {
		return null;
	}
	public Object getProxyId(){
		if(id==null){
			return null;
		}
		return id.getId();
	}
}
