package com.swust.kelab.service.preprocess.parser;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NewsExtract implements Extract{

	private static String CR=System.getProperty("line.separator");
	private static int BL_BLOCK=4;
	private static int THRESHOLD=45;
	public static ArrayList<String> REMOVE_TAG_NAME;
	static 
	{
		REMOVE_TAG_NAME=new ArrayList<String>();
		REMOVE_TAG_NAME.add("style");
		REMOVE_TAG_NAME.add("script");
		REMOVE_TAG_NAME.add("noscript");
		REMOVE_TAG_NAME.add("object");
		REMOVE_TAG_NAME.add("form");
		REMOVE_TAG_NAME.add("xml");
		REMOVE_TAG_NAME.add("select");
		REMOVE_TAG_NAME.add("textarea");
	}
	public static String REGEX;
	static
	{
		REGEX="<!--[\\s\\S]*?-->|<!DOCTYPE[\\s\\S]*?>";
		for(int i=0;i<REMOVE_TAG_NAME.size();i++)
		{
			REGEX+="|<"+REMOVE_TAG_NAME.get(i)+"[\\s\\S]*?>[\\s\\S]*?<\\/"+REMOVE_TAG_NAME.get(i)+">";
		}
	}
	public static String[] titleSplit={"_","-"," ","|"};
	public static String[] wordFilter={"copyright","ctrl","uid","gmt","poweredby"};
	
	private String title;
	private String date;
	private String keywords;
	private String description;
	private List<String> lines;
	private String  html;
	private StringBuffer text;
	private List<String> metaInfo;
	
	private int start;
	private int end;
	private ArrayList<Integer> indexDistribution ;
	
	
	public List<String> getMetaInfo() {
		return metaInfo;
	}
	public void setMetaInfo(List<String> metaInfo) {
		this.metaInfo = metaInfo;
	}
	public void setText(StringBuffer text) {
		this.text = text;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<String> getLines() {
		return lines;
	}
	public void setLines(List<String> lines) {
		this.lines = lines;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public ArrayList<Integer> getIndexDistribution() {
		return indexDistribution;
	}
	public void setIndexDistribution(ArrayList<Integer> indexDistribution) {
		this.indexDistribution = indexDistribution;
	}
	
	public NewsExtract(String url,String content,String encode)
	{
		html=content.toLowerCase();
		lines = new ArrayList<String>();
		indexDistribution = new ArrayList<Integer>();
		metaInfo=new ArrayList<String>();
		text=new StringBuffer();
		title=date=keywords=description="";
	}
	public void purify()
	{
		preProcess();
		findStartAndEnd();
		extractText(start, end);
	}
	public String getPlainText()
	{
		return text.toString();
	}
	private void preProcess() {
		html = html.replaceAll(REGEX, "");
		html = html.replaceAll("<a[^>]*>((?!<a).)*</a>", " ");           // remove link
		html = html.replaceAll("&.{2,5};|&#.{2,5};", " ");			// remove special char
		String titleRegex = "<title>[\\s\\S]*?</title>";
		Pattern p = Pattern.compile(titleRegex);
		Matcher m = p.matcher(html);
	    if(m.find())
	    {
	    	title=m.group(0);
	    	title=title.substring(title.indexOf("title>")+6, title.indexOf("/title>")-1);
	    	String split="_";
	        for(int i=0;i<titleSplit.length;i++)
	    	{
	        	if(title.contains(titleSplit[i]))
	        	{
	        		split=titleSplit[i];
	        		break;
	        	}
	    	}
	        title=title.split(split)[0];
	    }
	    String metaRegex = "<meta.*?name=\".*?\".*?/?>";
		p = Pattern.compile(metaRegex);
		m=p.matcher(html);
        while(m.find())
	    {
	    	metaInfo.add(m.group(0));
	    }
        keywords=extractMetaContent("keywords");
        description=extractMetaContent("description");
        html = html.replaceAll("(?is)<.*?>", "");
        String dateRegex = "(\\d{4}-\\d{2}-\\d{2}(\\s+\\d{2}:\\d{2})?" +
        		"|\\d{4}年\\d{2}月\\d{2}日(\\s+\\d{2}:\\d{2})?|\\d{4}/\\d{1,2}/\\d{1,2}(\\s+\\d{2}:\\d{2})?)";
        p = Pattern.compile(dateRegex);
        m=p.matcher(html);
        if(m.find())
	    {
	    	date=m.group(0);
	    	date=formatDate(date);
	    }
	}
	
	private void findStartAndEnd() {
		lines = Arrays.asList(html.split("\n"));
		indexDistribution.clear();
		
		for (int i = 0; i < lines.size() - BL_BLOCK; i++) {
			int wordsNum = 0;
			for (int j = i; j < i + BL_BLOCK; j++) { 
				lines.set(j, lines.get(j).replaceAll("\\s+", ""));
				String line=lines.get(j);
				for(int k=0;k<wordFilter.length;k++)
				{
					if(line.contains(wordFilter[k]))
					{
						lines.set(j, "");
						break;
					}
				}
				wordsNum += lines.get(j).length();
			}
			indexDistribution.add(wordsNum);
		}
		
		start = -1; end = -1;
		boolean boolstart = false, boolend = false;
		text.setLength(0);
		
		//ȡ��ֵ��
		int max=0;
		int maxValue=0;
		for(int i=0;i<indexDistribution.size();i++)
		{
			if(indexDistribution.get(i)>=maxValue)
			{
				max=i;
				maxValue=indexDistribution.get(i);
			}
		}
//		System.out.println(html);
		for (int i = 0; i < indexDistribution.size() - 1; i++) {
			
			if(indexDistribution.get(i)>THRESHOLD&&!boolstart)
			{
				
				if(indexDistribution.get(i)<THRESHOLD+15&&(indexDistribution.get(i)==indexDistribution.get(i+1))
						&&(indexDistribution.get(i+1)==indexDistribution.get(i+2))
						&&(indexDistribution.get(i+2)==indexDistribution.get(i+3))&&(indexDistribution.get(i+4)==0))
				{
					i+=2;
					continue;
				}
				if(indexDistribution.get(i+1)>0||indexDistribution.get(i+2)>0||indexDistribution.get(i+3)>0
						||indexDistribution.get(i+4)>0)
				{
					boolstart=true;
					start=i;
					continue;
				}
			}
			
			if (boolstart) {
				if (indexDistribution.get(i).intValue() == 0 
					&& indexDistribution.get(i+1).intValue() == 0) {
					end = i;
					boolend = true;
				}
			}
			
			if(boolend&&max<=end&&max>=start)
			{
				return;
			}
			if(boolend)
			{
				boolstart=boolend=false;
			}
		}
	}
	private void extractText(int start,int end)
	{
		if(start==-1||end==-1||end<start)
		{
			text.setLength(0);
			title="非新闻类网页";
			return;
		}
		for (int i = start; i <= end; i++) {
			if (lines.get(i).length() < 5) 
			{
				continue;
			}
			text.append(lines.get(i) + CR);
		}
	}
	private String extractMetaContent(String name)
	{
		String content="";
		int startIndex=-1;
		int endIndex=-1;
		for(int i=0;i<metaInfo.size();i++)
		{
			if(metaInfo.get(i).indexOf("name=\""+name+"\"")>0)
			{
				startIndex=metaInfo.get(i).indexOf("content=\"");
				if(startIndex<0)
					startIndex=metaInfo.get(i).indexOf("content=");
				endIndex=metaInfo.get(i).indexOf("\"",startIndex+9);
				if(endIndex<0)
					endIndex=metaInfo.get(i).indexOf(" ",startIndex+9);
				if(startIndex>=0&&endIndex>=0&&startIndex+9<=endIndex)
				{
					content=metaInfo.get(i).substring(startIndex+9,endIndex);
				}
			}
		}
		return content;
	}
	public void writeToFile(String filePath,String encode) throws Exception
	{
		File file=new File(filePath);
		FileOutputStream out=new FileOutputStream(file);
		String article="";
		if(text.length()!=0)
		{	
			article+="文章标题："+title+CR;
			article+="关键词："+keywords+CR;
			article+="描述："+description+CR;
			article+="发布日期："+date+CR;
			article+="正文："+CR+getPlainText();
		}
		else
		{
			article+="文章标题："+title+CR;
		}
		out.write(article.getBytes(encode));
	    out.close();
	}
	private String formatDate(String sourceDate)
	{
		String targetDate="";
		SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sourceFormat =new SimpleDateFormat("yyyy-MM-dd");
		if(sourceDate.replaceAll("\\d{4}年\\d{2}月\\d{2}日(\\s+\\d{2}:\\d{2})?", "").isEmpty())
    	{
    		sourceFormat=new SimpleDateFormat("yyyy年MM月dd日");
    	}
		if(sourceDate.replaceAll("\\d{4}/\\d{1,2}/\\d{1,2}(\\s+\\d{2}:\\d{2})?", "").isEmpty())
    	{
    		sourceFormat=new SimpleDateFormat("yyyy/MM/dd");
    	}
    	try {
    		targetDate=targetFormat.format(sourceFormat.parse(sourceDate));
			return targetDate;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return sourceDate;
		} 
	}
}
