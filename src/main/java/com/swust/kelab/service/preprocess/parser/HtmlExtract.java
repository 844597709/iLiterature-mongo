package com.swust.kelab.service.preprocess.parser;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;


public class HtmlExtract {
	private static HashMap<String,String> siteTemplates; 
	public static void loadDatabase(String ip,String user,String passwd)
	{
		siteTemplates=new HashMap<String,String>();
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://"+ip+":3306/isearch", user , passwd);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from siteurl");
			while(rs.next())
			{
				String url=rs.getString("surl_url");;
				String template=siteTemplates.get(url);
				if(template==null||template.isEmpty())
				{
					siteTemplates.put(url, rs.getString("surl_template"));
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private String[] forumKey={"bbs","club","tieba","baoliao","forum","dzh","168sjs","mala"};
	private boolean isForum;
	private Extract extract;
	
	
	public HtmlExtract(String url,String content)
	{
		isForum=true;
		for(int i=0;i<forumKey.length;i++)
		{
			if(url.contains(forumKey[i]))
			{
				isForum=true;
				break;
			}
		}
		if(isForum)
		{
			if(!getTempFromMap(url).isEmpty())
			{
				extract=new ModelPurify(content, "", getTempFromMap(url));
			}
			else
			{
				extract=new ForumExtract(url, content, "");
			}
		}
		else
		{
			extract=new NewsExtract(url, content, "");
		}
	}
	private String getTempFromMap(String url)
	{
		Iterator<String> urlStr=siteTemplates.keySet().iterator();
		String tempUrl="";
		String template="";
		while(urlStr.hasNext())
		{
			tempUrl=urlStr.next();
			String tempUrl1=tempUrl;
			tempUrl1=tempUrl.replaceAll("(https?|ftp|news)://", "");
			tempUrl1=tempUrl1.replaceAll("/.*", "");
			if(isContain(url,tempUrl1))
			{
				template=siteTemplates.get(tempUrl);
				if(template!=null&&!template.isEmpty())
				{
					break;
				}
			}
		}
		return template==null?"":template;
	}
	public static boolean isContain(String url,String tempUrl)
	{
		if(url.contains(tempUrl))
			return true;
		url=url.replaceFirst("\\.\\w+\\.", "");
		if(url.equalsIgnoreCase(tempUrl))
			return true;
		return false;
	}
	public void purify()
	{
		extract.purify();
	}
	public String getTitle()
	{
		return extract.getTitle();
	}
	public String getPlainText()
	{
		return extract.getPlainText();
	}
	public String getKeywords()
	{
		return extract.getKeywords();
	}
	public String getDescription()
	{
		return extract.getDescription();
	}
	public String getDate()
	{
		return extract.getDate();
	}
	public void writeToFile(String filePath,String encode) throws Exception
	{
		extract.writeToFile(filePath, encode);
	}
}
