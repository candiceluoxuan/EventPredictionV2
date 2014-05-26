package com.luoxuan.prediction.service;

import com.luoxuan.prediction.domain.PersistentWeibo;

public interface WeiboService {

	void saveWeibo(PersistentWeibo weibo);

	void saveWeiboThrowException(PersistentWeibo weibo) throws Exception;
}
