package com.swust.kelab.service.preprocess.parser;

public interface Extract {
	public abstract void purify();
	public abstract String getTitle();
	public abstract String getPlainText();
	public abstract String getKeywords();
	public abstract String getDescription();
	public abstract String getDate();
	public abstract void writeToFile(String filePath,String encode) throws Exception;
}
