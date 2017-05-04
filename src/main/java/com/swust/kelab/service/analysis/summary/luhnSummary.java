package com.swust.kelab.service.analysis.summary;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.swust.kelab.domain.KESentence;
import com.swust.kelab.domain.KEWord;
import com.swust.kelab.service.nlp.AnsjTool;
import com.swust.kelab.service.nlp.NLPTool;

public class luhnSummary {
	public String readFile(String filecontent, int words) throws IOException {
		Map<String, Integer> map = new HashMap<String, Integer>();
		Map<String, Double> mpp = new HashMap<String, Double>();// 存放最终权重的地方
		Map<String, Integer> mp3 = new HashMap<String, Integer>();
		// File filesummary=new File(file);
		Map<String, Integer> mm = new HashMap<String, Integer>();
		ReadStopword rs = new ReadStopword();
		mm = rs.readStopword();
		/**
		 * 修改分词方法
		 */
		NLPTool np = new AnsjTool();
		List<KESentence> listsent = np.splitSent(filecontent);
		for (KESentence kes : listsent) {
			List<KEWord> listword = np.segPos(kes);
			for (KEWord kew : listword) {
				String s = kew.getWord();
				// System.out.print(s+" ");
				//System.out.println();
				// 去除一下单个字母的和数值型的
				int flag = isNum(s);
				if (!s.isEmpty() && s.length() != 1 && flag == 0 && !mm.containsKey(s)) {
					if (!map.containsKey(s)) {
						map.put(s, 1);
					} else {
						int count = map.get(s);
						count++;
						map.remove(s);
						map.put(s, count);
					}
					// System.out.print(s+" ");
				}

			}
		}
		// System.out.println();
		Set<String> set = map.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String t = it.next();
			int c = map.get(t);
			if (c > 3) {
				mp3.put(t, c);
			}
		}
		// System.out.print(wordcount);
		// String[] sentens=filecontent.split("。");

		for (KESentence kes : listsent)// 分了文章再分句子
		{

			List<KEWord> listkew = kes.getWordList();
			int n = 0;
			int all = 0;
			double x = 0.0;
			for (KEWord keword : listkew) {
				String s = keword.getWord();
				// System.out.print(s+" ");
				int flag = isNum(s);
				if (!s.isEmpty() && s.length() != 1 && flag == 0 && !mm.containsKey(s)) {
					all++;
					if (mp3.containsKey(s)) {
						n++;
					}
				}
			}
			if (n == 0) {
				if (!mpp.containsKey(kes.getSentence())) {
					mpp.put(kes.getSentence(), 0.0);
				}

			} else {
				x = (Math.pow(n, 2)) / all;
				if (!mpp.containsKey(kes.getSentence())) {
					mpp.put(kes.getSentence(), x);
				}
			}
		}
		Set<String> st = mpp.keySet();
		Iterator<String> itor = st.iterator();
		String[] a = new String[st.size()];
		double[] b = new double[st.size()];
		int index = 0;
		while (itor.hasNext()) {
			String sent = itor.next();
			a[index] = sent;
			b[index] = mpp.get(sent);
			index++;
		}
		sort(a, b);
		int relength = 0;
		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i < a.length; i++) {

			relength = relength + a[i].length();

			if (relength < words) {
				sbf.append(a[i] + "。");
			} else {
				break;
			}
		}
		return sbf.toString();

	}
   /**
    * 为句子进行排序
    * @param a
    * @param b
    */
	public void sort(String a[], double b[]) {
		for (int i = 0; i < a.length - 1; i++) {
			for (int j = i + 1; j < a.length; j++) {
				if (b[i] < b[j]) {
					double temp = b[i];
					b[i] = b[j];
					b[j] = temp;
					String stemp = a[i];
					a[i] = a[j];
					a[j] = stemp;
				}
			}
		}
	}
   /**
    * 判断字符串是否为数字
    * @param ss
    * @return
    */	
	public int isNum(String ss)
	{
		int flag = 0;
		for (int i = 0; i < ss.length(); i++) {
			char a = ss.charAt(i);
			if (a >= '0' && a <= '9') {
				flag = 1;
				break;
			}
		}
		return flag;
	}

}
