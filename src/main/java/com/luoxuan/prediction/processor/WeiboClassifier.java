package com.luoxuan.prediction.processor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import moa.cluster.Cluster;
import moa.cluster.Clustering;

public class WeiboClassifier extends SingleFolderLoader {

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		try (FileInputStream fs = new FileInputStream(pathManager.getClusters());
				ObjectInputStream ois = new ObjectInputStream(fs)) {
			Clustering clustering = (Clustering) ois.readObject();

			System.out.println("Cluster size is " + clustering.size());
			for (Cluster cluster : clustering.getClustering()) {
				// System.out.println(cluster.getInfo());
				try (PrintWriter out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(
								pathManager.getResultsFolder()
										+ cluster.getId() + ".html", true),
								"UTF-8")))) {
					out.print(cluster.getInfo());
				}
				System.out.println(cluster.getId());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	protected void process(File file) {
		// TODO Auto-generated method stub

	}

}
