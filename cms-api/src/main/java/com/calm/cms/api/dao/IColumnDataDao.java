package com.calm.cms.api.dao;

import org.hibernate.SQLQuery;

import com.calm.framework.common.dao.IBaseDao;

public interface IColumnDataDao extends IBaseDao{

	SQLQuery createSQLQuery(String sql);

}
