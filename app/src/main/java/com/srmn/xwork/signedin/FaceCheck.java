package com.srmn.xwork.signedin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.srmn.xwork.app.MyApplication;
import com.srmn.xwork.base.FaceActivity;
import com.srmn.xwork.utils.FaceUtil;
import com.srmn.xwork.utils.StringUtil;
import com.srmn.xwork.utils.xfSDkUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.File;

@ContentView(R.layout.activity_face_check)
public class FaceCheck  extends FaceActivity implements View.OnClickListener {

    @ViewInject(R.id.txtAuthid)
    private TextView txtAuthid;
    @ViewInject(R.id.btnPhoto)
    private BootstrapButton btnPhoto;
    @ViewInject(R.id.btnCheck)
    private BootstrapButton btnCheck;
    @ViewInject(R.id.txtMessage)
    private TextView txtMessage;


    private int requestCode = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.showActionBar();
        this.showBackButton();


        Intent i = getIntent();

        if (i.hasExtra("requestCode"))
            requestCode = i.getIntExtra("requestCode", 0);

        initSdk();

        btnPhoto.setOnClickListener(this);
        btnCheck.setOnClickListener(this);
        txtAuthid.setText(MyApplication.getInstance().getFaceKey());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void showTip(String strMessage) {


        txtMessage.setText(strMessage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        String fileSrc = null;
        if (requestCode == REQUEST_CAMERA_IMAGE) {
            if (null == mPictureFile) {
                showTip("拍照失败，请重试");
                return;
            }

            fileSrc = mPictureFile.getAbsolutePath();
            updateGallery(fileSrc);

            setFile(fileSrc,(ImageView) findViewById(R.id.imgFace));
            // 跳转到图片裁剪页面

        }
    }


    @Override
    public void onClick(View view) {
        int clickId = view.getId();
        if(clickId==R.id.btnPhoto)
        {
            // 设置相机拍照后照片保存路径
            mPictureFile = new File(Environment.getExternalStorageDirectory(),
                    "picture" + System.currentTimeMillis()/1000 + ".jpg");
            // 启动拍照,并保存到临时文件
            Intent mIntent = new Intent();
            mIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            mIntent.putExtra("camerasensortype", 2); // 调用前置摄像头
            mIntent.putExtra("autofocus", true); // 自动对焦
            mIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPictureFile));
            mIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            startActivityForResult(mIntent, REQUEST_CAMERA_IMAGE);
        }
        else if(clickId==R.id.btnCheck)
        {
            String mAuthid = txtAuthid.getText().toString();
            if (StringUtil.isNullOrEmpty(mAuthid)) {
                showTip("authid不能为空");
                return;
            }

            if (null != mImageData) {
                mProDialog.setMessage("验证中...");
                mProDialog.show();
                // 设置用户标识，格式为6-18个字符（由字母、数字、下划线组成，不得以数字开头，不能包含空格）。
                // 当不设置时，云端将使用用户设备的设备ID来标识终端用户。
                mFaceRequest.setParameter(SpeechConstant.AUTH_ID, mAuthid);
                mFaceRequest.setParameter(SpeechConstant.WFR_SST, "verify");
                mFaceRequest.sendRequest(mImageData, mRequestListener);
            } else {
                showTip("请选择图片后再验证");
            }
        }

    }

    @Override
    public void regResult(int resukt) {
        if (resukt==RegResultOk) {
            showTip("注册成功");
        } else  if (resukt==RegResultFiled) {
            showTip("注册失败");
        }
    }


    @Override
    public void verifyResult(int resukt) {
        if (resukt==VerifyResultFiled) {
            MyApplication.getInstance().showLongToastMessage("验证失败");
        } else  if (resukt==VerifyResultNoOk) {
            MyApplication.getInstance().showLongToastMessage("未能通过验证");
        } else  if (resukt==VerifyResultOk) {
            MyApplication.getInstance().showLongToastMessage("通过验证");

            if (requestCode == Main.RequestCode_VerifyFace) {
                setResult(RESULT_OK);
                finish();
            } else {
                finish();
            }

        }
    }
}
