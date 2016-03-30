package com.srmn.xwork.signedin;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.srmn.xwork.app.MyApplication;
import com.srmn.xwork.base.BaseActivity;


import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_my_setting)
public class MySetting extends BaseActivity implements View.OnClickListener {


    @ViewInject(R.id.rlUserLoginID)
    private RelativeLayout rlUserLoginID;
    @ViewInject(R.id.txtLoginID)
    private TextView txtLoginID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackButton();
        txtLoginID.setText(MyApplication.getInstance().getLoginUserID());
        rlUserLoginID.setOnClickListener(this);


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
            case R.id.rlUserLoginID:

                final EditText et = new EditText(context);
                et.setText(MyApplication.getInstance().getLoginUserID());

                new android.app.AlertDialog.Builder(context)
                        .setTitle("请输入用户名：")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MyApplication.getInstance().setLoginUserID(et.getText().toString());
                                txtLoginID.setText(MyApplication.getInstance().getLoginUserID());
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();


                break;
        }
    }
}
