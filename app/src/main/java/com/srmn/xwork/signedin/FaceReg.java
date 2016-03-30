package com.srmn.xwork.signedin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.Setting;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.srmn.xwork.app.MyApplication;
import com.srmn.xwork.base.BaseActivity;
import com.srmn.xwork.base.FaceActivity;
import com.srmn.xwork.utils.FaceUtil;
import com.srmn.xwork.utils.StringUtil;
import com.srmn.xwork.utils.xfSDkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;


@ContentView(R.layout.activity_face_reg)
public class FaceReg extends FaceActivity implements View.OnClickListener {


    @ViewInject(R.id.txtAuthid)
    private TextView txtAuthid;
    @ViewInject(R.id.btnPhoto)
    private BootstrapButton btnPhoto;
    @ViewInject(R.id.btnReg)
    private BootstrapButton btnReg;
    @ViewInject(R.id.txtMessage)
    private TextView txtMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.showActionBar();
        this.showBackButton();

        initSdk();
        btnPhoto.setOnClickListener(this);
        btnReg.setOnClickListener(this);
        txtAuthid.setText(MyApplication.getInstance().getFaceKey());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        finish();
        return super.onOptionsItemSelected(item);
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
        else if(clickId==R.id.btnReg)
        {
            String mAuthid = txtAuthid.getText().toString();
            if (StringUtil.isNullOrEmpty(mAuthid)) {
                showTip("authid不能为空");
                return;
            }

            if (null != mImageData) {
                mProDialog.setMessage("注册中...");
                mProDialog.show();
                // 设置用户标识，格式为6-18个字符（由字母、数字、下划线组成，不得以数字开头，不能包含空格）。
                // 当不设置时，云端将使用用户设备的设备ID来标识终端用户。
                mFaceRequest.setParameter(SpeechConstant.AUTH_ID, mAuthid);
                mFaceRequest.setParameter(SpeechConstant.WFR_SST, "reg");
                mFaceRequest.sendRequest(mImageData, mRequestListener);
            } else {
                showTip("请选择图片后再注册");
            }
        }
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

            //setFile(fileSrc);
            // 跳转到图片裁剪页面
            FaceUtil.cropPicture(this,Uri.fromFile(new File(fileSrc)));
        } else if (requestCode == FaceUtil.REQUEST_CROP_IMAGE) {
            // 获取返回数据
            Bitmap bmp = data.getParcelableExtra("data");
            // 若返回数据不为null，保存至本地，防止裁剪时未能正常保存
            if (null != bmp) {
                FaceUtil.saveBitmapToFile(context, bmp);
            }
            // 获取图片保存路径
            fileSrc = FaceUtil.getImagePath(context);

            setFile(fileSrc,(ImageView) findViewById(R.id.imgFace));

        }
    }


    @Override
    protected void showTip(String strMessage) {
        txtMessage.setText(strMessage);
    }


    @Override
    public void regResult(int resukt) {
        if (resukt==RegResultOk) {
            MyApplication.getInstance().showLongToastMessage("注册成功");
            MyApplication.getInstance().setFaceCheckEnable(true);
            finish();
            gotoActivity(FaceLockSetting.class, Intent.FLAG_ACTIVITY_NEW_TASK);
        } else  if (resukt==RegResultFiled) {
            showTip("注册失败");
        }
    }


    @Override
    public void verifyResult(int resukt) {
        if (resukt==VerifyResultFiled) {
        showTip("验证失败");
        } else  if (resukt==VerifyResultNoOk) {
        showTip("验证不通过");
        } else  if (resukt==VerifyResultOk) {
        showTip("通过验证，欢迎回来！");
        }
    }
}
