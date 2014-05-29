package com.luoxuan.prediction.global;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/application-config.xml")
public class TestPathManager {

	@Autowired
	@Qualifier("pathManager")
	PathManager pathManager;

	@Test
	public void testGetRootPath() {
		System.out.println(pathManager.getRootPath());
		System.out.println(pathManager.getOriginalDataFolder());
		System.out.println(pathManager.getResourcesFolder());
		System.out.println(pathManager.getCorpus());
		System.out.println(pathManager.getVectors());
		System.out.println(pathManager.getWeibos());
		System.out.println(pathManager.getSortedWeibos());
		System.out.println(pathManager.getClusters());
		System.out.println(pathManager.getPreprocessedDataFolder());
		System.out.println(pathManager.getResultsFolder());
	}

}
