package com.calm.cms.api.dao;

import com.calm.framework.common.dao.IBaseDao;
import org.hibernate.SQLQuery;

public interface IColumnDataDao extends IBaseDao{

	SQLQuery createSQLQuery(String sql);

}
