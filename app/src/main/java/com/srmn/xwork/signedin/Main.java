package com.srmn.xwork.signedin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.srmn.xwork.androidlib.gis.GISAMapLocationTask;
import com.srmn.xwork.androidlib.gis.GISLocation;
import com.srmn.xwork.androidlib.gis.GISLocationListener;
import com.srmn.xwork.androidlib.gis.GISLocationOption;
import com.srmn.xwork.app.MyApplication;
import com.srmn.xwork.base.BaseActivity;
import com.srmn.xwork.ui.TabEntity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;



@ContentView(R.layout.activity_main)
public class Main extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int RequestCode_VerifyFace = 1;
    public static final int RequestCode_VerifyVoice = 2;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    @ViewInject(R.id.tabMain)
    private CommonTabLayout tabMain;
    @ViewInject(R.id.vpMain)
    private ViewPager vpMain;
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    @ViewInject(R.id.drawer_layout)
    private DrawerLayout drawer;
    @ViewInject(R.id.nav_view)
    private NavigationView navigationView;
    private TimeReceiver mBroadcastReceiver;
    private ConnectivityManager mConnectivityManager;
    private NetworkInfo netInfo;
    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isAvailable()) {

                    /////////////网络连接
                    String name = netInfo.getTypeName();

                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        setNetworkState(ConnectivityManager.TYPE_WIFI);

                    } else if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                        /////有线网络
                        setNetworkState(ConnectivityManager.TYPE_ETHERNET);
                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        /////////3g网络
                        setNetworkState(ConnectivityManager.TYPE_MOBILE);
                    }
                } else {
                    ////////网络断开
                    setNetworkState(ConnectivityManager.TYPE_DUMMY);
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        mTabEntities.add(new TabEntity("首页", R.drawable.ic_home_black_18dp,R.drawable.ic_home_grey600_18dp, HomeFragment.newInstance("","")));
        mTabEntities.add(new TabEntity("签到", R.drawable.ic_alarm_on_black_18dp,R.drawable.ic_alarm_on_grey600_18dp,SignFragment.newInstance("","")));
        mTabEntities.add(new TabEntity("记录", R.drawable.ic_today_black_18dp,R.drawable.ic_today_grey600_18dp,RecordFragment.newInstance("","")));
        mTabEntities.add(new TabEntity("设置", R.drawable.ic_wallet_travel_black_18dp,R.drawable.ic_wallet_travel_grey600_18dp,SettingFragment.newInstance("","")));


        vpMain.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        initMainTab();

        MyApplication.getInstance().autoUpdate(this);

        mBroadcastReceiver = new TimeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mBroadcastReceiver, intentFilter);

        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myNetReceiver, mFilter);

        GISLocationOption gisOption = MyApplication.getInstance().BuildSingleGISLocationOption();

        GISAMapLocationTask locationTask = new GISAMapLocationTask(gisOption, new GISLocationListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onLocationChanged(GISLocation location) {
                Fragment fragment = getCurrentFragment();
                if (fragment instanceof HomeFragment) {
                    ((HomeFragment) fragment).setLocation(location);
                }
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
        });

        locationTask.start();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        unregisterReceiver(myNetReceiver);
    }

    public Fragment getCurrentFragment() {
        return ((MyPagerAdapter) this.vpMain.getAdapter()).getItem(this.vpMain.getCurrentItem());
    }

    @Override
    protected void onStart() {
        super.onStart();
        //注册EventBus
        //EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //取消EventBus
        //EventBus.getDefault().unregister(this);
    }

    private void initMainTab() {
        tabMain.setTabData(mTabEntities);
        tabMain.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                gotoFrame(position);
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 0) {
                }
            }
        });

        vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabMain.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //
    }

    protected int dp2px(float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            gotoFrame(0);

        } else if (id == R.id.nav_signin) {
            gotoFrame(1);
        } else if (id == R.id.nav_calendar) {
            gotoFrame(2);
        } else if (id == R.id.nav_setting) {
            gotoFrame(3);
        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_about) {
            gotoActivity(About.class);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
                case RequestCode_VerifyFace:

                    MyApplication.getInstance().setLastFaceVerifyTime(new Date());
                    Fragment fragment = getCurrentFragment();

                    if (fragment instanceof SignFragment) {
                        ((SignFragment) fragment).RefreshUI();
                    }

                    break;
                case RequestCode_VerifyVoice: {
                    MyApplication.getInstance().setLastVoiceVerifyTime(new Date());
                    Fragment fragment2 = getCurrentFragment();

                    if (fragment2 instanceof SignFragment) {
                        ((SignFragment) fragment2).RefreshUI();
                    }

                }
                break;
                default:

                    break;
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }


    }

    public void gotoFrame(int inIndx) {
        vpMain.setCurrentItem(inIndx);
    }

    public void setNetworkState(int networkState) {
        Fragment fragment = getCurrentFragment();

        if (fragment instanceof HomeFragment) {
            ((HomeFragment) fragment).setNetworkState(networkState);
        }
    }

    public class TimeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
                Fragment fragment = getCurrentFragment();

                if (fragment instanceof HomeFragment) {
                    ((HomeFragment) fragment).setDateTime(new Date());
                } else if (fragment instanceof SignFragment) {
                    ((SignFragment) fragment).setDateTime(new Date());
                }
            }
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {



        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mTabEntities.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabEntities.get(position).getTabTitle();
        }

        @Override
        public Fragment getItem(int position) {
            return ((TabEntity)mTabEntities.get(position)).getFragment();
        }
    }
}
