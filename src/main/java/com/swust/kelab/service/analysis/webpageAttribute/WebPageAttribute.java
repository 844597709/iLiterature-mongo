package com.swust.kelab.service.analysis.webpageAttribute;


public interface WebPageAttribute {
    public int webPageHot(String wordMD5);
    public double webPageValue(String title,String summary);
    public double webPageRelative(String searchName,Double rank);
    public double webPageWarning(double sentiment,double hot,double relative);
    public boolean webPageHot(String content, String compareContent);
}
