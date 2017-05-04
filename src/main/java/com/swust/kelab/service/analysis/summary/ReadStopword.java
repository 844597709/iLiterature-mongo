package com.swust.kelab.service.analysis.summary;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.google.common.io.Resources;

public class ReadStopword {
	//public Map<String,Integer> map=new HashMap<String,Integer>();
	public Map<String,Integer>readStopword() throws IOException
	{
		Map<String,Integer> map=new HashMap<String,Integer>();
		//BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("stopwords.txt"),"UTF-8"));
		URL url=Resources.getResource("chineseStopwords.txt");
		FileReader fr=new FileReader(new File(url.getPath()));
		BufferedReader br=new BufferedReader(fr);
		String line=br.readLine();
		int n=1;
		while(line!=null)
		{
			if(!line.isEmpty())
			{
				String s=line.trim();
				if(!map.containsKey(s))
				{
				  	map.put(s, n);
				}
			}
			line=br.readLine();
			n++;
		}
		return map;
	}

}
