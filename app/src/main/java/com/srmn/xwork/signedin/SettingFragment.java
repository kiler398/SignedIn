package com.srmn.xwork.signedin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.srmn.xwork.app.MyApplication;
import com.srmn.xwork.base.BaseFragment;
import com.srmn.xwork.utils.xfSDkUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@ContentView(R.layout.fragment_setting)
public class SettingFragment extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    @ViewInject(R.id.txtVersion)
    private TextView txtVersion;
    @ViewInject(R.id.txtDeviceID)
    private TextView txtDeviceID;
    @ViewInject(R.id.rlFaceReg)
    private RelativeLayout rlFaceReg;
    @ViewInject(R.id.rlVoiceReg)
    private RelativeLayout rlVoiceReg;
    @ViewInject(R.id.rlUserInfo)
    private RelativeLayout rlUserInfo;
    @ViewInject(R.id.rlLocationReg)
    private RelativeLayout rlLocationReg;
    @ViewInject(R.id.txtEnableVoiceCheck)
    private TextView txtEnableVoiceCheck;
    @ViewInject(R.id.ibtnVoiceCheckSetting)
    private TextView ibtnVoiceCheckSetting;

    @ViewInject(R.id.txtEnableFaceCheck)
    private TextView txtEnableFaceCheck;
    @ViewInject(R.id.ibtnFaceCheckSetting)
    private TextView ibtnFaceCheckSetting;







    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }


    @Override
    public void onStart() {
        if (MyApplication.getInstance().getFaceCheckEnable()) {
            //txtEnableFaceCheck.setTextColor(getResources().getColor(R.color.c_black));;
            txtEnableFaceCheck.setText("已启用");

        } else {
            txtEnableFaceCheck.setText("未启用");
        }
        if (MyApplication.getInstance().getVoiceCheckEnable()) {
            txtEnableVoiceCheck.setText("已启用");

            //ibtnVoiceCheckSetting;

        } else {
            txtEnableVoiceCheck.setText("未启用");
        }
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        txtVersion.setText(MyApplication.getInstance().getVersionName());
        txtDeviceID.setText(xfSDkUtil.getUNDeviceID());
        rlUserInfo.setOnClickListener(this);
        rlFaceReg.setOnClickListener(this);
        rlVoiceReg.setOnClickListener(this);
        rlLocationReg.setOnClickListener(this);


        return v;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {

        //MyApplication.getInstance().showLongToastMessage(xfSDkUtil.getDeviceID());

        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rlFaceReg:
                if (MyApplication.getInstance().getFaceCheckEnable()) {
                    ((Main) this.getActivity()).gotoActivity(FaceLockSetting.class);
                } else {
                    ((Main) this.getActivity()).gotoActivity(FaceVerify.class);
                }
                break;
            case R.id.rlVoiceReg:
                if (MyApplication.getInstance().getVoiceCheckEnable()) {
                    ((Main) this.getActivity()).gotoActivity(VoiceLockSetting.class);
                } else {
                    ((Main) this.getActivity()).gotoActivity(VoiceVerify.class);
                }
                break;
            case R.id.rlUserInfo:
                ((Main) this.getActivity()).gotoActivity(MySetting.class);
                break;
            case R.id.rlLocationReg:
                ((Main) this.getActivity()).gotoActivity(LocationLockSetting.class);
                break;
        }


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
