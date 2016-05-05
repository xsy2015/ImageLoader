package com.water.jxwz.utils;

/**
 * Created by ${xsy} on 2016/4/15.
 */
public class SharedInfoUtils {
    private final static String USER_PREFS_NAME = "jxwz_user";
    private static SharedPrefsHelper mUserPrefs;
    /** 用户登录帐号 */
    public static final String USER_NAME= "userName";
    /** 用户登录密码 */
    public static final String USER_PWD = "userPwd";
    /** 经度 */
    public static final String LONGITUDE = "longitude";
    /** 维度 */
    public static final String LATITUDE = "latitude";
    /** USER_TOKEN */
    public static final String USER_TOKEN = "token";
    /** 是否自动登录 */
    public static final String IS_AUTOLOGIN = "isAutoLogin";

    private String userName;
    private String userPwd;
    private String longitude;
    private String latitude;


    private static SharedPrefsHelper getInstance() {
        if (mUserPrefs == null) {
            mUserPrefs = new SharedPrefsHelper(USER_PREFS_NAME);
        }
        return mUserPrefs;
    }

//    public static String getUserAccount() {
//        return getInstance().getStringValue(USER_NAME, "");
//    }
//
//    public static String getUserPwd() {
//        return getInstance().getStringValue(USER_PWD, "");
//    }
//
//    public static String getLONGITUDE() {
//        return getInstance().getStringValue(LONGITUDE, "");
//    }
//
//    public static String getLATITUDE() {
//        return getInstance().getStringValue(LATITUDE, "");
//    }


    public static String getUserName() {
        return getInstance().getStringValue(USER_NAME, "");
    }

    public static void setUserName(String userName) {
       getInstance().getEditor().putString(USER_NAME, userName).commit();
    }

    public static String getUserPwd() {
        return getInstance().getStringValue(USER_PWD, "");
    }

    public static void setUserPwd(String userPwd) {
        getInstance().getEditor().putString(USER_PWD, userPwd).commit();
    }

    public static String getLongitude() {
        return getInstance().getStringValue(LONGITUDE, "");
    }

    public static void setLongitude(String longitude) {
        getInstance().getEditor().putString(LONGITUDE, longitude).commit();
    }

    public static String getLatitude() {
        return getInstance().getStringValue(LATITUDE, "");
    }

    public static void setLatitude(String latitude) {
        getInstance().getEditor().putString(LATITUDE, latitude).commit();
    }
    /**
     * 获取token
     *
     * @return
     */
    public static String getToken() {
        return getInstance().getStringValue(USER_TOKEN, "");
    }

    /**
     * 保存token
     *
     * @return
     */
    public static void setToken(String token) {
        getInstance().getEditor().putString(USER_TOKEN, token).commit();
    }

    /**
     * 保存是否自动登录
     *
     * @return
     */
    public static void setIsAutoLogin(boolean isAutoLogin) {
        getInstance().getEditor().putBoolean(IS_AUTOLOGIN, isAutoLogin)
                .commit();
    }

    /**
     * 获取是否自动登录
     *
     * @return
     */
    public static boolean getIsAutoLogin() {
        return getInstance().getBooleanValue(IS_AUTOLOGIN, true);
    }
}


