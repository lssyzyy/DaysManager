package com.example.mypersonalmanager;

public class BeanDays {
    private int imageId;
    private String time;
    private String content;
    public BeanDays(){};
    public BeanDays(int imagedId,String time,String content){
        this.imageId=imagedId;
        this.time=time;
        this.content=content;
    }

    public int getImageId() {
        return imageId;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
