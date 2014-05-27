package com.luoxuan.prediction.processor;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.externalsorting.ExternalSort;
import com.luoxuan.prediction.global.PathManager;

public class PreprocessedWeiboSorter {

	Pattern datePattern = Pattern.compile("\"date\":(\\d+)");

	@Autowired
	@Qualifier("pathManager")
	protected PathManager pathManager;

	@Autowired
	@Qualifier("objectMapper")
	ObjectMapper objectMapper;

	Comparator<String> cmp = new Comparator<String>() {
		@Override
		public int compare(String o1, String o2) {
			// try {
			// PreprocessedWeibo weibo1 = objectMapper.readValue(o1,
			// PreprocessedWeibo.class);
			// PreprocessedWeibo weibo2 = objectMapper.readValue(o2,
			// PreprocessedWeibo.class);
			// return weibo1.getDate().compareTo(weibo2.getDate());
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// return 0;

			Matcher m1 = datePattern.matcher(o1);
			Matcher m2 = datePattern.matcher(o2);
			if (m1.find() && m2.find()) {
				Long date1 = Long.parseLong(m1.group(1));
				Long date2 = Long.parseLong(m2.group(1));
				return date1.compareTo(date2);
			}
			return 0;
		}
	};

	public void execute() {
		try {
			System.out.println("Begin external sorting: " + Calendar.getInstance().getTime().toString());
			List<File> temps = ExternalSort.sortInBatch(
					new File(pathManager.getWeibos()), cmp);

			System.out.println("Begin merge sorted files: " + Calendar.getInstance().getTime().toString());
			ExternalSort.mergeSortedFiles(temps,
					new File(pathManager.getSortedWeibos()), cmp);

			System.out.println("External sorting finish: " + Calendar.getInstance().getTime().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
