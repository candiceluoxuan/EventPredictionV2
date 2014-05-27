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
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import moa.cluster.Cluster;
import moa.core.AutoExpandVector;
import moa.options.FloatOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luoxuan.prediction.domain.ClusteredWeibo;
import com.luoxuan.prediction.domain.PreprocessedWeibo;
import com.luoxuan.prediction.global.PathManager;
import com.luoxuan.prediction.vector.VectorCalculator;

public class WeiboClassifier {

	@Autowired
	@Qualifier("word2VEC")
	VectorCalculator vectorCalculator;

	@Autowired
	@Qualifier("pathManager")
	protected PathManager pathManager;

	@Autowired
	@Qualifier("objectMapper")
	ObjectMapper objectMapper;

	LUOClusterer luoClusterer;
	private float eps = 0.02f;
	private int minPoints = 2;
	private int initPoints = 1000;
	private float epsMin = 0f;
	private float epsMax = 2f;

	private int dimention = 201;
	private Instances instances;

	int i = 5000;

	Queue<ClusteredWeibo> weiboQueue = new LinkedBlockingQueue<ClusteredWeibo>(
			initPoints);

	public WeiboClassifier() {
		initClusterer();
	}

	protected void initClusterer() {
		luoClusterer = new LUOClusterer();
		luoClusterer.epsilonOption = new FloatOption("epsilon", 'e',
				"Defines the epsilon neighbourhood", eps, epsMin, epsMax);
		luoClusterer.muOption.setValue(minPoints);
		// 50 samples
		// 0.07, 1 -> 25 clusters
		luoClusterer.initPointsOption.setValue(initPoints);
		luoClusterer.prepareForUse();

		ArrayList<Attribute> attributes = new ArrayList<Attribute>(dimention);
		for (int i = 1; i <= dimention; i++) {
			attributes.add(new Attribute("Dimension" + i));
		}
		instances = new Instances("vector", attributes, 0);
		instances.setClassIndex(dimention - 1);
	}

	public void execute() {
		File inputFile = new File(pathManager.getSortedWeibos());
		try (BufferedReader input = new BufferedReader(new InputStreamReader(
				new FileInputStream(inputFile), "UTF-8"))) {

			String text;
			while ((text = input.readLine()) != null) {
				PreprocessedWeibo preprocessedWeibo = objectMapper.readValue(
						text, PreprocessedWeibo.class);
				if (preprocessedWeibo != null) {
					ClusteredWeibo clusteredWeibo = new ClusteredWeibo(
							preprocessedWeibo);
					trainCluster(clusteredWeibo);

					// put the weibo into a queue for later classify
					boolean notFull = weiboQueue.offer(clusteredWeibo);
					// if the queue is full, classify the queue
					if (!notFull) {
						// System.out.println(weiboQueue.size());
						classify();
						weiboQueue.offer(clusteredWeibo);
					}

					// if (i-- == 0) {
					// break;
					// }
				}
			}

			classify();
		} catch (IOException ioException) {
			System.err.println("File Error!");
		} finally {
			// System.out.println("File " + file.getName() + " complete!");
		}
	}

	protected double[] initializeVector(double[] vector) {
		if (vector != null) {
			int size = vector.length;
			double[] result = new double[size + 1];
			for (int i = 0; i < size; i++) {
				result[i] = vector[i];
			}
			return result;
		}

		return vector;
	}

	protected void trainCluster(ClusteredWeibo weibo) {
		if (weibo != null) {
			double[] vector = initializeVector(vectorCalculator
					.getWordsVector(weibo.getKeywords()));

			if (vector != null && vector.length == dimention) {
				Instance instance = new DenseInstance(1, vector);
				instance.setDataset(instances);
				weibo.setInstance(instance);

				luoClusterer.trainOnInstance(instance);
			}
		}
	}

	protected void classify() {
		ClusteredWeibo weibo;
		AutoExpandVector<Cluster> clusters = luoClusterer.getClusteringResult()
				.getClustering();
		System.out.println("Cluster size is " + clusters.size());
		while ((weibo = weiboQueue.poll()) != null) {
			classify(clusters, weibo);
		}
	}

	protected void classify(AutoExpandVector<Cluster> clusters,
			ClusteredWeibo weibo) {
		Cluster cluster = matchCluster(clusters, weibo);

		if (cluster != null) {
			File outputFile = new File(String.format("%s%d.txt",
					pathManager.getResultsFolder(), cluster.hashCode()));
			try (PrintWriter out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(outputFile,
							true), "UTF-8")))) {
				out.println(objectMapper.writeValueAsString(weibo));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected Cluster matchCluster(AutoExpandVector<Cluster> clusters,
			ClusteredWeibo weibo) {

		if (weibo.getInstance() != null) {
			double maxProbability = 0;
			Cluster matchedCluster = null;

			for (Cluster cluster : clusters) {
				double probability = cluster.getInclusionProbability(weibo
						.getInstance());
				if (probability > maxProbability) {
					maxProbability = probability;
					matchedCluster = cluster;
				}
			}

			return matchedCluster;
		}

		return null;
	}
}
