package com.srmn.xwork.signedin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.srmn.xwork.app.MyApplication;
import com.srmn.xwork.base.BaseFragment;
import com.srmn.xwork.utils.DateTimeUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@ContentView(R.layout.fragment_record)
public class RecordFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    @ViewInject(R.id.calendarMain)
    private LinearLayout calendarMain;
    @ViewInject(R.id.txtDayInfo)
    private BootstrapLabel txtDayInfo;
    @ViewInject(R.id.txtMonthInfo)
    private BootstrapLabel txtMonthInfo;
    private CaldroidFragment caldroidFragment;

    public RecordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecordFragment newInstance(String param1, String param2) {
        RecordFragment fragment = new RecordFragment();
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

    private void setNoSignDate(Date date) {
        if (caldroidFragment != null) {
            caldroidFragment.setBackgroundResourceForDate(R.color.c_red, date);
            caldroidFragment.setTextColorForDate(R.color.c_white, date);
        }
    }

    private void setSignedDate(Date date) {
        if (caldroidFragment != null) {
            caldroidFragment.setBackgroundResourceForDate(R.color.c_green, date);
            caldroidFragment.setTextColorForDate(R.color.c_white, date);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
        args.putString(CaldroidFragment.MIN_DATE, "2015-12-01");
        args.putString(CaldroidFragment.MAX_DATE, DateTimeUtil.FormatDate(new Date()));
        caldroidFragment.setArguments(args);


        setNoSignDate(DateTimeUtil.BuildDate(2016, 1, 11));
        setNoSignDate(DateTimeUtil.BuildDate(2016, 1, 12));
        setNoSignDate(DateTimeUtil.BuildDate(2016, 1, 13));


        setSignedDate(DateTimeUtil.BuildDate(2016, 1, 14));
        setSignedDate(DateTimeUtil.BuildDate(2016, 1, 15));
        setSignedDate(DateTimeUtil.BuildDate(2016, 1, 16));

        caldroidFragment.refreshView();

        FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendarMain, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                //MyApplication.getInstance().showLongToastMessage("onSelectDate"+DateTimeUtil.FormatDate(date));
                caldroidFragment.clearSelectedDates();
                caldroidFragment.setSelectedDate(date);
                caldroidFragment.refreshView();
                txtDayInfo.setMarkdownText(DateTimeUtil.FormatDate(date) + "：已经签到 {fa_check_circle}");

            }

            @Override
            public void onChangeMonth(int month, int year) {
                txtMonthInfo.setMarkdownText(DateTimeUtil.FormatMonth(DateTimeUtil.BuildDate(year, month - 1, 1)) + "：应签/实签:(10/10) {fa_check_circle}");
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                MyApplication.getInstance().showLongToastMessage("onLongClickDate" + DateTimeUtil.FormatDate(date));
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {

                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);


//        cvMain.setMaxDate(new Date().getTime());
//
//        cvMain.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
//
//                Calendar cDay = Calendar.getInstance();
//                cDay.set(year, month, day, 0, 0, 0);
//
//                Date selectDay = cDay.getTime();
//
//                txtDayInfo.setMarkdownText(DateTimeUtil.FormatDate(selectDay) + "：已经签到 {fa_check_circle}");
//                txtMonthInfo.setMarkdownText(DateTimeUtil.FormatMonth(selectDay) + "：应签/实签:(10/10) {fa_check_circle}");
//            }
//        });
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
