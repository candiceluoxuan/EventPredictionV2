package com.luoxuan.prediction.processor;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.luoxuan.prediction.vector.TestWord2VEC;

public class TestCloneArray {

	Logger logger = LoggerFactory.getLogger(TestCloneArray.class);

	@Test
	public void test() {
		double[] a = new double[] { 0, 1, 2 };
		double[] b = a.clone();
		b[0] = 100;
		logger.debug("a:{}, b:{}", a, b);
	}

}
