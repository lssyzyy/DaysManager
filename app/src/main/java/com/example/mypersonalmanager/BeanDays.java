package com.example.mypersonalmanager;

public class BeanDays {
    private String dayid;
    private String time;
    private String content;
    public BeanDays(){};
    public BeanDays(String dayid,String time,String content){
        this.dayid=dayid;
        this.time=time;
        this.content=content;
    }

    public String getDayid() {
        return dayid;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }


    public void setDayid(String dayid) {
        this.dayid = dayid;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
