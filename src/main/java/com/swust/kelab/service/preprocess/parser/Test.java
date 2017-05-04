package com.swust.kelab.service.preprocess.parser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;




public class Test {
	
	public static int FILENUM=0;
	public static long ALLPURIFYTIME=0;  
	public static void main(String[] args) throws  Exception
	{      
		HtmlExtract.loadDatabase("localhost", "root", "cs.swust");
		File root=new File("D://项目开发//原始测试集//extractTest");
		long startTime=System.currentTimeMillis();
		getAllFileName(root);
		long endTime=System.currentTimeMillis();
		System.out.println("FileNum:"+FILENUM);
		System.out.println("StartTime:"+startTime);
		System.out.println("EndTime:"+endTime);
		System.out.println("Waste:"+(endTime-startTime));
		System.out.println("AllPurifyTime:"+ALLPURIFYTIME);
		System.out.println("TimePerFile:"+ALLPURIFYTIME/FILENUM);
		
	//	System.out.println("DateFormat Error:"+WebTextExtract.iSearch);
	}
	public static void getAllFileName(File file) throws Exception
	{
		if(file.isDirectory())
		{
			String[] list=file.list();
			for(int i=0;i<list.length;i++)
			{
				File childFile=new File(file.getAbsolutePath()+"//"+list[i]);
				getAllFileName(childFile);
			}
		}
		else
		{
			String encode="gb2312";
			String content=readTextFile(file.getAbsolutePath(),encode);
			long tempStart=System.currentTimeMillis();
			HtmlExtract parser=new HtmlExtract(file.getParent(), content);
			parser.purify();
			long tempEnd=System.currentTimeMillis();
			String directory=file.getParent().replace("D:", "E:");
			File dirFile=new File(directory);
			if(!dirFile.exists())
				dirFile.mkdirs();
			parser.writeToFile(directory+"//"+(file.getName().replaceFirst(".html|.htm", ".txt")),encode);
		//	webText.writeToFile(directory+"//"+(webText.getTitle()+".txt"),encode);
			FILENUM++;
			ALLPURIFYTIME+=(tempEnd-tempStart);
			/*String type=html.getHtmlType();
			if(type.equals("TopicType"))
				TOPICNUM++;
			if(type.equals("ImageType"))
				IMGNUM++;
			if(type.equals("HrefType"))
				HREFNUM++;*/
			System.out.println(FILENUM+":("+(tempEnd-tempStart)+")"+file.getParent()+"\\"+file.getName());
		}
	}
	public static String readTextFile(String sFileName, String sEncode)
    {
	    StringBuffer sbStr = new StringBuffer();
	    try
	    {
	         File ff = new File(sFileName);
	         InputStreamReader read = new InputStreamReader(new FileInputStream(ff),
	                    sEncode);
	         BufferedReader ins = new BufferedReader(read);
	         String dataLine = "";
	         while (null != (dataLine = ins.readLine()))
	         {
	            sbStr.append(dataLine);
	            sbStr.append("\r\n");
	         }
	         ins.close();
	    }
	    catch (Exception e)
	    {
	        System.out.println("read Text File Error");
	    }
	    return sbStr.toString();
	}
}
