package com.luoxuan.prediction.processor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.FilterModifWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.luoxuan.prediction.utility.EncodingHelper;

public class OriginalDataSplitter extends SingleFolderLoader {

	Pattern textPattern = Pattern.compile("\"text\": \"(.*?)\"");

	@Autowired
	@Qualifier("encodingHelper")
	EncodingHelper encodingHelper;

	public OriginalDataSplitter() {
		initializeStopWord();
	}

	private void initializeStopWord() {
		try (BufferedReader input = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File("library/HIT_stopword.dic")),
				"gbk"))) {
			String text;
			while ((text = input.readLine()) != null) {
				FilterModifWord.insertStopWord(text);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void execute() {
		execute(pathManager.getOriginalDataFolder());
	}

	@Override
	protected void process(File file) {
		File outputFile = new File(pathManager.getCorpus());

		try (BufferedReader input = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "gbk"));
				PrintWriter out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(outputFile,
								true), "UTF-8")))) {

			String text;
			while ((text = input.readLine()) != null) {
				Matcher matcher = textPattern.matcher(text);
				while (matcher != null && matcher.find()) {
					String content_unicode = matcher.group(1);
					String content = encodingHelper
							.unicodeToUTF8(content_unicode);
					for (Term term : split(content)) {
						out.print(term.getName());
						out.print(" ");
					}
					out.println();
				}
			}
		} catch (IOException ioException) {
			System.err.println("File Error!");
		} finally {
			System.out.println("Splitting " + file.getName() + " Complete!");
		}
	}

	protected List<Term> split(String words) {
		List<Term> parse = ToAnalysis.parse(words);
		return FilterModifWord.modifResult(parse);
	}
}
