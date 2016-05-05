package com.water.jxwz.bean;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by ${xsy} on 2016/4/19.
 */
public class OfficerBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String riverNumber;

    public OfficerBean(String name, String riverNumber) {
        this.name = name;
        this.riverNumber = riverNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRiverNumber() {
        return riverNumber;
    }

    public void setRiverNumber(String riverNumber) {
        this.riverNumber = riverNumber;
    }
}
