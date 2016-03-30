package com.srmn.xwork.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.iflytek.cloud.SpeechUtility;
import com.srmn.xwork.androidlib.gis.GISLocation;
import com.srmn.xwork.androidlib.gis.GISLocationOption;
import com.srmn.xwork.androidlib.utils.GsonUtil;
import com.srmn.xwork.androidlib.utils.IOUtil;
import com.srmn.xwork.androidlib.utils.PackageUtils;
import com.srmn.xwork.androidlib.utils.SharedPrefsUtil;
import com.srmn.xwork.androidlib.utils.StringUtil;
import com.srmn.xwork.enity.DayTimeRange;
import com.srmn.xwork.utils.xfSDkUtil;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.update.UmengUpdateAgent;

import org.xutils.x;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by kiler on 2016/1/3.
 */
public class MyApplication extends  com.srmn.xwork.androidlib.ui.MyApplication {


    public static final String TAG = "MyApplication";

    public static final String SharedPrefsNAME = "SGI";

    private static MyApplication instance;



    public static MyApplication getInstance() {
        return instance;
    }

    public static void setInstance(MyApplication instance) {
        MyApplication.instance = instance;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    public static boolean getIsTest() {
        return PackageUtils.readApplicationMetaDataStringValue("istest", getInstance()).equals("1");
    }

    public void showShortToastMessage(String message)
    {
        Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();;
    }

    public void showLongToastMessage(String message)
    {
        Toast.makeText(this.getApplicationContext(),message,Toast.LENGTH_LONG).show();;
    }

    @Override public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(this, "900017974", false);

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        x.Ext.init(this);
        x.Ext.setDebug(true); // 是否输出debug日志
        TypefaceProvider.registerDefaultIconSets();
        instance = this;
        SpeechUtility.createUtility(this, "appid=" + xfSDkUtil.getAppID());
//        copyofflineMap();

    }

    private String getOffLineMapPath()
    {
        if(IOUtil.isSdCardExist()) {
            return IOUtil.getSdCardPath() + "/amap/mini_mapv3/vmap/";
        }
        return "";
    }

    private void copyofflineMap() {
        String amapOfflinePath = getOffLineMapPath();

        if(!amapOfflinePath.equals(""))
        {
            copyofflineMapCity("changsha",amapOfflinePath);
            copyofflineMapCity("loudi",amapOfflinePath);
            copyofflineMapCity("xiangtan",amapOfflinePath);
            copyofflineMapCity("yiyang",amapOfflinePath);
            copyofflineMapCity("yueyang",amapOfflinePath);
        }
    }

    private void copyofflineMapCity(String cityName,String amapOfflinePath) {

        if(!IOUtil.fileIsExists(amapOfflinePath+cityName+".dat"))
        {
            try {
                IOUtil.copyBigDataToSD(cityName+".dat",amapOfflinePath+cityName+".dat");
            } catch (IOException e) {
                Log.e(TAG,"拷贝离线地图出错："+e.getMessage());
                e.printStackTrace();
            }
        }

    }

    public GISLocation getCheckLocation() {
        String json = SharedPrefsUtil.getStringValue(SharedPrefsNAME, "CheckLocation", "");

        if (StringUtil.isNullOrEmpty(json))
            return null;

        return GsonUtil.DeserializerSingleDataResult(json, GISLocation.class);
    }

    public void setCheckLocation(GISLocation checkLocation) {
        if (checkLocation == null)
            SharedPrefsUtil.putStringValue(SharedPrefsNAME, "CheckLocation", "");
        else
            SharedPrefsUtil.putStringValue(SharedPrefsNAME, "CheckLocation", GsonUtil.getGson().toJson(checkLocation));
    }

    public DayTimeRange getCheckDayTimeRange()
    {
        String json = SharedPrefsUtil.getStringValue(SharedPrefsNAME, "CheckDayTimeRange", "");

        if (StringUtil.isNullOrEmpty(json))
            return new DayTimeRange();

        return GsonUtil.DeserializerSingleDataResult(json, DayTimeRange.class);
    }

    public void setCheckDayTimeRange(DayTimeRange checkDayTimeRange) {
        if (checkDayTimeRange == null)
            SharedPrefsUtil.putStringValue(SharedPrefsNAME, "CheckDayTimeRange", GsonUtil.getGson().toJson(new DayTimeRange()));
        else
            SharedPrefsUtil.putStringValue(SharedPrefsNAME, "CheckDayTimeRange", GsonUtil.getGson().toJson(checkDayTimeRange));
    }


    public int getCheckLocationRange() {
        return SharedPrefsUtil.getIntValue(SharedPrefsNAME, "CheckLocationRange", 50);
    }

    public void setCheckLocationRange(int checkLocationRange) {
        SharedPrefsUtil.putIntValue(SharedPrefsNAME, "CheckLocationRange", checkLocationRange);
    }


    public String getLoginUserID() {
        return SharedPrefsUtil.getStringValue(SharedPrefsNAME, "LoginUserID", "");
    }

