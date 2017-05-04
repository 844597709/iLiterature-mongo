package com.swust.kelab.mongo.domain;

/**
 * Created by zengdan on 2017/1/6.
 */
public class TempUser {
    private Integer userId;
    private String userLoginName;
    private String userPassword;
    private String userRealName;
    private String userJobNum;
    private String userDepartment;
    private String userPosition;
    private String userContactNum;
    private String userEMail;
    private Integer userVerify;
    private String userRejectReason;
    private Integer userRole;
    private String userLoginTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserLoginName() {
        return userLoginName;
    }

    public void setUserLoginName(String userLoginName) {
        this.userLoginName = userLoginName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getUserJobNum() {
        return userJobNum;
    }

    public void setUserJobNum(String userJobNum) {
        this.userJobNum = userJobNum;
    }

    public String getUserDepartment() {
        return userDepartment;
    }

    public void setUserDepartment(String userDepartment) {
        this.userDepartment = userDepartment;
    }

    public String getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(String userPosition) {
        this.userPosition = userPosition;
    }

    public String getUserContactNum() {
        return userContactNum;
    }

    public void setUserContactNum(String userContactNum) {
        this.userContactNum = userContactNum;
    }

    public String getUserEMail() {
        return userEMail;
    }

    public void setUserEMail(String userEMail) {
        this.userEMail = userEMail;
    }

    public Integer getUserVerify() {
        return userVerify;
    }

    public void setUserVerify(Integer userVerify) {
        this.userVerify = userVerify;
    }

    public String getUserRejectReason() {
        return userRejectReason;
    }

    public void setUserRejectReason(String userRejectReason) {
        this.userRejectReason = userRejectReason;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public String getUserLoginTime() {
        return userLoginTime;
    }

    public void setUserLoginTime(String userLoginTime) {
        this.userLoginTime = userLoginTime;
    }
}
