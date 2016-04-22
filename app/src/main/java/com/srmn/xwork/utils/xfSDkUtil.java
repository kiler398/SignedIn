package com.srmn.xwork.utils;

import android.provider.Settings;

import com.srmn.xwork.androidlib.utils.PackageUtils;
import com.srmn.xwork.androidlib.utils.SharedPrefsUtil;
import com.srmn.xwork.app.MyApplication;

/**
 * Created by kiler on 2016/1/5.
 */
public class xfSDkUtil {

    public static final String DEVICE_ID = "DeviceID";

    public static String getDeviceID()
    {
        return SharedPrefsUtil.getStringValue(MyApplication.SharedPrefsNAME, DEVICE_ID,"");
    }
    public static void setDeviceID(String value)
    {
        SharedPrefsUtil.putStringValue(MyApplication.SharedPrefsNAME,DEVICE_ID,value);
    }


    public static String getUNDeviceID()
    {
        String androidId = Settings.Secure.getString(MyApplication.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    public static String getAppID()
    {
        return PackageUtils.readApplicationMetaDataStringValue("IFLYTEK_APPKEY",MyApplication.getInstance());
    }


}
