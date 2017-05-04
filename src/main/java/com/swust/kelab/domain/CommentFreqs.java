package com.swust.kelab.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;





public class CommentFreqs {
	final int wordnum=30;
	public String[] words;
	public int[] wordFreqs;
	public int[] orderFreqs=new int[wordnum];
	public List<Comment> result=new ArrayList<Comment>();
	
	public void countwords(String[] rawWords){
		Set<String> set = new TreeSet<String>();
		for(String word: rawWords){
			if(word.length()>1)
			    set.add(word);
		} 
		Iterator<String> ite = set.iterator();
		List<String> wordsList = new ArrayList<String>();
		List<Integer> freqList = new ArrayList<Integer>();
		while(ite.hasNext()){
			String word = (String) ite.next();
			int count = 0;
			for(String str: rawWords){
				if(str.equals(word)){
					count++;
				}
			}
			wordsList.add(word);
			freqList.add(count);
		}
		this.words = wordsList.toArray(new String[0]);
		wordFreqs = new int[freqList.size()];
		for(int i = 0; i < freqList.size(); i++){
			wordFreqs[i] = freqList.get(i);
		}
	}
		
	public void sort() {
			int maxfreq=1;
			int count=0;
			int i;
			int[] max_freqs=new int[wordnum];
			for(i=0;i<this.wordFreqs.length;i++){
				if(maxfreq<this.wordFreqs[i]){
					maxfreq=this.wordFreqs[i];
				}
			}
			while(count<wordnum){
				for(i=0;i<this.wordFreqs.length;i++){
					if(maxfreq==this.wordFreqs[i]){
						this.orderFreqs[count]=maxfreq;
						max_freqs[count++]=i;
						if(count==wordnum)  break;
					}
				}
				maxfreq--;
				if(maxfreq==0)
					break;
			}
		
			for(i=0;i<count;i++){
				Comment comment=new Comment();
				comment.word=this.words[max_freqs[i]];
				comment.freq=this.orderFreqs[i];
				this.result.add(comment);
			}
			Collections.sort(this.result, new Comparator<Comment>() {  
				  
	            public int compare(Comment o1, Comment o2) {  
	                int result=1;  
	                if(Math.random()<0.6){
	                	result=-1;
	                }
	                return result;  
	            }  
	        });  
		}
	public List<Comment> getHighFreqWords(String[] rawWords){
		if(rawWords==null){
        	Comment comment = new Comment();
        	comment.freq=0;
        	this.result.add(comment);
        	return this.result;
        }
		this.countwords(rawWords);
		this.sort();
		return this.result;
	}
}
