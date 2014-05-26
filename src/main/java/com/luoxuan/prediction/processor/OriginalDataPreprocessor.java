package com.luoxuan.prediction.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.luoxuan.prediction.domain.PersistentWeibo;
import com.luoxuan.prediction.service.WeiboService;
import com.luoxuan.prediction.utility.WeiboJsonAnalyzer;

public class OriginalDataPreprocessor extends SingleFolderLoader {

	@Autowired
	@Qualifier("weiboJsonAnalyzer")
	WeiboJsonAnalyzer weiboJsonAnalyzer;

	@Autowired
	@Qualifier("keyWordComputer")
	KeyWordComputer keyWordComputer;

	@Autowired
	@Qualifier("weiboServiceImpl")
	WeiboService weiboService;

	@Override
	public void execute() {
		execute(pathManager.getOriginalDataFolder());
	}

	@Override
	protected void process(File file) {
		try (BufferedReader input = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "gbk"))) {

			String text;
			while ((text = input.readLine()) != null) {
				String json = null;
				if ((json = weiboJsonAnalyzer.extractJson(text)) != null) {
					List<PersistentWeibo> weibos = weiboJsonAnalyzer
							.execute(json);
					for (PersistentWeibo weibo : weibos) {
						// System.out.println(weibo.getContent());

						// Keywords
						weibo.getKeywords2().clear();
						Collection<Keyword> keywords = keyWordComputer
								.computeArticleTfidf(weibo.getContent());
						for (Keyword keyword : keywords) {
							weibo.getKeywords2().add(
									new com.luoxuan.prediction.domain.Keyword(
											keyword.getName()));
						}

						weiboService.saveWeibo(weibo);
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
