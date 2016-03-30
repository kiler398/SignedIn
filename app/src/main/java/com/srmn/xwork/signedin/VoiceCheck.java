package com.srmn.xwork.signedin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeakerVerifier;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.VerifierListener;
import com.iflytek.cloud.VerifierResult;
import com.srmn.xwork.app.MyApplication;
import com.srmn.xwork.base.BaseActivity;
import com.srmn.xwork.base.VoiceActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;


@ContentView(R.layout.activity_voice_check)
public class VoiceCheck extends BaseActivity implements View.OnClickListener {


    protected static final String TAG = "VoiceCheck";
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

    private ProgressDialog progressDialog;

    private int requestCode = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackButton();

        Intent i = getIntent();

        if (i.hasExtra("requestCode"))
            requestCode = i.getIntExtra("requestCode", 0);

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

                // 清空提示信息
                showMsg.setText("");
                // 清空参数
                mVerifier.setParameter(SpeechConstant.PARAMS, null);
                mVerifier.setParameter(SpeechConstant.ISV_AUDIO_PATH,
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/verify.pcm");
                mVerifier = SpeakerVerifier.getVerifier();
                // 设置业务类型为验证
                mVerifier.setParameter(SpeechConstant.ISV_SST, "verify");
                // 对于某些麦克风非常灵敏的机器，如nexus、samsung i9300等，建议加上以下设置对录音进行消噪处理
                // mVerify.setParameter(SpeechConstant.AUDIO_SOURCE, "" + MediaRecorder.AudioSource.VOICE_RECOGNITION);


                // 数字密码注册需要传入密码
                String verifyPwd = mVerifier.generatePassword(8);
                mVerifier.setParameter(SpeechConstant.ISV_PWD, verifyPwd);
                showPwd.setText("请读出：" + verifyPwd);

                // 设置auth_id，不能设置为空
                mVerifier.setParameter(SpeechConstant.AUTH_ID, MyApplication.getInstance().getVoiceKey());
                mVerifier.setParameter(SpeechConstant.ISV_PWDT, "" + PWD_TYPE_NUM);
                // 开始验证
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

                        if (verifierResult.ret == 0) {

                            MyApplication.getInstance().showLongToastMessage("通过验证");
                            if (requestCode == Main.RequestCode_VerifyVoice) {
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                finish();
                            }


                        } else {
                            // 验证不通过
                            switch (verifierResult.err) {
                                case VerifierResult.MSS_ERROR_IVP_GENERAL:
                                    showMsg.setText("内核异常");
                                    break;
                                case VerifierResult.MSS_ERROR_IVP_TRUNCATED:
                                    showMsg.setText("出现截幅");
                                    break;
                                case VerifierResult.MSS_ERROR_IVP_MUCH_NOISE:
                                    showMsg.setText("太多噪音");
                                    break;
                                case VerifierResult.MSS_ERROR_IVP_UTTER_TOO_SHORT:
                                    showMsg.setText("录音太短");
                                    break;
                                case VerifierResult.MSS_ERROR_IVP_TEXT_NOT_MATCH:
                                    showMsg.setText("验证不通过，您所读的文本不一致");
                                    break;
                                case VerifierResult.MSS_ERROR_IVP_TOO_LOW:
                                    showMsg.setText("音量太低");
                                    break;
                                case VerifierResult.MSS_ERROR_IVP_NO_ENOUGH_AUDIO:
                                    showMsg.setText("音频长达不到自由说的要求");
                                    break;
                                default:
                                    showMsg.setText("验证不通过");
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onError(SpeechError speechError) {


                        switch (speechError.getErrorCode()) {
                            case ErrorCode.MSP_ERROR_NOT_FOUND:
                                showMsg.setText("模型不存在，请先注册");
                                break;

                            default:
                                showMsg.setText("发生错误，错误信息：" + speechError.getPlainDescription(true));
                                ;
                                break;
                        }
                    }

                    @Override
                    public void onEvent(int i, int i1, int i2, Bundle bundle) {
                        // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
                        //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                        //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
                        //		Log.d(TAG, "session id =" + sid);
                        //	}
                    }
                });

                break;
        }

    }


    @Override
    protected void onDestroy() {
        if (null != mVerifier) {
            mVerifier.stopListening();
            mVerifier.destroy();
        }
        super.onDestroy();
    }
}
