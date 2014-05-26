package com.luoxuan.prediction.processor;

import moa.clusterers.denstream.WithDBSCAN;

public class LUOClusterer extends WithDBSCAN {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7878554972609535501L;

	@Override
	protected double distance(double[] pointA, double[] pointB) {
		double distance = 0.0;
		for (int i = 0; i < pointA.length; i++) {
			distance += pointA[i] * pointB[i];
		}
		return 1 - distance;
	}
}
