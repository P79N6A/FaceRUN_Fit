package com.fly.run.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fly.run.R;
import com.fly.run.activity.training.plan.FitPlanItemActivity;
import com.fly.run.adapter.FitPlanAdapter;
import com.fly.run.bean.FitPlanBean;

import java.io.Serializable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FitPlanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FitPlanFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int mPosition;
    private List<FitPlanBean> mPlanList;

    private ListView listView;
    private FitPlanAdapter adapter;

    public FitPlanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RunDataFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static FitPlanFragment newInstance(String param1, String param2) {
//        FitPlanFragment fragment = new FitPlanFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
    public static FitPlanFragment newInstance(int position, List<FitPlanBean> planList) {
        FitPlanFragment fragment = new FitPlanFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, position);
        args.putSerializable(ARG_PARAM2, (Serializable) planList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(ARG_PARAM1);
            mPlanList = (List<FitPlanBean>) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fit_plan, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View view) {
        listView = (ListView) view.findViewById(R.id.listview);
        adapter = new FitPlanAdapter(getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FitPlanItemActivity.startActivityJump(getActivity(), mPlanList.get(i));
            }
        });
        adapter.setData(mPlanList);
    }

}
