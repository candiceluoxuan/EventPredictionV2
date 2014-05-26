package com.luoxuan.prediction.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "weibos")
public class PersistentWeibo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4284789979407599974L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "pid")
	private Long pid;

	@Column(name = "id")
	private String id;

	@Column(name = "uid")
	private String uid;

	@Column(name = "date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@Column(name = "content")
	private String content;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "weibo_pid")
	private Set<Keyword> weiboKeywords = new HashSet<Keyword>();

	@Transient
	private List<String> keywords = new LinkedList<>();

	// private double[] vector;

	public String getId() {
		return id;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public Set<Keyword> getWeiboKeywords() {
		return weiboKeywords;
	}

	public void setWeiboKeywords(Set<Keyword> weiboKeywords) {
		this.weiboKeywords = weiboKeywords;
	}

	// public double[] getVector() {
	// return vector;
	// }
	//
	// public void setVector(double[] vector) {
	// this.vector = vector;
	// }

}
