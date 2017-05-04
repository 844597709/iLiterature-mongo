package iLiteratureTest;

import com.swust.kelab.mongo.domain.TempWorks;
import com.swust.kelab.mongo.dao.query.BaseModel;

import java.util.List;

/**
 * Created by zengdan on 2017/2/15.
 */
public class AuthWork extends BaseModel{
    private Integer authId;
    private List<TempWorks> works;

//    private Integer workAuthId;
//    private Integer workTotalHits;
//    private Integer workTotalRecoms;
//    private Integer workCommentsNum;

    public Integer getAuthId() {
        return authId;
    }

    public void setAuthId(Integer authId) {
        this.authId = authId;
    }

    public List<TempWorks> getWorks() {
        return works;
    }

    public void setWorks(List<TempWorks> works) {
        this.works = works;
    }

    /*public Integer getWorkAuthId() {
        return workAuthId;
    }

    public void setWorkAuthId(Integer workAuthId) {
        this.workAuthId = workAuthId;
    }

    public Integer getWorkTotalHits() {
        return workTotalHits;
    }

    public void setWorkTotalHits(Integer workTotalHits) {
        this.workTotalHits = workTotalHits;
    }

    public Integer getWorkTotalRecoms() {
        return workTotalRecoms;
    }

    public void setWorkTotalRecoms(Integer workTotalRecoms) {
        this.workTotalRecoms = workTotalRecoms;
    }

    public Integer getWorkCommentsNum() {
        return workCommentsNum;
    }

    public void setWorkCommentsNum(Integer workCommentsNum) {
        this.workCommentsNum = workCommentsNum;
    }*/
}
