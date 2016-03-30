package com.srmn.xwork.signedin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.srmn.xwork.androidlib.gis.GISLocation;
import com.srmn.xwork.androidlib.utils.DateTimeUtil;
import com.srmn.xwork.androidlib.utils.NumberUtil;
import com.srmn.xwork.androidlib.utils.StringUtil;
import com.srmn.xwork.app.MyApplication;
import com.srmn.xwork.base.BaseFragment;


import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@ContentView(R.layout.fragment_home)
public class HomeFragment extends BaseFragment implements View.OnClickListener  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @ViewInject(R.id.btnSignIn)
    private BootstrapButton btnSignIn;


    @ViewInject(R.id.txtTime)
    private TextView txtTime;
    @ViewInject(R.id.txtWeek)
    private TextView txtWeek;
    @ViewInject(R.id.txtDate)
    private TextView txtDate;

    @ViewInject(R.id.txtLocation)
    private TextView txtLocation;
    @ViewInject(R.id.txtInfo)
    private TextView txtInfo;


    private OnFragmentInteractionListener mListener;


    public HomeFragment() {


        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View v = super.onCreateView(inflater, container, savedInstanceState);
        btnSignIn.setOnClickListener(this);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        setDateTime(new Date());
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

        int clickId = view.getId();
        if(clickId==R.id.btnSignIn) {
            ((Main) this.getActivity()).gotoFrame(1);
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
        if (!StringUtil.isNullOrEmpty(location.getAddress()))
        {
            txtLocation.setText(location.getAddress());
        } else {
            txtLocation.setText(location.getLongitude() + "," + location.getLatitude());
        }

        if (MyApplication.getInstance().getCheckLocation() != null && location.getLatitude() > 1 && location.getLongitude() > 1) {
            if (MyApplication.getInstance().getCheckLocation().getLatitude() > 1 && MyApplication.getInstance().getCheckLocation().getLongitude() > 1) {
                float distance = AMapUtils.calculateLineDistance(
                        new LatLng(MyApplication.getInstance().getCheckLocation().getLatitude(), MyApplication.getInstance().getCheckLocation().getLongitude()),
                        new LatLng(location.getLatitude(), location.getLongitude()));

                String info = "距离签到点" + MyApplication.getInstance().getCheckLocation().getAddress() + "(" + NumberUtil.roundNumber(distance, 0) + "米)";

                if (distance <= MyApplication.getInstance().getCheckLocationRange()) {
                    info = info + ",可以签到";
                    btnSignIn.setEnabled(true);
                } else {
                    info = info + ",不能签到";
                    btnSignIn.setEnabled(false);
                }


                this.txtInfo.setText(info);
            }

        }



    }

    public void setNetworkState(int networkState) {

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
