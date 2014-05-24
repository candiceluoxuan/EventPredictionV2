package com.luoxuan.prediction.vector;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

public class Word2VEC implements VectorCalculator {

	private HashMap<String, double[]> wordMap = new HashMap<String, double[]>();

	private int words;
	private int size;
	private int topNSize = 40;

	public Word2VEC() {

	}

	public Word2VEC(String vectorFilePath) throws IOException {
		this.loadGoogleModel(vectorFilePath);
	}

	/**
	 * 加载模型
	 * 
	 * @param path
	 *            模型的路径
	 * @throws IOException
	 */
	public void loadGoogleModel(String path) throws IOException {
		DataInputStream dis = null;
		BufferedInputStream bis = null;
		double len = 0;
		double vector = 0;
		try {
			bis = new BufferedInputStream(new FileInputStream(path));
			dis = new DataInputStream(bis);
			// //读取词数
			words = Integer.parseInt(readString(dis));
			// //大小
			size = Integer.parseInt(readString(dis));
			String word;
			double[] vectors = null;
			for (int i = 0; i < words; i++) {
				word = readString(dis);
				vectors = new double[size];
				len = 0;
				for (int j = 0; j < size; j++) {
					vector = readFloat(dis);
					len += vector * vector;
					vectors[j] = vector;
				}
				len = Math.sqrt(len);

				for (int j = 0; j < size; j++) {
					vectors[j] /= len;
				}

				wordMap.put(word, vectors);
				dis.read();
			}
		} finally {
			bis.close();
			dis.close();
		}
	}

	/**
	 * 加载模型
	 * 
	 * @param path
	 *            模型的路径
	 * @throws IOException
	 */
	public void loadJavaModel(String path) throws IOException {
		try (DataInputStream dis = new DataInputStream(new BufferedInputStream(
				new FileInputStream(path)))) {
			words = dis.readInt();
			size = dis.readInt();

			double vector = 0;

			String key = null;
			double[] value = null;
			for (int i = 0; i < words; i++) {
				double len = 0;
				key = dis.readUTF();
				value = new double[size];
				for (int j = 0; j < size; j++) {
					vector = dis.readFloat();
					len += vector * vector;
					value[j] = vector;
				}

				len = Math.sqrt(len);

				for (int j = 0; j < size; j++) {
					value[j] /= len;
				}
				wordMap.put(key, value);
			}

		}
	}

	private static final int MAX_SIZE = 50;

	private void insertTopN(String name, double score,
			List<WordEntry> wordsEntrys) {
		// TODO Auto-generated method stub
		if (wordsEntrys.size() < topNSize) {
			wordsEntrys.add(new WordEntry(name, score));
			return;
		}
		double min = Double.MAX_VALUE;
		int minOffe = 0;
		for (int i = 0; i < topNSize; i++) {
			WordEntry wordEntry = wordsEntrys.get(i);
			if (min > wordEntry.score) {
				min = wordEntry.score;
				minOffe = i;
			}
		}

		if (score > min) {
			wordsEntrys.set(minOffe, new WordEntry(name, score));
		}

	}

	@Override
	public Set<WordEntry> distance(String queryWord) {
		double[] center = wordMap.get(queryWord);
		if (center == null) {
			return Collections.emptySet();
		}

		int resultSize = wordMap.size() < topNSize ? wordMap.size() : topNSize;
		TreeSet<WordEntry> result = new TreeSet<WordEntry>();

		double min = Double.MIN_VALUE;
		for (Map.Entry<String, double[]> entry : wordMap.entrySet()) {
			double[] vector = entry.getValue();
			double dist = 0;
			for (int i = 0; i < vector.length; i++) {
				dist += center[i] * vector[i];
			}

			if (dist > min) {
				result.add(new WordEntry(entry.getKey(), dist));
				if (resultSize < result.size()) {
					result.pollLast();
				}
				min = result.last().score;
			}
		}
		result.pollFirst();

		return result;
	}

	@Override
	public Set<WordEntry> distance(List<String> words) {
		double[] wordsVector = getWordsVector(words);
		if (wordsVector == null) {
			return null;
		}
		double[] tempVector;
		String name;
		List<WordEntry> wordEntrys = new ArrayList<WordEntry>(topNSize);
		keyword: for (Entry<String, double[]> entry : wordMap.entrySet()) {
			name = entry.getKey();
			for (String word : words) {
				if (name.equals(word)) {
					continue keyword;
				}
			}
			double dist = 0;
			tempVector = entry.getValue();
			for (int i = 0; i < wordsVector.length; i++) {
				dist += wordsVector[i] * tempVector[i];
			}
			insertTopN(name, dist, wordEntrys);
		}
		return new TreeSet<WordEntry>(wordEntrys);
	}

	@Override
	public double[] getWordVector(String word) {
		return wordMap.get(word);
	}

	@Override
	public double[] getWordsVector(List<String> words) {
		if (words == null)
			return null;
		double[] wordsVector = null;
		for (String word : words) {
			double[] wordVector = getWordVector(word);
			if (wordVector != null) {
				if (wordsVector == null) {
					wordsVector = wordVector;
				} else {
					for (int i = 0; i < size; i++) {
						wordsVector[i] += wordVector[i];
					}
				}
			}
		}
		return wordsVector;
	}

	@Override
	public double distance(double[] v1, double[] v2) {
		double distance = 0.0;
		for (int i = 0; i < v1.length; i++) {
			distance += v1[i] * v2[i];
		}
		return distance;
	}

	public static float readFloat(InputStream is) throws IOException {
		byte[] bytes = new byte[4];
		is.read(bytes);
		return getFloat(bytes);
	}

	/**
	 * 读取一个float
	 * 
	 * @param b
	 * @return
	 */
	public static float getFloat(byte[] b) {
		int accum = 0;
		accum = accum | (b[0] & 0xff) << 0;
		accum = accum | (b[1] & 0xff) << 8;
		accum = accum | (b[2] & 0xff) << 16;
		accum = accum | (b[3] & 0xff) << 24;
		return Float.intBitsToFloat(accum);
	}

	/**
	 * 读取一个字符串
	 * 
	 * @param dis
	 * @return
	 * @throws IOException
	 */
	private static String readString(DataInputStream dis) throws IOException {
		// TODO Auto-generated method stub
		byte[] bytes = new byte[MAX_SIZE];
		byte b = dis.readByte();
		int i = -1;
		StringBuilder sb = new StringBuilder();
		while (b != 32 && b != 10) {
			i++;
			bytes[i] = b;
			b = dis.readByte();
			if (i == 49) {
				sb.append(new String(bytes));
				i = -1;
				bytes = new byte[MAX_SIZE];
			}
		}
		sb.append(new String(bytes, 0, i + 1));
		return sb.toString();
	}

	public int getTopNSize() {
		return topNSize;
	}

	public void setTopNSize(int topNSize) {
		this.topNSize = topNSize;
	}

	public HashMap<String, double[]> getWordMap() {
		return wordMap;
	}

	public int getWords() {
		return words;
	}

	public int getSize() {
		return size;
	}
}
