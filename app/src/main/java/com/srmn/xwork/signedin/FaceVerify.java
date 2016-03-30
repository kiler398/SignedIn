package com.srmn.xwork.signedin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.srmn.xwork.androidlib.utils.RandomUtil;
import com.srmn.xwork.androidlib.utils.StringUtil;
import com.srmn.xwork.app.MyApplication;
import com.srmn.xwork.base.BaseActivity;


import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;


@ContentView(R.layout.activity_face_verify)
public class FaceVerify extends BaseActivity implements View.OnClickListener {


    @ViewInject(R.id.btnReg)
    private BootstrapButton btnReg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackButton();
        btnReg.setOnClickListener(this);
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
            case R.id.btnReg:

                if (StringUtil.isNullOrEmpty(MyApplication.getInstance().getLoginUserID())) {
                    MyApplication.getInstance().showShortToastMessage("当前用户名为空，请先在用户设置里面设置用户名");
                    return;
                }

                String newFaceKey = MyApplication.getInstance().getLoginUserID() + RandomUtil.generateString(8);
                MyApplication.getInstance().setFaceKey(newFaceKey);
                gotoActivity(FaceReg.class);
                break;
        }
    }
}
