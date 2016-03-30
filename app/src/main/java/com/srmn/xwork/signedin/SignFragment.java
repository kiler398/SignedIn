package com.srmn.xwork.signedin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.beardedhen.androidbootstrap.api.attributes.BootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.srmn.xwork.app.MyApplication;
import com.srmn.xwork.base.BaseFragment;
import com.srmn.xwork.gis.GISAMapLocationTask;
import com.srmn.xwork.gis.GISLocation;
import com.srmn.xwork.gis.GISLocationListener;
import com.srmn.xwork.gis.GISLocationOption;
import com.srmn.xwork.utils.DateTimeUtil;
import com.srmn.xwork.utils.NumberUtil;
import com.srmn.xwork.utils.StringUtil;
import com.srmn.xwork.utils.xfSDkUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@ContentView(R.layout.fragment_sign)
public class SignFragment extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    @ViewInject(R.id.txtTime)
    private TextView txtTime;
    @ViewInject(R.id.txtWeek)
    private TextView txtWeek;
    @ViewInject(R.id.txtDate)
    private TextView txtDate;


    @ViewInject(R.id.txtLocation)
    private TextView txtLocation;
    @ViewInject(R.id.txtInfo)
    private BootstrapLabel txtInfo;


    @ViewInject(R.id.txtFace)
    private BootstrapLabel txtFace;
    @ViewInject(R.id.txtVoice)
    private BootstrapLabel txtVoice;


    @ViewInject(R.id.btnSignIn)
    private BootstrapButton btnSignIn;


    @ViewInject(R.id.btnCheckLocation)
    private BootstrapButton btnCheckLocation;
    @ViewInject(R.id.btnCheckFace)
    private BootstrapButton btnCheckFace;
    @ViewInject(R.id.btnCheckVoice)
    private BootstrapButton btnCheckVoice;


    public SignFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignFragment newInstance(String param1, String param2) {
        SignFragment fragment = new SignFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =super.onCreateView(inflater, container, savedInstanceState);
        btnCheckFace.setOnClickListener(this);
        btnCheckVoice.setOnClickListener(this);
        btnCheckLocation.setOnClickListener(this);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        RefreshUI();
    }

    public void RefreshUI() {

        setDateTime(new Date());

        if (MyApplication.getInstance().getTodayLocationIsVerify()) {
            txtInfo.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
            txtInfo.setMarkdownText("位置签到：已完成 {fa_check_circle}");
            btnCheckFace.setVisibility(View.VISIBLE);
            btnCheckVoice.setVisibility(View.VISIBLE);
        } else {
            txtInfo.setBootstrapBrand(DefaultBootstrapBrand.DANGER);
            txtInfo.setMarkdownText("位置签到：未完成 {fa_exclamation_triangle}");
            btnCheckFace.setVisibility(View.GONE);
            btnCheckVoice.setVisibility(View.GONE);
        }

        if (MyApplication.getInstance().getTodayFaceIsVerify()) {
            txtFace.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
            txtFace.setMarkdownText("人脸签到：已完成 {fa_check_circle}");
        } else {
            txtFace.setBootstrapBrand(DefaultBootstrapBrand.DANGER);
            txtFace.setMarkdownText("人脸签到：未完成 {fa_exclamation_triangle}");
        }

        if (MyApplication.getInstance().getTodayVoiceIsVerify()) {
            txtVoice.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
            txtVoice.setMarkdownText("声纹签到：已完成 {fa_check_circle}");
        } else {
            txtVoice.setBootstrapBrand(DefaultBootstrapBrand.DANGER);
            txtVoice.setMarkdownText("声纹签到：未完成 {fa_exclamation_triangle}");
        }


        if (MyApplication.getInstance().getTodayIsVerify()) {
            btnSignIn.setVisibility(View.VISIBLE);
        } else {
            btnSignIn.setVisibility(View.GONE);
        }


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
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
            case R.id.btnCheckFace:
                ((Main) this.getActivity()).gotoActivityForResult(FaceCheck.class, Main.RequestCode_VerifyFace);
                break;
            case R.id.btnCheckVoice:
                ((Main) this.getActivity()).gotoActivityForResult(VoiceCheck.class, Main.RequestCode_VerifyVoice);
                break;
            case R.id.btnCheckLocation:
                GISLocationOption gisOption = MyApplication.getInstance().BuildSingleGISLocationOption();

                GISAMapLocationTask locationTask = new GISAMapLocationTask(gisOption, new GISLocationListener() {
                    @Override
                    public void onStarted() {

                    }

                    @Override
                    public void onLocationChanged(GISLocation location) {
                        setLocation(location);
                    }

                    @Override
                    public void onEnd() {

                    }

                    @Override
                    public void onFailed(String errorInfo) {

                    }

                    @Override
                    public void onProgress(String message, Integer progressIndex) {

                    }
                });

                locationTask.start();
                break;
        }


    }

    public void setDateTime(Date dateTime) {
        if (txtTime != null)
            txtTime.setText(DateTimeUtil.FormatMiniteTime(dateTime));
        if (txtDate != null)
            txtDate.setText(DateTimeUtil.FormatDate(dateTime));
        if (txtWeek != null)
            txtWeek.setText(DateTimeUtil.FormatWeek(dateTime));
    }

    public void setLocation(GISLocation location) {
        if (MyApplication.getInstance().getCheckLocation() != null && location.getLatitude() > 1 && location.getLongitude() > 1) {
            if (MyApplication.getInstance().getCheckLocation().getLatitude() > 1 && MyApplication.getInstance().getCheckLocation().getLongitude() > 1) {

                String info = "";

                if (!StringUtil.isNullOrEmpty(location.getAddress())) {
                    info = location.getAddress();
                } else {
                    info = location.getLongitude() + "," + location.getLatitude();
                }


                float distance = AMapUtils.calculateLineDistance(
                        new LatLng(MyApplication.getInstance().getCheckLocation().getLatitude(), MyApplication.getInstance().getCheckLocation().getLongitude()),
                        new LatLng(location.getLatitude(), location.getLongitude()));

                info = info + ",距离签到点" + MyApplication.getInstance().getCheckLocation().getAddress() + "(" + NumberUtil.roundNumber(distance, 0) + "米)";

                this.txtLocation.setText(info);

                if (distance <= MyApplication.getInstance().getCheckLocationRange()) {
                    txtInfo.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                    txtInfo.setMarkdownText("位置签到：已完成 {fa_check_circle}");
                    btnCheckFace.setVisibility(View.VISIBLE);
                    btnCheckVoice.setVisibility(View.VISIBLE);
                    MyApplication.getInstance().setLastLocationVerifyTime(new Date());
                } else {
                    txtInfo.setBootstrapBrand(DefaultBootstrapBrand.DANGER);
                    txtInfo.setMarkdownText("位置签到：未完成 {fa_exclamation_triangle}");
                    btnCheckFace.setVisibility(View.GONE);
                    btnCheckVoice.setVisibility(View.GONE);
                }


            }

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
