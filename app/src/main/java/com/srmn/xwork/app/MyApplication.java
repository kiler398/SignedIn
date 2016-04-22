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
import com.srmn.xwork.cfg.Configuration;
import com.srmn.xwork.dao.DaoContainer;
import com.srmn.xwork.entities.PersonInfoEntity;
import com.srmn.xwork.utils.xfSDkUtil;
import com.tencent.bugly.crashreport.CrashReport;


import org.xutils.DbManager;
import org.xutils.ex.DbException;
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

    protected DbManager dbmanager;
    protected DaoContainer daoContainer;

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
        CrashReport.initCrashReport(this, Configuration.BuglyAppID, false);


        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                // 数据库的名字
                .setDbName("SignedIn")
                // 保存到指定路径
                // .setDbDir(new File(Environment.getExternalStorageDirectory().getAbsolutePath()))
                // 数据库的版本号
                .setDbVersion(13)
                // 数据库版本更新监听
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                        if (oldVersion < newVersion) {//升级判断,如果再升级就要再加两个判断,从1到3,从2到3
                            Log.e(TAG, "数据库版本更新了！");
                            try {
                                db.dropDb();
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });

        dbmanager = x.getDb(daoConfig);
        daoContainer = new DaoContainer(dbmanager);


        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        x.Ext.init(this);
        x.Ext.setDebug(true); // 是否输出debug日志
        TypefaceProvider.registerDefaultIconSets();
        instance = this;
        SpeechUtility.createUtility(this, "appid="+Configuration.XfAppID);

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

    public PersonInfoEntity getPersonInfo() {
        return this.daoContainer.getPersonInfoDaoInstance().getCurrentPersonInfo();
    }

    public String getFaceKey() {
        return getPersonInfo().getFaceCheckID();
    }

    public String getLoginUserID() {
        return getPersonInfo().getCode();
    }

    public void updatePersonInfo(PersonInfoEntity personinfo) {
        try {
            this.daoContainer.getPersonInfoDaoInstance().update(personinfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void  setFaceCheckEnable(boolean faceCheckEnable) {
        PersonInfoEntity personInfoEntity = getPersonInfo();
        personInfoEntity.setEnableFaceCheck(faceCheckEnable);
        try {
            this.daoContainer.getPersonInfoDaoInstance().update(personInfoEntity);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    public void setFaceKey(String newFaceKey) {
        PersonInfoEntity personInfoEntity = getPersonInfo();
        personInfoEntity.setFaceCheckID(newFaceKey);
        try {
            this.daoContainer.getPersonInfoDaoInstance().update(personInfoEntity);
        } catch (DbException e) {
            e.printStackTrace();
        }
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


    public GISLocation getCheckLocation() {


        PersonInfoEntity personInfoEntity = getPersonInfo();

        if(personInfoEntity==null)
            return null;

        if(personInfoEntity.getLocationAddress()==null)
            return null;

        GISLocation location = new GISLocation();

        location.setAddress(personInfoEntity.getLocationAddress());
        location.setLongitude(personInfoEntity.getLocationLng());
        location.setLatitude(personInfoEntity.getLocationLat());

        return location;
    }

    public boolean getFaceCheckEnable() {

        PersonInfoEntity personInfoEntity = getPersonInfo();

        if(personInfoEntity==null)
            return false;

        if(personInfoEntity.getEnableFaceCheck()==null)
            return false;

        return personInfoEntity.getEnableFaceCheck();
    }

    public boolean getVoiceCheckEnable() {

        PersonInfoEntity personInfoEntity = getPersonInfo();

        if(personInfoEntity==null)
            return false;

        if(personInfoEntity.getEnableVoiceCheck()==null)
            return false;

        return personInfoEntity.getEnableVoiceCheck();

    }

    public float getCheckLocationRange() {
        PersonInfoEntity personInfoEntity = getPersonInfo();

        if(personInfoEntity==null)
            return 0;

        if(personInfoEntity.getCheckRange()<0)
            return 0;

        return personInfoEntity.getCheckRange();

    }

    public String getVoiceKey() {
        PersonInfoEntity personInfoEntity = getPersonInfo();

        if(personInfoEntity==null)
            return "";

        if(personInfoEntity.getVoiceCheckID()==null)
            return "";

        return personInfoEntity.getVoiceCheckID();
    }
}
