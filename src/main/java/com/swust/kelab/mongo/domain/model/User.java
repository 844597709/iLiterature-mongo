package com.swust.kelab.mongo.domain.model;

import com.swust.kelab.mongo.dao.query.BaseModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class User extends BaseModel{
    private Integer userId;
    private String userLoginName;
    private String userPassword;
    private String userRealName;
    private String userJobNum;
    private String userDepartment;
    private String userPosition;
    private String userContactNum;
    private String userEmail;
    private Integer userVertify;
    private String userRejectReason;
    private Integer userRole;
    
    //--zd--
    private int time;
    private String deadTime;
    private Date upDeadTime;
    private int isLegal;
    
    public int getTime(){
    	return time;
    }
    public void setTime(int time){
    	this.time = time;
    }
    
    public String getDeadTime(){
    	return deadTime;
    }
    public Date getUpDeadTime() throws ParseException{
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    	upDeadTime = df.parse(deadTime);
    	return upDeadTime;
    }
    public void setDeadTime(int time){
    	Date date = new Date(System.currentTimeMillis());
    	Calendar calendar = Calendar.getInstance();
    	calendar.add(Calendar.DATE, time);
    	date = calendar.getTime();
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    	String deadTime = df.format(date);
    	System.out.println("deadTime--"+deadTime);
    	this.deadTime = deadTime;
    	System.out.println("this.deadTime--"+this.deadTime);
    }
    public void setDeadTime(String deadTime) throws ParseException{
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(deadTime);
        String time = sdf.format(date);
    	this.deadTime = time;
    }
    
    public int getIsLegal(){
    	return isLegal;
    }
    public void setIsLegal(int isLegal){
    	this.isLegal = isLegal;
    }
    //--至此--


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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getUserVertify() {
        return userVertify;
    }

    public void setUserVertify(Integer userVertify) {
        this.userVertify = userVertify;
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

    public void setUpDeadTime(Date upDeadTime) {
        this.upDeadTime = upDeadTime;
    }
}
