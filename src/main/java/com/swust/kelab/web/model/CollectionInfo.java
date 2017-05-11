package com.swust.kelab.web.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;









import com.swust.kelab.domain.WorkDetail;
import com.swust.kelab.domain.WorksUpdate;

public class CollectionInfo {
	
	public static int compare_date(String DATE1, String DATE2) {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt2.getTime() - dt1.getTime() > 604800000) {
				return 1; //超过7天
			} else {
				return -1;
			} 
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}
	

	public static Map<String, Object> collectionInfo(List<WorksUpdate> result,String[] paraName){
		
		if(result.size() == 0){
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("none", false);
			return data;
		}
		
		String[] copy = new String[paraName.length-1];
		for(int i = 0 ; i < copy.length ; i++){
			if(i == copy.length-1){
				copy[i] = paraName[paraName.length-1];
			}
			else{
				copy[i] = paraName[i];
			}
			int zx = copy[i].indexOf(':');
			copy[i] = copy[i].substring(0,zx);
		}
		
		//初始化数组
		int updateDateNum = 7;
		String[] time = new String[updateDateNum];
		String[] attr1 = new String[updateDateNum];
		String[] attr2 = new String[updateDateNum];
		String[] attr3 = new String[updateDateNum];
		String[] attr4 = new String[updateDateNum];
		String[] attr5 = new String[updateDateNum];
		String[] attr6 = new String[updateDateNum];
		String[] attr7= new String[updateDateNum];
		String[] attr9 = new String[updateDateNum];
		
		for(int i = 0 ; i < updateDateNum ; i ++){
			time[i] = new String();
			attr1[i] = new String();
			attr2[i] = new String();
			attr3[i] = new String();
			attr4[i] = new String();
			attr5[i] = new String();
			attr6[i] = new String();
			attr7[i] = new String();
			attr9[i] = new String();
		}
		
		//***********************往数组中送数据*********************
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		WorksUpdate temp = new WorksUpdate();
		int  k = 0 ;
		int flag = -1;
		for(int j = 0 ; j < result.size() ; j++){
			temp = result.get(j);	
			String date1 = temp.wouptime.substring(0,10);
			Date date=new Date();  
			String str=sdf.format(date);  
			flag = compare_date(date1,str);
			if(flag > 0) //超过7天
			{
				k = j ;
				break;
			}
		}
		
		if(k>=updateDateNum){
			for(int i = 6; i> 0 ; i--){
				WorksUpdate ans = result.get(i);
				attr1[i] = ans.attr1;
				attr2[i] = ans.attr2;
				attr3[i] = ans.attr3;
				attr4[i] = ans.attr4;
				attr5[i] = ans.attr5;
				attr6[i] = ans.attr6;
				attr7[i] = ans.attr7;
				attr9[i] = ans.attr9;
			}
		}
		else {
			WorksUpdate ans = result.get(k);
			attr1[0] = ans.attr1;
			attr2[0] = ans.attr2;
			attr3[0] = ans.attr3;
			attr4[0] = ans.attr4;
			attr5[0] = ans.attr5;
			attr6[0] = ans.attr6;
			attr7[0] = ans.attr7;
			attr9[0] = ans.attr9;
			int cnt = 1;
			while(cnt < updateDateNum){
				if(k>0){
					for(int i = k-1;i>-1 ; i--){
						ans = result.get(i);
						attr1[cnt] = ans.attr1;
						attr2[cnt] = ans.attr2;
						attr3[cnt] = ans.attr3;
						attr4[cnt] = ans.attr4;
						attr5[cnt] = ans.attr5;
						attr6[cnt] = ans.attr6;
						attr7[cnt] = ans.attr7;
						attr9[cnt] = ans.attr9;
						cnt++;
					}
				}
				else
				{
					attr1[cnt] = attr1[cnt-1];	
					attr2[cnt] = attr2[cnt-1];
					attr3[cnt] = attr3[cnt-1];
					attr4[cnt] = attr4[cnt-1];
					attr5[cnt] = attr5[cnt-1];
					attr6[cnt] = attr6[cnt-1];
					attr7[cnt] = attr7[cnt-1];
					attr9[cnt] = attr9[cnt-1];
					cnt++;
				}
				
			}
		}
		
		
		
		Date date[]= new Date[updateDateNum];
		Calendar calendar = Calendar.getInstance();
		date[0]=new Date();
		calendar.setTime(date[0]);
		for(int i=1;i<updateDateNum;i++)
		{
			 calendar.add(Calendar.DAY_OF_MONTH, -i);  //设置为前一天
			 date[i] = calendar.getTime();   //得到前一天的时间
			 calendar = Calendar.getInstance(); 
			
		}
		for(int i=0;i<updateDateNum;i++)
		{	
			 String defaultEndDate = sdf.format(date[i]); //格式化当前时间
			 defaultEndDate = defaultEndDate.substring(5);	
			 time[6-i] = defaultEndDate;
			 
		}
	
		
		for(int i = 0 ; i < updateDateNum; i++){
			if(attr1[i] ==null ||attr2[i] == null||attr3[i] == null||attr4[i] == null
					||attr5[i] == null||attr6[i] == null||attr7[i] == null||attr9[i] == null)
			{
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("none", false);
				return data;
			}
			else {
				if(attr1[i].equals("-1"))
					attr1[i] = "0" ;
				if(attr2[i].equals("-1"))
					attr2[i] = "0" ;
				if(attr3[i].equals("-1"))
					attr3[i] = "0" ;
				if(attr4[i].equals("-1"))
					attr4[i] = "0" ;
				if(attr5[i].equals("-1"))
					attr5[i] = "0" ;
				if(attr6[i].equals("-1"))
					attr6[i] = "0" ;
				if(attr7[i].equals("-1"))
					attr7[i] = "0" ;
				if(attr9[i].equals("-1"))
					attr9[i] = "0" ;
			}
			
		}
		
		//*********************
		List<String[]> attr = new ArrayList<String[]>();
		attr.add(attr1);
		attr.add(attr2);
		attr.add(attr3);
		attr.add(attr4);
		attr.add(attr5);
		attr.add(attr6);
		attr.add(attr7);
		attr.add(attr9);
		
		
		String[] aver = new String[copy.length];
		double ave = 0;
		
		
		for(int i = 0 ; i < attr.size() ; i ++){
			for(int j = 0 ; j < attr.get(i).length ; j++){
				ave = ave+Double.parseDouble(attr.get(i)[j]);
			}
			ave = ave/attr.get(i).length;
			aver[i] = String.valueOf(ave);
				 
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		List<AttrModel> workData = new ArrayList<AttrModel>();
		for(int i = 0 ; i < copy.length ; i++){
			AttrModel model = new AttrModel(updateDateNum);
			model.setAttrName(copy[i]);
			model.setAttrValue(attr.get(i));
			model.setAttrAverage(aver[i]);
			workData.add(model);
		}
		data.put("time", time);
		data.put("info", workData);
		
		return data;
		
	}

	public static Map<String, Object> totalAttr(List<WorkDetail> result) {
		
		if(result.size() == 0){
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("none", false);
			return data;
		}
		
		int updateDateNum = 7;
		String[] time = new String[updateDateNum];
		String[] totalHits = new String[updateDateNum];
		String[] totalRecoms = new String[updateDateNum];
		String[] commentsNum = new String[updateDateNum];
		
		
		for(int i = 0 ; i < updateDateNum ; i ++){
			time[i] = new String();
			totalHits[i] = new String();
			totalRecoms[i] = new String();
			commentsNum[i] = new String();
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		WorkDetail temp = new WorkDetail();
		int  k = 0 ;
		int flag = -1;
		for(int j = 0 ; j < result.size() ; j++){
			temp = result.get(j);	
			String date1 = temp.sTime.substring(0,10);
			Date date=new Date();  
			String str=sdf.format(date);  
			flag = compare_date(date1,str);
			if(flag > 0) //超过7天
			{
				k = j ;
				break;
			}
		}
		
		if(k>=updateDateNum){
			for(int i = 6; i> 0 ; i--){
				WorkDetail ans = result.get(i);
				totalHits[i] = ans.totalHits;
				totalRecoms[i] = ans.totalRecoms;
				commentsNum[i] = ans.commentsNum;
				
			}
		}
		else {
			WorkDetail ans = result.get(k);
			totalHits[0] = ans.totalHits;
			totalRecoms[0] = ans.totalRecoms;
			commentsNum[0] = ans.commentsNum;
			int cnt = 1;
			while(cnt < updateDateNum){
				if(k>0){
					for(int i = k-1;i>-1 ; i--){
						ans = result.get(i);
						totalHits[cnt] = ans.totalHits;
						totalRecoms[cnt] = ans.totalRecoms;
						commentsNum[cnt] = ans.commentsNum;
						cnt++;
					}
				}
				else
				{
					totalHits[cnt] = totalHits[cnt-1];	
					totalRecoms[cnt] = totalRecoms[cnt-1];
					commentsNum[cnt] = commentsNum[cnt-1];
					cnt++;
				}
				
			}
		}
		
		Date date[]= new Date[updateDateNum];
		Calendar calendar = Calendar.getInstance();
		date[0]=new Date();
		calendar.setTime(date[0]);
		for(int i=1;i<updateDateNum;i++)
		{
			 calendar.add(Calendar.DAY_OF_MONTH, -i);  //设置为前一天
			 date[i] = calendar.getTime();   //得到前一天的时间
			 calendar = Calendar.getInstance(); 
			
		}
		for(int i=0;i<updateDateNum;i++)
		{	
			 String defaultEndDate = sdf.format(date[i]); //格式化当前时间
			 defaultEndDate = defaultEndDate.substring(5);	
			 time[6-i] = defaultEndDate;
			 
		}
		
		
		
		for(int i = 0 ; i < updateDateNum; i++){
			
			if(totalHits[i] ==null ||totalRecoms[i] == null||commentsNum[i] == null)
			{
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("none", false);
				return data;
			}
		
			else
			{
				if(totalHits[i].equals("-1"))
					totalHits[i] = "0" ;
				if(totalRecoms[i].equals("-1"))
					totalRecoms[i] = "0" ;
				if(commentsNum[i].equals("-1"))
					commentsNum[i] = "0" ;
			}
		}
		
		List<String[]> attr = new ArrayList<String[]>();
		attr.add(totalHits);
		attr.add(totalRecoms);
		attr.add(commentsNum);
		
		Map<String, Object> data = new HashMap<String, Object>();
		List<TotalAttr> workData = new ArrayList<TotalAttr>();
		for(int i = 0 ; i < 3 ; i++){
			TotalAttr model = new TotalAttr(updateDateNum);		
			model.setAttrValue(attr.get(i));
			workData.add(model);
		}
		data.put("time", time);
		data.put("info", workData);
		
		return data;
	}
	
}