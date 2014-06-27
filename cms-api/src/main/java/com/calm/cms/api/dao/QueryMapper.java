package com.calm.cms.api.dao;

import org.hibernate.SQLQuery;

public interface QueryMapper {

	String getSql();

	void setParameter(SQLQuery createSQLQuery);

}
