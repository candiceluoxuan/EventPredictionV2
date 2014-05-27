package com.luoxuan.prediction.processor;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/application-config.xml")
public class TestOriginalDataPreprocessorToFile {

	@Autowired
	@Qualifier("originalDataPreprocessorToFile")
	OriginalDataPreprocessorToFile originalDataPreprocessorToFile;
	
	@Autowired
	@Qualifier("preprocessedWeiboSorter")
	PreprocessedWeiboSorter preprocessedWeiboSorter;
	
	@Test
	public void testExecute() {
		originalDataPreprocessorToFile.execute();
		preprocessedWeiboSorter.execute();
	}

}
