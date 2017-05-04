package com.swust.kelab.service.analysis.summary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.swust.kelab.domain.KESentence;
import com.swust.kelab.domain.KEWord;
import com.swust.kelab.service.nlp.AnsjTool;
import com.swust.kelab.service.nlp.NLPTool;

//采用TF+ISF方法来计算相似度
public class AccumulateSim {

	// 设定过滤掉的阈值，比如相似度大于多少的过滤掉，或者固定过滤掉多少句子
	public double thre = 1.0 / 5.0;
	// 提取摘要的字数
	public int words = 400;

	// 参数表示给出某个主题下面的历史文档和该主题下面的当前所有文档，此处由于存储一个主题对应的历史文档不好存储，所以只能转化为字符串存储
	// content1为历史文档，content2为当前文档
	public String CaculateSim(String content1, String content2) throws IOException {
		// 把list中的每个content看成一个文档，计算TF和IDF，然后把所有句子合起来计算ISF
		StringBuffer sb = new StringBuffer();
		sb.append(content1);
		sb.append(content2);
		// 统计停用词
		Map<String, Integer> mapStop = new HashMap<String, Integer>();
		// 按照出场顺序编号单词索引号
		Map<String, Integer> mapIndex = new HashMap<String, Integer>();
		// 临时标记每个单词出现的句子编号
		Map<String, Integer> mapMark = new HashMap<String, Integer>();
		// 统计文档所有的单词和每个单词出现的句子数
		Map<String, Integer> mapsNum = new HashMap<String, Integer>();
		// 临时存储句子到map中
		Map<Integer, String> mapSent = new HashMap<Integer, String>();
		ReadStopword rs = new ReadStopword();
		mapStop = rs.readStopword();
		// IKAnalyzer analyzer = new IKAnalyzer();
		String allcontent = sb.toString();
		// String[] allsent=allcontent.split("。");
		NLPTool nlp = new AnsjTool();
		List<KESentence> listkes = nlp.splitSent(allcontent);
		// 句子索引
		int sentindex = 0;
		// 单词索引
		int wordindex = 0;
		for (KESentence kes : listkes) {
			sentindex++;
			//System.out.println(kes.getSentence());
			List<KEWord> listkew =nlp.segPos(kes);
			for (KEWord kew : listkew) {

				String s = kew.getWord();
				//System.out.print(s+" ");
				// 去除一下单个字母的和数值型的
				int flag = isNum(s);
				if (!s.isEmpty() && s.length() != 1 && flag == 0 && !mapStop.containsKey(s)) {
					if (!mapsNum.containsKey(s)) {
						wordindex++;
						mapIndex.put(s, wordindex);
						mapsNum.put(s, 1);
						mapMark.put(s, sentindex);// 存储每个单词第一次出现的句子索引号
					} else {
						if (mapMark.get(s) != sentindex) {
							int num = mapsNum.get(s);
							num++;
							mapsNum.remove(s);
							mapsNum.put(s, num);
							mapMark.remove(s);
							mapMark.put(s, sentindex);
						}
					}
					// System.out.print(s+" ");
				}
			}
		}

		// 将每个句子表示成tf-isf的向量
		AccumulateSim as = new AccumulateSim();
		List<Vector<Double>> list1 = as.caculateTFISF(mapIndex, mapsNum, mapStop, content1, sentindex);
		List<Vector<Double>> list2 = as.caculateTFISF(mapIndex, mapsNum, mapStop, content2, sentindex);
		List<Double> listsim = as.caculateSim(list1, list2, mapIndex);
		// Map<Integer,Double> mapRemain=new HashMap<Integer,Double>();
		int[] sentNum = new int[listsim.size()];
		double[] sentSim = new double[listsim.size()];
		for (int j = 0; j < listsim.size(); j++) {
			sentNum[j] = j;
			sentSim[j] = listsim.get(j);
		}
		// 根据相似度sentSim进行排序
		as.sort(sentNum, sentSim);
		String[] sent1 = content2.split("。");
		for (int count = 0; count < sent1.length; count++) {
			// System.out.println(sent1[count]);
			mapSent.put(count, sent1[count]);
		}
		// 去除的句子数
		int delNum = (int) Math.round(thre * listsim.size());
		//System.out.println("总句子数:" + sentNum.length);
		int[] sentRemain = new int[sentNum.length - delNum];

		for (int m = 0; m < sentNum.length; m++) {
			if (m >= delNum) {
				sentRemain[m - delNum] = sentNum[m];
			}
		}
		// 将sentRemain按照索引编号进行排序
		for (int n = 0; n < sentRemain.length - 1; n++) {
			for (int p = n + 1; p < sentRemain.length; p++) {
				if (sentRemain[n] > sentRemain[p]) {
					int temp = sentRemain[n];
					sentRemain[n] = sentRemain[p];
					sentRemain[p] = temp;
				}
			}
		}
		StringBuffer sbf = new StringBuffer();
		for (int q = 0; q < sentRemain.length; q++) {
			sbf.append(mapSent.get(sentRemain[q]) + "。");
			//System.out.println(mapSent.get(sentRemain[q]) + "。");
		}
		luhnSummary ls = new luhnSummary();
		// 提取摘要
		String summary2= ls.readFile(sbf.toString(),words);
		return summary2;

	}

