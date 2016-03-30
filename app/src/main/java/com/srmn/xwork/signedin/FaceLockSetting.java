package com.srmn.xwork.signedin;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.srmn.xwork.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;


@ContentView(R.layout.activity_face_lock_setting)
public class FaceLockSetting extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.rlFaceCheck)
    private RelativeLayout rlFaceCheck;
    @ViewInject(R.id.rlFaceReg)
    private RelativeLayout rlFaceReg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackButton();
        rlFaceCheck.setOnClickListener(this);
        rlFaceReg.setOnClickListener(this);
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
            case R.id.rlFaceCheck:
                gotoActivity(FaceCheck.class);
                break;
            case R.id.rlFaceReg:
                gotoActivity(FaceVerify.class);
                break;
        }
    }
}
