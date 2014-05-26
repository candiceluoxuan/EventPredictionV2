package com.luoxuan.prediction.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import moa.options.FloatOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luoxuan.prediction.domain.PersistentWeibo;
import com.luoxuan.prediction.vector.VectorCalculator;

public class ClusterTrainer extends SingleFolderLoader {

	LUOClusterer luoClusterer;
	private float eps = 0.02f;
	private int minPoints = 1;
	private int initPoints = 1000;
	private float epsMin = 0f;
	private float epsMax = 2f;

	private int dimention = 201;
	private Instances instances;

	@Autowired
	@Qualifier("word2VEC")
	VectorCalculator vectorCalculator;

	@Autowired
	@Qualifier("objectMapper")
	ObjectMapper objectMapper;

	public ClusterTrainer() {
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

	@Override
	public void execute() {
		execute(pathManager.getPreprocessedDataFolder());

		System.out.println("Cluster size is "
				+ luoClusterer.getClusteringResult().size());
		// for (Cluster cluster : luoClusterer.getClusteringResult()
		// .getClustering()) {
		// System.out.println(cluster.getInfo());
		// }

		try (FileOutputStream f = new FileOutputStream(
				pathManager.getClusters());
				ObjectOutputStream oos = new ObjectOutputStream(f)) {
			oos.writeObject(luoClusterer.copy().getClusteringResult());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void process(File file) {
		try (BufferedReader input = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "utf-8"))) {

			String text;
			while ((text = input.readLine()) != null) {
				PersistentWeibo weibo = objectMapper.readValue(text,
						PersistentWeibo.class);
				trainCluster(weibo);
			}
		} catch (IOException ioException) {
			System.err.println("File Error!");
		} finally {
			System.out.println("Mission complete!");
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

	protected void trainCluster(PersistentWeibo weibo) {
		if (weibo != null) {
			double[] vector = initializeVector(vectorCalculator
					.getWordsVector(weibo.getKeywords()));
			if (vector != null && vector.length == dimention) {
				Instance instance = new DenseInstance(1, vector);
				instance.setDataset(instances);
				luoClusterer.trainOnInstance(instance);
			}
		}
	}
}
