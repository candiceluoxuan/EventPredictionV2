package com.luoxuan.prediction.dao;

import java.util.List;

import com.luoxuan.prediction.domain.PersistentWeibo;

public interface WeiboDao {
	public void save(PersistentWeibo weibo);

	public List<PersistentWeibo> query(String sql,Object[] args);
}
