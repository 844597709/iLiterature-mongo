package com.swust.kelab.domain;

import com.mysql.jdbc.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class User {
    private int userId;
    private String userTrueName;
    private String userName;
    private String password;
    private int role;
    private String position;
    private String contactNum;
    private String email;
    private String department;
    private String jobNum;
    private String rejectReason;
    private int verify;

    //--zd--
    private int time;
    private String deadTime;
    private Date upDeadTime;
    private int isLegal;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getDeadTime() {
        return deadTime;
    }

    public Date getUpDeadTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        if (!StringUtils.isNullOrEmpty(deadTime)) {
            try {
                this.upDeadTime = df.parse(deadTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.upDeadTime;
    }

    public void setDeadTime(int time) {
        //System.out.println("time--"+time);
        Date date = new Date(System.currentTimeMillis());
        //System.out.println("date--"+date);
        Calendar calendar = Calendar.getInstance();
        //System.out.println("Calendar.DATE--"+Calendar.DATE);
        calendar.add(Calendar.DATE, time);
        date = calendar.getTime();
        //System.out.println("calendar.getTime()--"+date);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String deadTime = df.format(date);
        System.out.println("deadTime--" + deadTime);
        this.deadTime = deadTime;
        System.out.println("this.deadTime--" + this.deadTime);
    }

    public void setDeadTime(String deadTime) {
        /*System.out.println("deadTime1--"+deadTime);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    	String deadTime = df.format(time);
    	System.out.println("deadTime2--"+deadTime);*/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!StringUtils.isNullOrEmpty(deadTime)) {
            try {
                Date date = sdf.parse(deadTime);
                String time = sdf.format(date);
                this.deadTime = time;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getIsLegal() {
        return isLegal;
    }

    public void setIsLegal(int isLegal) {
        this.isLegal = isLegal;
    }
    //--至此--

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserTrueName() {
        return userTrueName;
    }

    public void setUserTrueName(String userTrueName) {
        this.userTrueName = userTrueName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public int getVerify() {
        return verify;
    }

    public void setVerify(int verify) {
        this.verify = verify;
    }

}
