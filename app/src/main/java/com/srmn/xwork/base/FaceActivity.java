package com.srmn.xwork.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechError;
import com.srmn.xwork.signedin.R;
import com.srmn.xwork.utils.FaceListener;
import com.srmn.xwork.utils.FaceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

/**
 * Created by kiler on 2016/1/10.
 */
public abstract class FaceActivity extends BaseActivity implements FaceListener {


    protected final int REQUEST_PICTURE_CHOOSE = 1;
    protected final int REQUEST_CAMERA_IMAGE = 2;
    protected Bitmap mImage = null;
    protected byte[] mImageData = null;

    // 进度对话框
    protected ProgressDialog mProDialog;
    // 拍照得到的照片文件
    protected File mPictureFile;
    // FaceRequest对象，集成了人脸识别的各种功能
    protected FaceRequest mFaceRequest;

    protected void initSdk()
    {
        mProDialog = new ProgressDialog(this);
        mProDialog.setCancelable(true);
        mProDialog.setTitle("请稍后");

        mProDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // cancel进度框时,取消正在进行的操作
                if (null != mFaceRequest) {
                    mFaceRequest.cancel();
                }
            }
        });

        mFaceRequest = new FaceRequest(this);
    }
    protected abstract void showTip(final String strMessage);

    protected void updateGallery(String filename) {
        MediaScannerConnection.scanFile(this, new String[] {filename}, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    @Override
                    public void onScanCompleted(String path, Uri uri) {

                    }
                });
    }


    public void setFile(String fileSrc, ImageView imgview) {
        // 获取图片的宽和高
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        mImage = BitmapFactory.decodeFile(fileSrc, options);

        // 压缩图片
        options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max(
                (double) options.outWidth / 1024f,
                (double) options.outHeight / 1024f)));
        options.inJustDecodeBounds = false;
        mImage = BitmapFactory.decodeFile(fileSrc, options);


        // 若mImageBitmap为空则图片信息不能正常获取
        if (null == mImage) {
            showTip("图片信息无法正常获取！");
            return;
        }

        // 部分手机会对图片做旋转，这里检测旋转角度
        int degree = FaceUtil.readPictureDegree(fileSrc);
        if (degree != 0) {
            // 把图片旋转为正的方向
            mImage = FaceUtil.rotateImage(degree, mImage);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //可根据流量及网络状况对图片进行压缩
        mImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        mImageData = baos.toByteArray();

        imgview.setImageBitmap(mImage);

    }

    protected RequestListener mRequestListener = new RequestListener() {

        @Override
        public void onEvent(int eventType, Bundle params) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            if (null != mProDialog) {
                mProDialog.dismiss();
            }

            try {
                String result = new String(buffer, "utf-8");
                Log.d("FaceDemo", result);

                JSONObject object = new JSONObject(result);
                String type = object.optString("sst");
                if ("reg".equals(type)) {
                    register(object);
                } else if ("verify".equals(type)) {
                    verify(object);
                } else if ("detect".equals(type)) {
                    detect(object);
                } else if ("align".equals(type)) {
                    align(object);
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO: handle exception
            }
        }



        @Override
        public void onCompleted(SpeechError error) {
            if (null != mProDialog) {
                mProDialog.dismiss();
            }

            if (error != null) {
                switch (error.getErrorCode()) {
                    case ErrorCode.MSP_ERROR_ALREADY_EXIST:
                        showTip("authid已经被注册，请更换后再试");
                        break;

                    default:
                        showTip(error.getPlainDescription(true));
                        break;
                }
            }
        }
    };

    public static final int RegResultOk = 1;
    public static final int RegResultFiled = 2;


    public static final int VerifyResultOk = 11;
    public static final int VerifyResultNoOk = 12;
    public static final int VerifyResultFiled = 13;




    private void register(JSONObject obj) throws JSONException {
        int ret = obj.getInt("ret");
        if (ret != 0) {

            regResult(RegResultFiled);

            return;
        }
        if ("success".equals(obj.get("rst"))) {

            regResult(RegResultOk);
        } else {

            regResult(RegResultFiled);

        }
    }

    private void verify(JSONObject obj) throws JSONException {
        int ret = obj.getInt("ret");
        if (ret != 0) {
            verifyResult(VerifyResultFiled);
//            showTip("验证失败");
            return;
        }
        if ("success".equals(obj.get("rst"))) {
            if (obj.getBoolean("verf")) {
                verifyResult(VerifyResultOk);
                //showTip("通过验证，欢迎回来！");
            } else {
                verifyResult(VerifyResultNoOk);
                //showTip("验证不通过");
            }
        } else {
            verifyResult(VerifyResultFiled);
            //showTip("验证失败");
        }
    }

    private void detect(JSONObject obj) throws JSONException {
        int ret = obj.getInt("ret");
        if (ret != 0) {
            showTip("检测失败");
            return;
        }

        if ("success".equals(obj.get("rst"))) {
            JSONArray faceArray = obj.getJSONArray("face");

            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStrokeWidth(Math.max(mImage.getWidth(), mImage.getHeight()) / 100f);

            Bitmap bitmap = Bitmap.createBitmap(mImage.getWidth(),
                    mImage.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(mImage, new Matrix(), null);
            for (int i = 0; i < faceArray.length(); i++) {
                float x1 = (float) faceArray.getJSONObject(i)
                        .getJSONObject("position").getDouble("left");
                float y1 = (float) faceArray.getJSONObject(i)
                        .getJSONObject("position").getDouble("top");
                float x2 = (float) faceArray.getJSONObject(i)
                        .getJSONObject("position").getDouble("right");
                float y2 = (float) faceArray.getJSONObject(i)
                        .getJSONObject("position").getDouble("bottom");
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawRect(new Rect((int)x1, (int)y1, (int)x2, (int)y2),
                        paint);
            }

            mImage = bitmap;
            ((ImageView) findViewById(R.id.imgFace)).setImageBitmap(mImage);
        } else {
            showTip("检测失败");
        }
    }

    @SuppressWarnings("rawtypes")
    private void align(JSONObject obj) throws JSONException {
        int ret = obj.getInt("ret");
        if (ret != 0) {
            showTip("聚焦失败");
            return;
        }
        if ("success".equals(obj.get("rst"))) {
            Paint paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(Math.max(mImage.getWidth(), mImage.getHeight()) / 100f);

            Bitmap bitmap = Bitmap.createBitmap(mImage.getWidth(),
                    mImage.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(mImage, new Matrix(), null);

            JSONArray faceArray = obj.getJSONArray("result");
            for (int i = 0; i < faceArray.length(); i++) {
                JSONObject landmark = faceArray.getJSONObject(i).getJSONObject(
                        "landmark");
                Iterator it = landmark.keys();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    JSONObject postion = landmark.getJSONObject(key);
                    canvas.drawPoint((float) postion.getDouble("x"),
                            (float) postion.getDouble("y"), paint);
                }
            }

            mImage = bitmap;
            ((ImageView) findViewById(R.id.imgFace)).setImageBitmap(mImage);
        } else {
            showTip("聚焦失败");
        }
    }


    @Override
    public void finish() {
        if (null != mProDialog) {
            mProDialog.dismiss();
        }
        super.finish();
    }

}
