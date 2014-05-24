package com.luoxuan.prediction.global;

public class PathManager {

	private String rootPath;
	private String originalDataFolder;
	private String resourcesFolder;
	private String corpus;
	private String vectors;
	private String clusters;
	private String preprocessedDataFolder;
	private String resultsFolder;

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public String getOriginalDataFolder() {
		return getRootPath() + originalDataFolder;
	}

	public void setOriginalDataFolder(String originalDataFolder) {
		this.originalDataFolder = originalDataFolder;
	}

	public String getResourcesFolder() {
		return getRootPath() + resourcesFolder;
	}

	public void setResourcesFolder(String resourcesFolder) {
		this.resourcesFolder = resourcesFolder;
	}

	public String getCorpus() {
		return getResourcesFolder() + corpus;
	}

	public void setCorpus(String corpus) {
		this.corpus = corpus;
	}

	public String getVectors() {
		return getResourcesFolder() + vectors;
	}

	public void setVectors(String vectors) {
		this.vectors = vectors;
	}

	public String getClusters() {
		return getResourcesFolder() + clusters;
	}

	public void setClusters(String clusters) {
		this.clusters = clusters;
	}

	public String getPreprocessedDataFolder() {
		return getRootPath() + preprocessedDataFolder;
	}

	public void setPreprocessedDataFolder(String preprocessedDataFolder) {
		this.preprocessedDataFolder = preprocessedDataFolder;
	}

	public String getResultsFolder() {
		return getRootPath() + resultsFolder;
	}

	public void setResultsFolder(String resultsFolder) {
		this.resultsFolder = resultsFolder;
	}

	public PathManager() {

	}
}
