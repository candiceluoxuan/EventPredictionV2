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
import java.util.Collection;
import java.util.List;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luoxuan.prediction.domain.PreprocessedWeibo;
import com.luoxuan.prediction.utility.WeiboJsonAnalyzer;

public class OriginalDataPreprocessorToFile extends SingleFolderLoader {

	@Autowired
	@Qualifier("weiboJsonAnalyzer")
	WeiboJsonAnalyzer weiboJsonAnalyzer;

	@Autowired
	@Qualifier("keyWordComputer")
	KeyWordComputer keyWordComputer;

	@Autowired
	@Qualifier("objectMapper")
	ObjectMapper objectMapper;

	@Override
	public void execute() {
		execute(pathManager.getOriginalDataFolder());
	}

	@Override
	protected void process(File file) {
		File outputFile = new File(pathManager.getWeibos());

		try (BufferedReader input = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "gbk"));
				PrintWriter out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(outputFile,
								true), "UTF-8")))) {

			String text;
			while ((text = input.readLine()) != null) {
				String json = null;
				if ((json = weiboJsonAnalyzer.extractJson(text)) != null) {
					List<PreprocessedWeibo> weibos = weiboJsonAnalyzer
							.executePreprocessedWeibo(json);
					for (PreprocessedWeibo weibo : weibos) {
						// System.out.println(weibo.getContent());

						// Keywords
						weibo.getKeywords().clear();
						Collection<Keyword> keywords = keyWordComputer
								.computeArticleTfidf(weibo.getContent());
						for (Keyword keyword : keywords) {
							weibo.getKeywords().add(keyword.getName());
						}

						out.println(objectMapper.writeValueAsString(weibo));
					}
				}
			}
		} catch (IOException ioException) {
			System.err.println("File Error!");
		} finally {
			System.out.println("Parse " + file.getName() + " Complete!");
		}
	}

}
