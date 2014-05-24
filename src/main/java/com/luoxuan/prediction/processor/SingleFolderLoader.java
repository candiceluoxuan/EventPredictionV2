package com.luoxuan.prediction.processor;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.luoxuan.prediction.global.PathManager;

public abstract class SingleFolderLoader {

	@Autowired
	@Qualifier("pathManager")
	protected PathManager pathManager;

	public abstract void execute();

	public void execute(String folder) {
		File file = new File(folder);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				System.out.println("文件夹是空的!");
				return;
			} else {
				for (File file2 : files) {
					if (file2.isDirectory()) {
						System.out.println("文件夹:" + file2.getAbsolutePath());
					} else {
						System.out.println("文件:" + file2.getAbsolutePath());
						process(file2);
					}
				}
			}
		} else {
			System.out.println("文件不存在!");
		}
	}

	protected abstract void process(File file);

	public void rest() {
		System.out.println(pathManager.getRootPath());
		System.out.println(pathManager.getOriginalDataFolder());
		System.out.println(pathManager.getResourcesFolder());
		System.out.println(pathManager.getCorpus());
		System.out.println(pathManager.getVectors());
		System.out.println(pathManager.getClusters());
		System.out.println(pathManager.getPreprocessedDataFolder());
		System.out.println(pathManager.getResultsFolder());
	}

}
