package com.luoxuan.prediction.processor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luoxuan.prediction.domain.PersistentWeibo;
import com.luoxuan.prediction.utility.EncodingHelper;
import com.luoxuan.prediction.vector.VectorCalculator;

public class KeywordAbstracter extends SingleFolderLoader {

	Pattern textPattern = Pattern.compile("\"text\": \"(.*?)\"");
	Pattern idPattern = Pattern.compile("^(.*?)\t(.*?)\t");

	@Autowired
	@Qualifier("encodingHelper")
	EncodingHelper encodingHelper;

	@Autowired
	@Qualifier("keyWordComputer")
	KeyWordComputer keyWordComputer;

	@Autowired
	@Qualifier("word2VEC")
	VectorCalculator vectorCalculator;

	@Autowired
	@Qualifier("objectMapper")
	ObjectMapper objectMapper;

	PersistentWeibo weibo = new PersistentWeibo();

	public KeywordAbstracter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		execute(pathManager.getOriginalDataFolder());
	}

	@Override
	protected void process(File file) {
		File outputFile = new File(pathManager.getPreprocessedDataFolder()
				+ file.getName());

		try (BufferedReader input = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "gbk"));
				PrintWriter out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(
								new FileOutputStream(outputFile), "UTF-8")))) {

			String text;
			while ((text = input.readLine()) != null) {

				Matcher idMatcher = idPattern.matcher(text);
				if (idMatcher.find()) {
					weibo.setUid(idMatcher.group(1));
					weibo.setId(idMatcher.group(2));
				}

				Matcher textMatcher = textPattern.matcher(text);
				while (textMatcher.find()) {
					String content_unicode = textMatcher.group(1);
					String content = encodingHelper
							.unicodeToUTF8(content_unicode);

					// content
					weibo.setContent(content);
					// Keywords
					weibo.getKeywords().clear();
					Collection<Keyword> keywords = keyWordComputer
							.computeArticleTfidf(content);
					for (Keyword keyword : keywords) {
						weibo.getKeywords().add(keyword.getName());
					}

					// vector
//					weibo.setVector(vectorCalculator.getWordsVector(weibo
//							.getKeywords()));

					// out.println(JSON.toJSONString(weibo));
					out.println(objectMapper.writeValueAsString(weibo));
				}
			}
		} catch (IOException ioException) {
			System.err.println("File Error!");
		} finally {
			// System.out.println("File " + file.getName() + " complete!");
		}
	}

}
