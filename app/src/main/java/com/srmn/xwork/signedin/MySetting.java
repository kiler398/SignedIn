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
import com.srmn.xwork.entities.PersonInfoEntity;


import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_my_setting)
public class MySetting extends BaseActivity implements View.OnClickListener {


    @ViewInject(R.id.rlUserLoginID)
    private RelativeLayout rlUserLoginID;
    @ViewInject(R.id.rlUserName)
    private RelativeLayout rlUserName;
    @ViewInject(R.id.rlUserOrgName)
    private RelativeLayout rlUserOrgName;
    @ViewInject(R.id.rlUserPostionName)
    private RelativeLayout rlUserPostionName;


    @ViewInject(R.id.txtLoginID)
    private TextView txtLoginID;
    @ViewInject(R.id.txtUserName)
    private TextView txtUserName;
    @ViewInject(R.id.txtOrgName)
    private TextView txtOrgName;
    @ViewInject(R.id.txtPostionName)
    private TextView txtPostionName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackButton();
        txtLoginID.setText(MyApplication.getInstance().getPersonInfo().getCode());
        txtPostionName.setText(MyApplication.getInstance().getPersonInfo().getPostionName());
        txtOrgName.setText(MyApplication.getInstance().getPersonInfo().getOrgName());
        txtUserName.setText(MyApplication.getInstance().getPersonInfo().getName());
        rlUserLoginID.setOnClickListener(this);
        rlUserName.setOnClickListener(this);
        rlUserOrgName.setOnClickListener(this);
        rlUserPostionName.setOnClickListener(this);

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
                final EditText etUserCode = new EditText(context);
                etUserCode.setText(MyApplication.getInstance().getPersonInfo().getCode());

                new android.app.AlertDialog.Builder(context)
                        .setTitle("请输入用户编码：")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(etUserCode)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                PersonInfoEntity personinfo = MyApplication.getInstance().getPersonInfo();
                                personinfo.setCode(etUserCode.getText().toString());
                                MyApplication.getInstance().updatePersonInfo(personinfo);
                                txtLoginID.setText(personinfo.getCode());

                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.rlUserName:
                final EditText etUserName = new EditText(context);
                etUserName.setText(MyApplication.getInstance().getPersonInfo().getName());

                new android.app.AlertDialog.Builder(context)
                        .setTitle("请输入用户名：")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(etUserName)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                PersonInfoEntity personinfo = MyApplication.getInstance().getPersonInfo();
                                personinfo.setName(etUserName.getText().toString());
                                MyApplication.getInstance().updatePersonInfo(personinfo);
                                txtUserName.setText(personinfo.getName());
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();


                break;
            case R.id.rlUserOrgName:

                final EditText etUserOrgName = new EditText(context);
                etUserOrgName.setText(MyApplication.getInstance().getPersonInfo().getOrgName());

                new android.app.AlertDialog.Builder(context)
                        .setTitle("请输入公司名：")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(etUserOrgName)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                PersonInfoEntity personinfo = MyApplication.getInstance().getPersonInfo();
                                personinfo.setOrgName(etUserOrgName.getText().toString());
                                MyApplication.getInstance().updatePersonInfo(personinfo);
                                txtOrgName.setText(personinfo.getOrgName());
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();


                break;
            case R.id.rlUserPostionName:

                final EditText etUserPostionName = new EditText(context);
                etUserPostionName.setText(MyApplication.getInstance().getPersonInfo().getPostionName());

                new android.app.AlertDialog.Builder(context)
                        .setTitle("请输入用户名：")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(etUserPostionName)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                PersonInfoEntity personinfo = MyApplication.getInstance().getPersonInfo();
                                personinfo.setPostionName(etUserPostionName.getText().toString());
                                MyApplication.getInstance().updatePersonInfo(personinfo);
                                txtPostionName.setText(personinfo.getPostionName());
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();


                break;
        }
    }
}
