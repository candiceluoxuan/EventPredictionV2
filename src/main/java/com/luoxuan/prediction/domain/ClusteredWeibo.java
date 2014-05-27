package com.luoxuan.prediction.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import weka.core.Instance;

public class ClusteredWeibo extends PreprocessedWeibo {

	@JsonIgnore
	private Instance instance;

	public Instance getInstance() {
		return instance;
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
	}

	public ClusteredWeibo(PreprocessedWeibo preprocessedWeibo) {
		setId(preprocessedWeibo.getId());
		setUid(preprocessedWeibo.getUid());
		setDate(preprocessedWeibo.getDate());
		setContent(preprocessedWeibo.getContent());
		setKeywords(preprocessedWeibo.getKeywords());
		setFile(preprocessedWeibo.getFile());
	}
}
