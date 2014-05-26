package com.luoxuan.prediction.dao;

import com.luoxuan.prediction.domain.PersistentWeibo;

public class WeiboDaoImpl extends AbstractHibernateDao<PersistentWeibo>
		implements WeiboDao {

	public WeiboDaoImpl() {
		super(PersistentWeibo.class);
	}

}
