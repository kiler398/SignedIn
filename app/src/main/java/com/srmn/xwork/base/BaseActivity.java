package com.srmn.xwork.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ViewUtils;
import android.view.KeyEvent;

import com.srmn.xwork.signedin.HomeFragment;
import com.srmn.xwork.signedin.SignFragment;
import com.umeng.analytics.MobclickAgent;

import org.xutils.x;

import java.util.Date;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by kiler on 2016/1/8.
 */
public class BaseActivity extends AppCompatActivity {
    protected AppCompatActivity context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        x.view().inject(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(context);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(context);
        MobclickAgent.onPause(this);

    }

    public void gotoActivity(java.lang.Class cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }


    public void gotoActivity(java.lang.Class cls, int taskType) {
        Intent intent = new Intent(context, cls);
        intent.addFlags(taskType);
        startActivity(intent);
    }

    public void gotoActivityForResult(java.lang.Class cls, int requestCode) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("requestCode", requestCode);
        startActivityForResult(intent, requestCode);
    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setActionBarTitle(String actionBarTitle)
    {
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar!=null)
            actionBar.setTitle(actionBarTitle);
    }

    public void toggleActionBar()
    {
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar!=null)
        {
            if(actionBar.isShowing())
            {
                actionBar.hide();
            }
            else
            {
                actionBar.show();
            }
        }
    }

    public void showActionBar()
    {
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar!=null)
            actionBar.show();
    }
    public void hideActionBar()
    {
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar!=null)
            actionBar.hide();
    }

    public void showBackButton()
    {
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar!=null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void hideBackButton() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(false);
    }


}
