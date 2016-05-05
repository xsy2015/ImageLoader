package com.water.jxwz.db;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by ${xsy} on 2016/4/14.
 */
public class Photos extends DataSupport {
    private String takePicTime;
    private String imgUrl;
    private String imgName;
    @Column(defaultValue = "0")
    private int isUpload;//0:已上传 1：没上传
    @Column(defaultValue = "0")
    private int imgStatus;//0:干净 1：脏 2:取消
    private String mapX;//经度
    private String mapY;//纬度
    private int id;
    private int section;
    @Column(defaultValue = "false")
    public boolean isSeleted;

    public String getTakePicTime() {
        return takePicTime;
    }

    public void setTakePicTime(String takePicTime) {
        this.takePicTime = takePicTime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(int isUpload) {
        this.isUpload = isUpload;
    }

    public int getImgStatus() {
        return imgStatus;
    }

    public void setImgStatus(int imgStatus) {
        this.imgStatus = imgStatus;
    }

    public String getMapX() {
        return mapX;
    }

    public void setMapX(String mapX) {
        this.mapX = mapX;
    }

    public String getMapY() {
        return mapY;
    }

    public void setMapY(String mapY) {
        this.mapY = mapY;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public boolean isSeleted() {
        return isSeleted;
    }

    public void setIsSeleted(boolean isSeleted) {
        this.isSeleted = isSeleted;
    }
    public void setSeleted(boolean isSeleted) {
        this.isSeleted = isSeleted;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }
}
