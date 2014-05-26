package com.luoxuan.prediction.vector;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/application-config.xml")
public class TestWord2VEC {

	Logger logger = LoggerFactory.getLogger(TestWord2VEC.class);

	@Autowired
	@Qualifier("word2VEC")
	VectorCalculator vectorCalculator;

	@Test
	public void testGetWordVector() {
		logger.debug("淘宝:{}", vectorCalculator.getWordVector("淘宝"));
		logger.debug("乐宝:{}", vectorCalculator.getWordVector("乐宝"));
		logger.debug("众安:{}", vectorCalculator.getWordVector("众安"));
	}

	@Test
	public void testGetWordsVector() {
		logger.debug("[淘宝]:{}", vectorCalculator.getWordsVector(new ArrayList<String>(
				Arrays.asList(new String[] { "淘宝", "乐宝", "众安" }))));
		logger.debug("JSON: {}", JSON.toJSONString(vectorCalculator.getWordsVector(new ArrayList<String>(
				Arrays.asList(new String[] { "淘宝", "乐宝", "众安" })))));
	}

}
