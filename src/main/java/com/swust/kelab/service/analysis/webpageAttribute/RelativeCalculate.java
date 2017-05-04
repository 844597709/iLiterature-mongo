package com.swust.kelab.service.analysis.webpageAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.swust.kelab.domain.KESentence;
import com.swust.kelab.domain.KEWord;
import com.swust.kelab.service.nlp.AnsjTool;
import com.swust.kelab.service.nlp.NLPTool;

@Service
public class RelativeCalculate {
    /***
     * 功能：相似度
     * 
     * @author jia
     * @param s1
     * @param s2
     * @return
     */
    public double relativeCalculate(String s1, String s2) {
        
        List<String> vector1 = new ArrayList<String>();
        List<String> vector2 = new ArrayList<String>();
        List<String> vector3=new ArrayList<String>();

        Map<String, Integer> map1 = new HashMap<String, Integer>();
        Map<String, Integer> map2 = new HashMap<String, Integer>();
        
        List<Integer> list1 = new ArrayList<Integer>();
        List<Integer> list2 = new ArrayList<Integer>();

        vector1 = participle(s1);
        vector2 = participle(s2);
        vector3=participle(s1+"。"+s2);
        
        map1 = wordRepeatCount(vector1);
        map2 = wordRepeatCount(vector2);
        
        HashSet<String> set=new HashSet<String>();
        for(int i=0;i<vector3.size();i++){
            set.add(vector3.get(i));
        }
        
        double relative = 0, divisor = 0, dividend = 0;

        Iterator<String> iter = set.iterator();
        while (iter.hasNext()) {
            String key=iter.next();
            if (map1.containsKey(key)&&(!map2.containsKey(key))) {
                list1.add(map1.get(key));
                list2.add(0);
            }
            else if(map2.containsKey(key)&&(!map1.containsKey(key))){
                list1.add(0);
                list2.add(map2.get(key));
            }
            else if(map1.containsKey(key)&&(map2.containsKey(key))){
                list1.add(map1.get(key));
                list2.add(map2.get(key));
            }
        }
        divisor = pointMulti(list1, list2);
        dividend = Math.sqrt(squares(list1) * squares(list2));
        relative =divisor / dividend;
        
        return relative;
    }

    /***
     * 功能：平方和
     * 
     * @author jia
     * @param vector
     * @return
     */
    private double squares(List<Integer> vector) {
        double result = 0;
        for (int i = 0; i < vector.size(); i++) {
            result += vector.get(i) * vector.get(i);
        }
        return result;
    }

    /***
     * 功能：点乘法
     * 
     * @author jia
     * @param vector1
     * @param vector2
     * @return
     */
    private double pointMulti(List<Integer> vector1, List<Integer> vector2) {
        double result = 0;
        for (int i = 0; i < vector1.size(); i++) {
            result += vector1.get(i) * vector2.get(i);
        }
        return result;
    }

    /***
     * 功能：分词
     * 
     * @author jia
     * @param s
     * @return
     */
    private List<String> participle(String content) {
        List<String> result = new ArrayList<String>();
        try {
          NLPTool nlpTool=new AnsjTool();
          List<KEWord> keyWord=new ArrayList<KEWord>();
          KESentence sentence=new KESentence(content);
          keyWord=nlpTool.segPos(sentence);
          for(int i=0;i<keyWord.size();i++){
              result.add(keyWord.get(i).getWord());
          }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /***
     * 功能：词频
     * 
     * @author jia
     * @param list
     * @return
     */
    public Map<String, Integer> wordRepeatCount(List<String> list) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        for (int i = 0; i < list.size(); i++) {
            if (result.containsKey(list.get(i))) {
                int count = result.get(list.get(i));
                result.put(list.get(i), ++count);
            } else {
                result.put(list.get(i), 1);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            String s1 = "小贩跳海城管见死不救珠海城管反驳:水不深?";
            String s2 = "随后温总理就离开了舟曲县城，预计温总理今天下午就回到北京。以上就是温总理今天上午的最新动态";
            String s3 = "四川";
            String s4 = "小贩跳海城管见死不救城管反驳:水不深?";
            RelativeCalculate relativeCalculate = new RelativeCalculate();
            System.out.println(relativeCalculate.relativeCalculate(s1, s4));
            //relativeCalculate.participle(s1);
        } catch (Exception e) {
        }
    }
}
