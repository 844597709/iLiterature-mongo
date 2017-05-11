package com.swust.kelab.mongo.domain.vo;

import com.swust.kelab.mongo.domain.TempCrawlWebsite;

/**
 * Created by zengdan on 2017/5/10.
 */
public class TempCrawlWebsiteVo extends TempCrawlWebsite{
    private Integer crwsTotalAuthorNum;
    private Integer crwsTotalWorkNum;

    public Integer getCrwsTotalAuthorNum() {
        return crwsTotalAuthorNum;
    }

    public void setCrwsTotalAuthorNum(Integer crwsTotalAuthorNum) {
        this.crwsTotalAuthorNum = crwsTotalAuthorNum;
    }

    public Integer getCrwsTotalWorkNum() {
        return crwsTotalWorkNum;
    }

    public void setCrwsTotalWorkNum(Integer crwsTotalWorkNum) {
        this.crwsTotalWorkNum = crwsTotalWorkNum;
    }
}
