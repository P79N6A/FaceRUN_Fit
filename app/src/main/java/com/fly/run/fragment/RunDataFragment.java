package com.fly.run.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fly.run.R;
import com.fly.run.view.RunTrainView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link RunDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RunDataFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView ivBg;
    private RunTrainView runTrainView;

    public RunDataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RunDataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RunDataFragment newInstance(String param1, String param2) {
        RunDataFragment fragment = new RunDataFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_run_data, container, false);
        ivBg = (ImageView) rootView.findViewById(R.id.iv_bg);
//        runTrainView = (RunTrainView) rootView.findViewById(R.id.view_run_train);
        return rootView;
    }

    public ImageView getIvBg() {
        return ivBg;
    }

//    public void setTrainNowData(String distance, String time, String speed, String kcal) {
//        if (!TextUtils.isEmpty(distance))
//            runTrainView.setTvDistanceText(distance);
//        if (!TextUtils.isEmpty(time))
//            runTrainView.setTvTimeText(time);
//        if (!TextUtils.isEmpty(speed))
//            runTrainView.setTvSpeedText(speed);
//        if (!TextUtils.isEmpty(kcal))
//            runTrainView.setTvHeatText(kcal);
//    }
}
