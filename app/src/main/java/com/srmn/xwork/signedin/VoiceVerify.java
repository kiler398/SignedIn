package com.srmn.xwork.signedin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeakerVerifier;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechListener;
import com.srmn.xwork.androidlib.utils.RandomUtil;
import com.srmn.xwork.androidlib.utils.StringUtil;
import com.srmn.xwork.app.MyApplication;
import com.srmn.xwork.base.BaseActivity;
import com.srmn.xwork.base.VoiceActivity;
import com.srmn.xwork.entities.PersonInfoEntity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;


@ContentView(R.layout.activity_voice_verify)
public class VoiceVerify extends BaseActivity implements View.OnClickListener {


    @ViewInject(R.id.btnReg)
    private BootstrapButton btnReg;

    //private static final int PWD_TYPE_NUM = 3;

    // 声纹识别对象
//    private SpeakerVerifier mVerifier;
//
//    ProgressDialog dialogInit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackButton();

        btnReg.setOnClickListener(this);

//        dialogInit  = ProgressDialog.show(this, "提示", "声纹识别引擎初始化中", true,
//                true, new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialogInterface) {
//                        MyApplication.getInstance().showShortToastMessage("取消声纹注册");
//                        context.finish();
//                    }
//                });
//
//        // 初始化SpeakerVerifier，InitListener为初始化完成后的回调接口
//        mVerifier = SpeakerVerifier.createVerifier(context, new InitListener() {
//
//            @Override
//            public void onInit(int errorCode) {
//                if (ErrorCode.SUCCESS == errorCode) {
//                    MyApplication.getInstance().showShortToastMessage("引擎初始化成功");
//                    dialogInit.dismiss();
//                } else {
//                    MyApplication.getInstance().showShortToastMessage("引擎初始化失败，错误码：" + errorCode);
//                    dialogInit.dismiss();
//                }
//            }
//        });
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


//                // 获取密码之前先终止之前的注册或验证过程
//                mVerifier.cancel();
//
//
//                dialogInit  = ProgressDialog.show(this, "提示", "声纹密码注册中。。。", true,
//                        true, new DialogInterface.OnCancelListener() {
//                            @Override
//                            public void onCancel(DialogInterface dialogInterface) {
//                                MyApplication.getInstance().showShortToastMessage("取消声纹注册");
//                                context.finish();
//                            }
//                        });
//
//
//                // 清空参数
//                mVerifier.setParameter(SpeechConstant.PARAMS, null);
//                //设置数字密码
//                mVerifier.setParameter(SpeechConstant.ISV_PWDT, "" + PWD_TYPE_NUM);
//
//
//                mVerifier.getPasswordList(new SpeechListener() {
//                    @Override
//                    public void onEvent(int i, Bundle bundle) {
//
//                    }
//
//                    @Override
//                    public void onBufferReceived(byte[] bytes) {
//                        String result = new String(bytes);
//                        StringBuffer numberString = new StringBuffer();
//                        try {
//                            JSONObject object = new JSONObject(result);
//                            if (!object.has("num_pwd")) {
//
//                                return;
//                            }
//
//                            JSONArray pwdArray = object.optJSONArray("num_pwd");
//                            numberString.append(pwdArray.get(0));
//                            for (int i = 1; i < pwdArray.length(); i++) {
//                                numberString.append("-" + pwdArray.get(i));
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//
//                        String newFaceKey = MyApplication.getInstance().getLoginUserID() + RandomUtil.generateString(8);
//                        MyApplication.getInstance().setVoiceKey(newFaceKey);
//
//                        String regNumberPassword =
//
//                    }
//
//                    @Override
//                    public void onCompleted(SpeechError speechError) {
//                        if (null != speechError && ErrorCode.SUCCESS != speechError.getErrorCode()) {
//                            MyApplication.getInstance().showShortToastMessage("获取密码失败：" + speechError.getErrorCode());
//                            dialogInit.dismiss();
//                        }
//                    }
//                });

                String newVoiceKey = MyApplication.getInstance().getLoginUserID() + RandomUtil.generateString(8);
                PersonInfoEntity personInfoEntity = MyApplication.getInstance().getPersonInfo();
                personInfoEntity.setVoiceCheckID(newVoiceKey);
                MyApplication.getInstance().updatePersonInfo(personInfoEntity);
                gotoActivity(VoiceReg.class);
                break;
        }
    }


}
