package com.luoxuan.prediction.processor;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

public class TestDateFormatter {

	@Test
	public void test() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);
//		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = dateFormat.parse("Wed Jul 09 11:12:06 +0800 2013");
//			Date birthday = dateFormat.parse("2010-5-27");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
