package com.luoxuan.prediction.domain;

import moa.cluster.Cluster;
import weka.core.Instance;

public class QuantifiedWeibo extends PersistentWeibo {

	private double[] vector;
	private Instance instance;
	private Cluster cluster;

	public double[] getVector() {
		return vector;
	}

	public void setVector(double[] vector) {
		this.vector = vector;
	}

	public Instance getInstance() {
		return instance;
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
	}

	public Cluster getCluster() {
		return cluster;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

}
