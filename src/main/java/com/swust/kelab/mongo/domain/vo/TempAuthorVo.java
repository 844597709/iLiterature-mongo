package com.swust.kelab.mongo.domain.vo;

import com.swust.kelab.mongo.domain.TempAuthor;
import com.swust.kelab.mongo.domain.TempCrawlWebsite;

import java.util.List;

/**
 * Created by zengdan on 2017/5/10.
 */
public class TempAuthorVo extends TempAuthor {
    private List<TempCrawlWebsite> crawlWebsiteList;

    public List<TempCrawlWebsite> getCrawlWebsiteList() {
        return crawlWebsiteList;
    }

    public void setCrawlWebsiteList(List<TempCrawlWebsite> crawlWebsiteList) {
        this.crawlWebsiteList = crawlWebsiteList;
    }
}
