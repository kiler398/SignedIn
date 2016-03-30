package com.srmn.xwork.signedin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Printer;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeakerVerifier;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechListener;
import com.iflytek.cloud.VerifierListener;
import com.iflytek.cloud.VerifierResult;
import com.srmn.xwork.app.MyApplication;
import com.srmn.xwork.base.BaseActivity;
import com.srmn.xwork.base.VoiceActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;


@ContentView(R.layout.activity_voice_reg)
public class VoiceReg extends BaseActivity implements View.OnClickListener {

    protected static final String TAG = "VoiceReg";
    private static final int PWD_TYPE_NUM = 3;
    @ViewInject(R.id.showPwd)
    private TextView showPwd;
    @ViewInject(R.id.showMsg)
    private TextView showMsg;
    @ViewInject(R.id.showRegFbk)
    private TextView showRegFbk;
    @ViewInject(R.id.showVoice)
    private TextView showVoice;
    @ViewInject(R.id.txtAuthid)
    private TextView txtAuthid;
    @ViewInject(R.id.txtVlolume)
    private TextView txtVlolume;
    @ViewInject(R.id.pgVlolume)
    private ProgressBar pgVlolume;
    @ViewInject(R.id.btnReg)
    private BootstrapButton btnReg;
    // 声纹识别对象
    private SpeakerVerifier mVerifier;
    // 数字声纹密码
    private String mNumPwd = "";
    // 数字声纹密码段，默认有5段
    private String[] mNumPwdSegs;


    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackButton();


        btnReg.setOnClickListener(this);
        txtAuthid.setText(MyApplication.getInstance().getFaceKey());


        // 初始化SpeakerVerifier，InitListener为初始化完成后的回调接口
        mVerifier = SpeakerVerifier.createVerifier(context, new InitListener() {
            @Override
            public void onInit(int errorCode) {
                if (ErrorCode.SUCCESS == errorCode) {
                    MyApplication.getInstance().showShortToastMessage("引擎初始化成功");
                } else {
                    MyApplication.getInstance().showShortToastMessage("引擎初始化失败，错误码：" + errorCode);
                }
            }


        });