	// 计算当前所有句子与历史信息的总相似度
	public List<Double> caculateSim(List<Vector<Double>> list1, List<Vector<Double>> list2,
			Map<String, Integer> mapIndex) {
		List<Double> list = new ArrayList<Double>();
		for (int i = 0; i < list2.size(); i++) {
			double sentallsim = 0.0;
			for (int j = 0; j < list1.size(); j++) {
				sentallsim += caculateVectorSim(list2.get(i), list1.get(j));
			}
			list.add(i, sentallsim);
		}
		return list;
	}

	// 计算两个向量之间的相似度
	public double caculateVectorSim(Vector<Double> v1, Vector<Double> v2) {
		double sim = 0.0;
		double sum1 = 0.0;
		for (int i = 0; i < v1.size(); i++) {
			sum1 += v1.get(i) * v2.get(i);
		}
		double sum2 = 0.0;
		for (int j = 0; j < v1.size(); j++) {
			sum2 += Math.pow(v1.get(j), 2);
		}
		double sum3 = 0.0;
		for (int k = 0; k < v2.size(); k++) {
			sum3 += Math.pow(v2.get(k), 2);
		}
		sim = sum1 / (Math.sqrt(sum2) * Math.sqrt(sum3));
		return sim;
	}

	// 计算每个句子中的词语的tf和isf，然后将每个句子表示成向量存储到list表中
	public List<Vector<Double>> caculateTFISF(Map<String, Integer> mapindex, Map<String, Integer> mapsNum,
			Map<String, Integer> mapStop, String content, int sentindex) throws IOException {

		// String[] sent=content.split("。");
		NLPTool np = new AnsjTool();
		List<KESentence> listkes = np.splitSent(content);
		// IKAnalyzer analyzer = new IKAnalyzer();
		List<Vector<Double>> list = new ArrayList<Vector<Double>>();
		// 存储该句子中的词频
		Map<String, Integer> mapfreInsent = new HashMap<String, Integer>();

		for (KESentence kes : listkes) {
			// Reader r = new StringReader(sent[i]);
			// TokenStream ts = analyzer.tokenStream("", r);
			// lc = new LowerCaseFilter(ts);
			// stem = new PorterStemFilter(lc);
			List<KEWord> listword = np.segPos(kes);
			for (KEWord keword : listword) {
				// TermAttribute
				// termAttribute=stem.getAttribute(TermAttribute.class);
				String s = keword.getWord();
				int flag = isNum(s);
				if (!s.isEmpty() && s.length() != 1 && flag == 0 && !mapStop.containsKey(s)) {
					if (!mapfreInsent.containsKey(s)) {
						mapfreInsent.put(s, 1);
					} else {
						int fre = mapfreInsent.get(s);
						fre++;
						mapfreInsent.remove(s);
						mapfreInsent.put(s, fre);
					}
					// System.out.print(s+" ");
				}
			}
			Set<String> set = mapfreInsent.keySet();
			Iterator<String> it = set.iterator();
			Vector<Double> v1 = new Vector<Double>();
			//System.out.println("mapindex："+mapindex.size());
			for (int j = 0; j < mapindex.size(); j++) {
				v1.add(j, 0.0);// 先初始化向量，不然后面的数据不好往里面加
			}
			while (it.hasNext()) {
				String s = it.next();
//				System.out.println(s);
				double weight = mapfreInsent.get(s) * ((Math.log10(sentindex / mapsNum.get(s))) / Math.log10(2));
				v1.set(mapindex.get(s) - 1, weight);
			}
			list.add(v1);

		}
		return list;
	}

	public void sort(int a[], double b[]) {
		for (int i = 0; i < a.length - 1; i++) {
			for (int j = i + 1; j < a.length; j++) {
				if (b[i] < b[j]) {
					double temp = b[i];
					b[i] = b[j];
					b[j] = temp;
					int stemp = a[i];
					a[i] = a[j];
					a[j] = stemp;
				}
			}
		}
	}

	public int isNum(String ss)// 判断字符串是否为数字
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

	public static void main(String args[]) throws IOException {
		File file = new File("G:\\切糕\\1.txt");
		FileInputStream fs = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fs, "GBK"));
		String s = br.readLine();
		StringBuffer sb = new StringBuffer();
		while (s != null) {
			sb.append(s);
			s = br.readLine();
		}
		// 如果第一次提取摘要，则直接用词方法
		luhnSummary ls = new luhnSummary();
		String summary1 = ls.readFile(sb.toString(), 260);
		System.out.println("摘要1:"+summary1);
		// 否则用此方法
		AccumulateSim am = new AccumulateSim();

		File file1 = new File("G:\\切糕\\2.txt");
		FileInputStream ft = new FileInputStream(file1);
		BufferedReader bd = new BufferedReader(new InputStreamReader(ft, "GBK"));
		String str = bd.readLine();
		StringBuffer sf = new StringBuffer();
		while (str != null) {
			sf.append(str);
			str = bd.readLine();
		}
		String summary2=am.CaculateSim(sb.toString(), sf.toString());
		System.out.println("摘要2:"+summary2);
	}

}
