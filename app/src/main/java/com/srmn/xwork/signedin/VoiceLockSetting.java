package com.srmn.xwork.signedin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.srmn.xwork.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;


@ContentView(R.layout.activity_voice_lock_setting)
public class VoiceLockSetting extends BaseActivity implements View.OnClickListener {


    @ViewInject(R.id.rlVoiceCheck)
    private RelativeLayout rlVoiceCheck;
    @ViewInject(R.id.rlVoiceReg)
    private RelativeLayout rlVoiceReg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackButton();

        rlVoiceCheck.setOnClickListener(this);
        rlVoiceReg.setOnClickListener(this);
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
            case R.id.rlVoiceCheck:
                gotoActivity(VoiceCheck.class);
                break;
            case R.id.rlVoiceReg:
                gotoActivity(VoiceVerify.class);
                break;
        }
    }
}
