package com.swust.kelab.service.preprocess.parser;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.MetaTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;


public class ModelPurify implements Extract{
	public static ArrayList<String> REMOVE_TAG_NAME;
	static 
	{
		REMOVE_TAG_NAME=new ArrayList<String>();
		REMOVE_TAG_NAME.add("style");
		REMOVE_TAG_NAME.add("script");;
		REMOVE_TAG_NAME.add("select");
	}
	public static String[] titleSplit={"_","-"," ","|"};
	public static String[] wordPaser={"没有登录","无权","原因"};
	public static String REGEX;
	static
	{
		REGEX="<!--[\\s\\S]*?-->";
		for(int i=0;i<REMOVE_TAG_NAME.size();i++)
		{
			REGEX+="|<"+REMOVE_TAG_NAME.get(i)+"[\\s\\S]*?>[\\s\\S]*?<\\/"+REMOVE_TAG_NAME.get(i)+">";
		}
	}
	private static String CR=System.getProperty("line.separator");
	private ArrayList<HtmlModel> modelList;
	private NodeList nodeList;
	private OrFilter filter;
	private String title="";
	private StringBuffer htmlPlainText;
	private HashMap<String,String> metaList;
	private String date;
	
	public void setPlainText(StringBuffer plainText) {
		this.htmlPlainText = plainText;
	}
	public HashMap<String, String> getMetaList() {
		return metaList;
	}
	public void setMetaList(HashMap<String, String> metaList) {
		this.metaList = metaList;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	public ArrayList<HtmlModel> getModelList() {
		return modelList;
	}
	public void setModelList(ArrayList<HtmlModel> modelList) {
		this.modelList = modelList;
	}
	public NodeList getNodeList() {
		return nodeList;
	}
	public void setNodeList(NodeList nodeList) {
		this.nodeList = nodeList;
	}
	
	public void addModel(String tag,String attr,String val)
	{
		this.modelList.add(new HtmlModel(tag,attr,val));
	}
	
	public ModelPurify(String htmlContent,String charset,String template)
	{
		htmlContent=preProcess(htmlContent);
		Parser htmlParser=Parser.createParser(htmlContent, charset);
		this.modelList=new ArrayList<HtmlModel>();
		this.filter=new OrFilter();
		loadFilter(template);
		try {
			this.nodeList=htmlParser.parse(this.filter);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.metaList=new HashMap<String,String>();
		this.htmlPlainText=new StringBuffer();
	}
	private void loadFilter(String template)
	{
		loadModel(template);
		ArrayList<NodeFilter> filterList=new ArrayList<NodeFilter>();
		filterList.add(new TagNameFilter("meta"));
		filterList.add(new TagNameFilter("title"));
		for(int i=0;i<this.modelList.size();i++)
		{
			HtmlModel model=this.modelList.get(i);
			TagNameFilter tagFilter=new TagNameFilter(model.getTagName());
			if(!model.getAttrName().isEmpty())
			{
				HasAttributeFilter attrFilter=new HasAttributeFilter(model.getAttrName(),model.getValue());
				filterList.add(new AndFilter(new NodeFilter[]{tagFilter,attrFilter}));
			}
			else
			{
				filterList.add(tagFilter);
			}
		}
		NodeFilter[] fList=new NodeFilter[]{};
		this.filter.setPredicates(filterList.toArray(fList));
	}
	private void loadModel(String template)
	{
		String[] tempModelList=template.split("&");
		String[] tempModel;
		for(int i=0;i<tempModelList.length;i++)
		{
			tempModel=tempModelList[i].split(",");
			if(tempModel.length<2)
			{
				this.modelList.add(new HtmlModel(tempModel[0],"",""));
			}
			else
			{
				this.modelList.add(new HtmlModel(tempModel[0],tempModel[1],tempModel[2]));
			}
		}
	}
	private String preProcess(String content)
	{
		content=content.toLowerCase();
		content=content.replaceAll(REGEX, "");
//		content = content.replaceAll("<a[^>]*>((?!<a).)*</a>", " ");           // remove link
		content = content.replaceAll("&.{2,5};|&#.{2,5};", " ");			// remove special char
		
		String tempContent=content.replaceAll("(?is)<.*?>", "");
		String dateRegex = "(\\d{4}-\\d{1,2}-\\d{1,2}(\\s+\\d{1,2}:\\d{1,2})?" +
 		"|\\d{4}年\\d{1,2}月\\d{1,2}日(\\s+\\d{1,2}:\\d{1,2})?|\\d{4}/\\d{1,2}/\\d{1,2}(\\s+\\d{1,2}:\\d{1,2})?)";
		Pattern p = Pattern.compile(dateRegex);
		Matcher m=p.matcher(tempContent);
		Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);  
        Date benchMark=calendar.getTime();
        Date maxDate=new Date(0);
		Date tempDate=benchMark;
		while(m.find())
		{
			tempDate=formatDate(m.group(0));
			if(tempDate.after(maxDate)&&tempDate.before(benchMark))
				maxDate=tempDate;
		}
		if(maxDate!=null)
		{
			date=getFormateDate(maxDate);
		}
	    return content;
	}
	private Date formatDate(String sourceDate)
	{
		SimpleDateFormat sourceFormat =new SimpleDateFormat("yyyy-MM-dd");
		if(sourceDate.replaceAll("\\d{4}年\\d{1,2}月\\d{1,2}日(\\s+\\d{1,2}:\\d{1,2})?", "").isEmpty())
    	{
    		sourceFormat=new SimpleDateFormat("yyyy年MM月dd日");
    	}
		if(sourceDate.replaceAll("\\d{4}/\\d{1,2}/\\d{1,2}(\\s+\\d{1,2}:\\d{1,2})?", "").isEmpty())
    	{
    		sourceFormat=new SimpleDateFormat("yyyy/MM/dd");
    	}
    	try {
			return sourceFormat.parse(sourceDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Date();
		} 
	}
	private String getFormateDate(Date date)
	{
		SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return targetFormat.format(date);
	}

	private void extractContent(NodeList list)
	{
		for(int i=0;i<list.size();i++)
		{
			Node node=list.elementAt(i);
			if(node instanceof TextNode)
			{
				String text=node.getText();
				text = text.replaceAll("\\s+","");
				text.trim();
				this.htmlPlainText.append(text);
				continue;
			}
			if(node instanceof Tag)
			{
				NodeList childList=node.getChildren();
				if(childList!=null&&childList.size()>0)
				{
					extractContent(childList);
					this.htmlPlainText.append(CR);
				}
			}
		}
	}
	private void filterMainContent(NodeList list)
	{
		for(int i=0;i<list.size();i++)
		{
			if(list.elementAt(i) instanceof Tag)
			{
				TagNode tagNode=(TagNode)list.elementAt(i);
				String tagName=tagNode.getTagName();
				if(tagName.equalsIgnoreCase("meta"))
				{
					MetaTag meta=(MetaTag)list.elementAt(i);
					this.metaList.put(meta.getMetaTagName(), meta.getMetaContent());
					this.nodeList.remove(i);
					i--;
					continue;
				}
				if(tagName.equalsIgnoreCase("title"))
				{
					NodeList titleTxtList=tagNode.getChildren();
					if(titleTxtList!=null&&titleTxtList.size()>0)
					for(int k=0;k<titleTxtList.size();k++)
					{
						if(titleTxtList.elementAt(k) instanceof TextNode)
						{
							String text=titleTxtList.elementAt(k).getText();
							text=text.replaceAll("\\s+", "");
							text.trim();
							this.title+=text;
						}
					}
					this.nodeList.remove(i);
					i--;
					continue;
				}
			}
		}
	}
	
	public void writeToFile(String filePath,String encoding) throws IOException
	{
		File file=new File(filePath);
		FileOutputStream out=new FileOutputStream(file);
		String article="";
		if(getPlainText().length()!=0)
		{	
			article+="文章标题："+getTitle()+CR;
			article+="关键词："+getKeywords()+CR;
			article+="描述："+getDescription()+CR;
			article+="发布时间："+getDate()+CR;
			article+="正文："+CR+getPlainText();
		}
		else
		{
			article+="文章标题："+getTitle()+CR;
		}
		out.write(article.getBytes(encoding));
	    out.close();
	}
	public String getTitle()
	{
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
		return this.title;
	}
	public void purify()
	{
		filterMainContent(this.nodeList);
		extractContent(this.nodeList);
	}
	public String getMetaInfo(String attr)
	{
		return this.metaList.get(attr);
	}
	public String getPlainText()
	{
		String text=htmlPlainText.toString().replaceAll("\\n[\\s| ]*\\r","");
		if(text.length()<100)
		{
			boolean needLogin=true;
			for(int i=0;i<wordPaser.length;i++)
			{
				if(text.indexOf(wordPaser[i])<0)
				{
					needLogin=false;
					break;
				}
			}
			if(needLogin)
				text="";
		}
		return text;
	}
	@Override
	public String getDate() {
		// TODO Auto-generated method stub
//		long interval=5;
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String tempDate=date;
//		if(tempDate==null||tempDate.isEmpty())
//			tempDate=dateFormat.format(new Date());
//		try {
//			Date publishDate=dateFormat.parse(tempDate);
//			if(Math.abs(publishDate.getTime()-new Date().getTime())>interval*365*24*3600*1000)
//				tempDate=dateFormat.format(new Date());
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return tempDate;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(date==null||date.isEmpty())
			date=dateFormat.format(new Date());
		return date;
	}
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return getMetaInfo("description")==null?"":getMetaInfo("description");
	}
	@Override
	public String getKeywords() {
		// TODO Auto-generated method stub
		return getMetaInfo("keywords")==null?"":getMetaInfo("keywords");
	}
}
