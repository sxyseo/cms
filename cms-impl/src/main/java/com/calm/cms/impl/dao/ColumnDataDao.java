package com.calm.cms.impl.dao;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import com.calm.cms.api.dao.IColumnDataDao;
import com.calm.framework.common.dao.hibernate.HibernateBaseDao;

@Service
public class ColumnDataDao extends HibernateBaseDao implements IColumnDataDao {
	@Override
	public SQLQuery createSQLQuery(String sql) {
		Session session = getSession();
		return session.createSQLQuery(sql);
	}
}
