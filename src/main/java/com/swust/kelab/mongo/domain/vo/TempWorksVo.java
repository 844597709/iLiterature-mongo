package com.swust.kelab.mongo.domain.vo;

import com.swust.kelab.mongo.domain.TempCrawlWebsite;
import com.swust.kelab.mongo.domain.TempWorks;
import com.swust.kelab.mongo.domain.TempWorksUpdate;

import java.util.List;

/**
 * Created by zengdan on 2017/5/10.
 */
public class TempWorksVo extends TempWorks{
    private List<TempCrawlWebsite> crawlWebsiteList;
    private String workLastUpdateTime;

    public List<TempCrawlWebsite> getCrawlWebsiteList() {
        return crawlWebsiteList;
    }

    public void setCrawlWebsiteList(List<TempCrawlWebsite> crawlWebsiteList) {
        this.crawlWebsiteList = crawlWebsiteList;
    }

    public String getWorkLastUpdateTime() {
        return workLastUpdateTime;
    }

    public void setWorkLastUpdateTime(String workLastUpdateTime) {
        this.workLastUpdateTime = workLastUpdateTime;
    }
}
