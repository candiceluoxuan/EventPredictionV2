package com.luoxuan.prediction.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "keywords")
public class Keyword implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8566147549297477784L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "pid")
	private Long pid;

	@Column(name = "keyword")
	private String keyword;

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Keyword() {

	}

	public Keyword(String keyword) {
		setKeyword(keyword);
	}
}
