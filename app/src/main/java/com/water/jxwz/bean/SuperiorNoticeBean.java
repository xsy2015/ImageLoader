package com.water.jxwz.bean;

import java.io.Serializable;

/**
 * Created by ${xsy} on 2016/4/18.
 */
public class SuperiorNoticeBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private String time;
    private String content;

    public SuperiorNoticeBean(String title,String time,String content){
        this.title=title;
        this.time=time;
        this.content=content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
