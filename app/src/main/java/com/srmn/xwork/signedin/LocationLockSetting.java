package com.srmn.xwork.signedin;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.srmn.xwork.androidlib.gis.GISAMapLocationTask;
import com.srmn.xwork.androidlib.gis.GISLocation;
import com.srmn.xwork.androidlib.gis.GISLocationListener;
import com.srmn.xwork.androidlib.gis.GISLocationOption;
import com.srmn.xwork.app.MyApplication;
import com.srmn.xwork.base.BaseActivity;
import com.srmn.xwork.entities.PersonInfoEntity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.Date;


@ContentView(R.layout.activity_location_lock_setting)
public class LocationLockSetting extends BaseActivity implements View.OnClickListener, GISLocationListener {


    @ViewInject(R.id.rllocation)
    private RelativeLayout rllocation;
    @ViewInject(R.id.rlLocationRange)
    private RelativeLayout rlLocationRange;
    @ViewInject(R.id.rlCheckTime)
    private RelativeLayout rlCheckTime;

    @ViewInject(R.id.txtLocationRange)
    private TextView txtLocationRange;
    @ViewInject(R.id.txtlocation)
    private TextView txtlocation;
    @ViewInject(R.id.txtCheckTime)
    private TextView txtCheckTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackButton();
        rllocation.setOnClickListener(this);
        rlLocationRange.setOnClickListener(this);
        rlCheckTime.setOnClickListener(this);

        txtLocationRange.setText(MyApplication.getInstance().getCheckLocationRange() + "(米)");
        if (MyApplication.getInstance().getCheckLocation() != null)
            txtlocation.setText(MyApplication.getInstance().getCheckLocation().toString());
        else
            txtlocation.setText("");
        txtCheckTime.setText(MyApplication.getInstance().getCheckDayTimeRange());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlLocationRange:

                final EditText et = new EditText(context);
                et.setRawInputType(InputType.TYPE_CLASS_NUMBER);//设置进入的时候显示为number模式
                et.setText(MyApplication.getInstance().getCheckLocationRange() + "");


                new android.app.AlertDialog.Builder(context)
                        .setTitle("请输入签到半径：")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MyApplication.getInstance().setCheckLocationRange(Integer.parseInt(et.getText().toString()));
                                txtLocationRange.setText(MyApplication.getInstance().getLo() + "(米)");
                            }
                        }).setNegativeButton("取消", null)
                        .show();


                break;
            case R.id.rllocation:
                final View v = LayoutInflater.from(context).inflate(R.layout.dialog_location_select, null);

                final TextView dtxtLocation = (TextView) v.findViewById(R.id.txtLocation);
                final TextView dtxtlat = (TextView) v.findViewById(R.id.txtlat);
                final TextView dtxtlng = (TextView) v.findViewById(R.id.txtlng);

                final LocationLockSetting lactivity = (LocationLockSetting) context;

                PersonInfoEntity location = MyApplication.getInstance().getPersonInfo();

                if (location == null) {
                    dtxtLocation.setText("");
                    dtxtlat.setText("0");
                    dtxtlng.setText("0");
                } else {
                    dtxtLocation.setText(location.getLocationAddress() + "");
                    dtxtlat.setText(location.getLocationLat() + "");
                    dtxtlng.setText(location.getLocationLng() + "");
                }


                new android.app.AlertDialog.Builder(context)
                        .setTitle("请输入签到地址：")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(v)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                PersonInfoEntity personInfo = MyApplication.getInstance().getPersonInfo();

                                personInfo.setLocationAddress(dtxtLocation.getText().toString());
                                personInfo.setLocationLng(Double.parseDouble(dtxtlng.getText().toString()));
                                personInfo.setLocationLat(Double.parseDouble(dtxtlat.getText().toString()));

                                //MyApplication.getInstance().setCheckLocation(location);

                                txtlocation.setText(MyApplication.getInstance().getCheckLocation().toString());

                            }
                        }).setNeutralButton("定位获取当前位置",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GISLocationOption locationOption = MyApplication.getInstance().BuildSingleGISLocationOption();

                                GISAMapLocationTask gisaMapLocationTask = new GISAMapLocationTask(locationOption, lactivity);

                                gisaMapLocationTask.start();
                            }
                        })

                        .setNegativeButton("取消", null)
                        .show();


                break;



        }
    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onLocationChanged(GISLocation location) {
        if (location != null) {
            if (location.getErrorCode() == 0) {



                MyApplication.getInstance().showLongToastMessage("定位成功！");


            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("高德定位错误", "定位错误, 错误码:"
                        + location.getErrorCode() + ", 错误信息:"
                        + location.getErrorInfo());

                MyApplication.getInstance().showLongToastMessage("定位错误：" + location.getErrorInfo());
            }
        }

        txtlocation.setText(MyApplication.getInstance().getCheckLocation().toString());
    }

    @Override
    public void onEnd() {

    }

    @Override
    public void onFailed(String errorInfo) {

    }

    @Override
    public void onProgress(String message, Integer progressIndex) {

    }
}
