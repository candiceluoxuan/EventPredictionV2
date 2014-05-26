package com.luoxuan.prediction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.luoxuan.prediction.dao.WeiboDao;
import com.luoxuan.prediction.domain.PersistentWeibo;

@Transactional
public class WeiboServiceImpl implements WeiboService {

//	@Autowired
//	@Qualifier("weiboDaoImpl")
	private WeiboDao weiboDao;

	public WeiboDao getWeiboDao() {
		return weiboDao;
	}

	public void setWeiboDao(WeiboDao weiboDao) {
		this.weiboDao = weiboDao;
	}

	@Override
	public void saveWeibo(PersistentWeibo weibo) {
		weiboDao.save(weibo);
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public void saveWeiboThrowException(PersistentWeibo weibo) throws Exception {
		weiboDao.save(weibo);
	}

}
