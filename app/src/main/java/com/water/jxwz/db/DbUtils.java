package com.water.jxwz.db;

import android.text.TextUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ${xsy} on 2016/4/15.
 */
public class DbUtils {


    public static List<String> getImgUrl(){
       List<String> mUrList=new ArrayList<>();
        List<Photos> photoses = DataSupport.order("takePicTime").find(Photos.class);
        Collections.reverse(photoses);
        for (int i = 0; i <photoses.size() ; i++) {
            if (!TextUtils.isEmpty(photoses.get(i).getImgUrl())) {

                mUrList.add(photoses.get(i).getImgUrl());
            }
        }
        return mUrList;
    }

}