    public void setLoginUserID(String loginUserID) {
        SharedPrefsUtil.putStringValue(SharedPrefsNAME, "LoginUserID", loginUserID);
    }

    public String getFaceKey() {
        return SharedPrefsUtil.getStringValue(SharedPrefsNAME, "FaceKey", "");
    }

    public void setFaceKey(String faceKey) {
        SharedPrefsUtil.putStringValue(SharedPrefsNAME, "FaceKey", faceKey);
    }


    public String getVoiceKey() {
        return SharedPrefsUtil.getStringValue(SharedPrefsNAME, "VoiceKey", "");
    }

    public void setVoiceKey(String voiceKey) {
        SharedPrefsUtil.putStringValue(SharedPrefsNAME, "VoiceKey", voiceKey);
    }


    public boolean getFaceCheckEnable() {
        return SharedPrefsUtil.getBooleanValue(SharedPrefsNAME, "FaceCheckEnable", false);
    }

    public void setFaceCheckEnable(boolean faceCheckEnable) {
        SharedPrefsUtil.putBooleanValue(SharedPrefsNAME, "FaceCheckEnable", faceCheckEnable);
    }


    public boolean getVoiceCheckEnable() {
        return SharedPrefsUtil.getBooleanValue(SharedPrefsNAME, "VoiceCheckEnable", false);
    }

    public void setVoiceCheckEnable(boolean voiceCheckEnable) {
        SharedPrefsUtil.putBooleanValue(SharedPrefsNAME, "VoiceCheckEnable", voiceCheckEnable);
    }


    public boolean getTodayIsVerify() {
        if (!getTodayLocationIsVerify()) {
            return false;
        }
        if (!getTodayFaceIsVerify()) {
            return false;
        }
        if (!getTodayVoiceIsVerify()) {
            return false;
        }
        return true;
    }

    public boolean getTodayLocationIsVerify() {
        Date lastLocationVerifyTime = getLastLocationVerifyTime();
        long timechange = (new Date().getTime() - lastLocationVerifyTime.getTime()) / 1000;

        //地点签到时间在10分钟以内为有效
        if (timechange < 10 * 60) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getTodayFaceIsVerify() {

        if (!getTodayLocationIsVerify()) {
            return false;
        }

        Date lastFaceVerifyTime = getLastFaceVerifyTime();
        long timechange = (new Date().getTime() - lastFaceVerifyTime.getTime()) / 1000;

        //地点签到时间在10分钟以内为有效
        if (timechange < 10 * 60) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getTodayVoiceIsVerify() {

        if (!getTodayLocationIsVerify()) {
            return false;
        }


        Date lastVoiceVerifyTime = getLastVoiceVerifyTime();
        long timechange = (new Date().getTime() - lastVoiceVerifyTime.getTime()) / 1000;

        //地点签到时间在10分钟以内为有效
        if (timechange < 10 * 60) {
            return true;
        } else {
            return false;
        }
    }


    public Date getLastLocationVerifyTime() {
        return new Date(SharedPrefsUtil.getLongValue(SharedPrefsNAME, "LastLocationVerifyTime", 0));
    }

    public void setLastLocationVerifyTime(Date lastLocationVerifyTime) {
        SharedPrefsUtil.putLongValue(SharedPrefsNAME, "LastLocationVerifyTime", lastLocationVerifyTime.getTime());
    }

    public Date getLastFaceVerifyTime() {
        return new Date(SharedPrefsUtil.getLongValue(SharedPrefsNAME, "LastFaceVerifyTime", 0));
    }

    public void setLastFaceVerifyTime(Date lastFaceVerifyTime) {
        SharedPrefsUtil.putLongValue(SharedPrefsNAME, "LastFaceVerifyTime", lastFaceVerifyTime.getTime());
    }

    public Date getLastVoiceVerifyTime() {
        return new Date(SharedPrefsUtil.getLongValue(SharedPrefsNAME, "LastVoiceVerifyTime", 0));
    }

    public void setLastVoiceVerifyTime(Date lastVoiceVerifyTime) {
        SharedPrefsUtil.putLongValue(SharedPrefsNAME, "LastVoiceVerifyTime", lastVoiceVerifyTime.getTime());
    }


    public void autoUpdate(Context context) {


        UmengUpdateAgent.setDeltaUpdate(false);
        UmengUpdateAgent.setUpdateOnlyWifi(true);
        UmengUpdateAgent.update(context);


    }


    public GISLocationOption BuildGISLocationOption() {

        GISLocationOption locationOption = new GISLocationOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        locationOption.setLocationMode(GISLocationOption.LocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        locationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        locationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        locationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        locationOption.setMockEnable(true);
        //设置定位间隔,单位毫秒,默认为2000ms
        locationOption.setInterval(2000);

        return locationOption;

    }


    public GISLocationOption BuildSingleGISLocationOption() {

        GISLocationOption locationOption = new GISLocationOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        locationOption.setLocationMode(GISLocationOption.LocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        locationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        locationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        locationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        locationOption.setMockEnable(true);

        return locationOption;

    }

    public String getVersionName() {
        return PackageUtils.getVersion(MyApplication.getContext());
    }
}
