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


import org.xutils.x;

import java.util.Date;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by kiler on 2016/1/8.
 */
public class BaseActivity extends com.srmn.xwork.androidlib.ui.BaseActivity {


    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(context);

    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(context);


    }















}
