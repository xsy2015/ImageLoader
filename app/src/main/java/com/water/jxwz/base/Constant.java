package com.water.jxwz.base;

/**
 * Created by ${xsy} on 2016/4/13.
 */
public interface Constant {

    public static final String BASE_URL ="http://120.26.149.142:8036/eagle/image/doImage";
    /** Log打印开关，debug版本下打开，release版本下关闭 */
    public final static boolean DEBUG = true;
    public final static String TAG = "JXWZ";
    // 调用系统照相机的requestCode
    public static final int PHOTO_REQUEST_CAMERA = 0x001;
    // 调用系统(或者自定相册)照相册的requestCode
    public static final int PHOTO_REQUEST_GALLERY = 0x002;
    // 调用系统剪切的requestCode
    public static final int PHOTO_REQUEST_CROP = 0x003;
    /** 缓存 */
    public static final String HHXH_CACHEDIR ="/jxwz/caches/";
    //从拍照界面进入显示Img页面
    public static final String FROM_CAMERA ="from_camera";
    //从Img列表界面进入显示Img页面
    public static final String FROM_PICTURE_LIST ="from_picture_list";
    //从Img列表界面点击预览显示Img页面
    public static final String FROM_PICTURE_PREVIEW ="from_picture_preview";
}