        progressDialog = ProgressDialog.show(context, "请稍等", "声纹引擎初始化中...", true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 清空参数
        mVerifier.setParameter(SpeechConstant.PARAMS, null);
        mVerifier.setParameter(SpeechConstant.ISV_PWDT, "" + PWD_TYPE_NUM);
        mVerifier.getPasswordList(new SpeechListener() {
                                      @Override
                                      public void onEvent(int i, Bundle bundle) {

                                      }

                                      @Override
                                      public void onBufferReceived(byte[] bytes) {
                                          String result = new String(bytes);
                                          StringBuffer numberString = new StringBuffer();
                                          try {
                                              JSONObject object = new JSONObject(result);
                                              if (!object.has("num_pwd")) {
                                                  //initTextView();
                                                  return;
                                              }

                                              JSONArray pwdArray = object.optJSONArray("num_pwd");
                                              numberString.append(pwdArray.get(0));
                                              for (int i = 1; i < pwdArray.length(); i++) {
                                                  numberString.append("-" + pwdArray.get(i));
                                              }
                                          } catch (JSONException e) {
                                              e.printStackTrace();
                                          }


                                          mNumPwd = numberString.toString();
                                          mNumPwdSegs = mNumPwd.split("-");
                                          //MyApplication.getInstance().showShortToastMessage("您的密码：\n" + mNumPwd);
                                      }

                                      @Override
                                      public void onCompleted(SpeechError speechError) {
                                          if (null != speechError && ErrorCode.SUCCESS != speechError.getErrorCode()) {
                                              MyApplication.getInstance().showShortToastMessage("获取密码失败：" + speechError.getErrorCode());
                                          }

                                          if (progressDialog != null)
                                              progressDialog.dismiss();
                                      }
                                  }

        );


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (null != mVerifier) {
            mVerifier.stopListening();
            mVerifier.destroy();
        }
        super.onDestroy();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnReg:
                // 清空参数
                mVerifier.setParameter(SpeechConstant.PARAMS, null);
                mVerifier.setParameter(SpeechConstant.ISV_AUDIO_PATH, Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/test.pcm");
                // 对于某些麦克风非常灵敏的机器，如nexus、samsung i9300等，建议加上以下设置对录音进行消噪处理
                //	mVerify.setParameter(SpeechConstant.AUDIO_SOURCE, "" + MediaRecorder.AudioSource.VOICE_RECOGNITION);
                // 数字密码注册需要传入密码
                if (TextUtils.isEmpty(mNumPwd)) {
                    MyApplication.getInstance().showShortToastMessage("请获取密码后进行操作");
                    return;
                }

                mVerifier.setParameter(SpeechConstant.ISV_PWD, mNumPwd);
                ((TextView) findViewById(R.id.showPwd)).setText("请读出："
                        + mNumPwd.substring(0, 8));
                showMsg.setText("训练 第" + 1 + "遍，剩余4遍");

                // 设置auth_id，不能设置为空
                mVerifier.setParameter(SpeechConstant.AUTH_ID, MyApplication.getInstance().getVoiceKey());
                // 设置业务类型为注册
                mVerifier.setParameter(SpeechConstant.ISV_SST, "train");
                // 设置声纹密码类型
                mVerifier.setParameter(SpeechConstant.ISV_PWDT, "" + PWD_TYPE_NUM);
                // 开始注册
                mVerifier.startListening(new VerifierListener() {
                    @Override
                    public void onVolumeChanged(int volume, byte[] data) {
                        showVoice.setText("当前正在说话:");
                        pgVlolume.setProgress(volume);
                        Log.d(TAG, "返回音频数据：" + data.length);
                    }

                    @Override
                    public void onBeginOfSpeech() {
                        showVoice.setText("开始说话");
                        txtVlolume.setVisibility(View.VISIBLE);
                        pgVlolume.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onEndOfSpeech() {
                        showVoice.setText("结束说话");
                        txtVlolume.setVisibility(View.GONE);
                        pgVlolume.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResult(VerifierResult verifierResult) {
                        showMsg.setText(verifierResult.source);

                        if (verifierResult.ret == ErrorCode.SUCCESS) {
                            switch (verifierResult.err) {
                                case VerifierResult.MSS_ERROR_IVP_GENERAL:
                                    showMsg.setText("内核异常");
                                    break;
                                case VerifierResult.MSS_ERROR_IVP_EXTRA_RGN_SOPPORT:
                                    showRegFbk.setText("训练达到最大次数");
                                    break;
                                case VerifierResult.MSS_ERROR_IVP_TRUNCATED:
                                    showRegFbk.setText("出现截幅");
                                    break;
                                case VerifierResult.MSS_ERROR_IVP_MUCH_NOISE:
                                    showRegFbk.setText("太多噪音");
                                    break;
                                case VerifierResult.MSS_ERROR_IVP_UTTER_TOO_SHORT:
                                    showRegFbk.setText("录音太短");
                                    break;
                                case VerifierResult.MSS_ERROR_IVP_TEXT_NOT_MATCH:
                                    showRegFbk.setText("训练失败，您所读的文本不一致");
                                    break;
                                case VerifierResult.MSS_ERROR_IVP_TOO_LOW:
                                    showRegFbk.setText("音量太低");
                                    break;
                                case VerifierResult.MSS_ERROR_IVP_NO_ENOUGH_AUDIO:
                                    showMsg.setText("音频长达不到自由说的要求");
                                default:
                                    showRegFbk.setText("");
                                    break;
                            }

                            if (verifierResult.suc == verifierResult.rgn) {

                                MyApplication.getInstance().showLongToastMessage("注册成功");
                                MyApplication.getInstance().setVoiceCheckEnable(true);
                                finish();
                                gotoActivity(VoiceLockSetting.class, Intent.FLAG_ACTIVITY_NEW_TASK);

                            } else {
                                int nowTimes = verifierResult.suc + 1;
                                int leftTimes = verifierResult.rgn - nowTimes;

                                showPwd.setText("请读出：" + mNumPwdSegs[nowTimes - 1]);

                                showMsg.setText("训练 第" + nowTimes + "遍，剩余" + leftTimes + "遍");
                            }

                        } else {

                            showMsg.setText("注册失败，请重新开始。");
                        }
                    }

                    @Override
                    public void onError(SpeechError speechError) {
                        if (speechError.getErrorCode() == ErrorCode.MSP_ERROR_ALREADY_EXIST) {
                            showMsg.setText("模型已存在，如需重新注册，请先删除");
                        } else {
                            showMsg.setText("发生错误，错误信息：" + speechError.getPlainDescription(true));
                        }
                    }

                    @Override
                    public void onEvent(int i, int i1, int i2, Bundle bundle) {

                    }
                });
                break;

        }
    }
}
