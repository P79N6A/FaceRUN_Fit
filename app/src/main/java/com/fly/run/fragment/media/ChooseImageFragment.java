package com.fly.run.fragment.media;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.fly.run.R;
import com.fly.run.adapter.ChooseImagesAdapter;
import com.fly.run.bean.FileItem;
import com.fly.run.utils.ToastUtil;
import com.fly.run.view.actionbar.CommonActionBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ChooseImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseImageFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private CommonActionBar actionBar;

    public ChooseImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CircleAttentionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChooseImageFragment newInstance(String param1, String param2) {
        ChooseImageFragment fragment = new ChooseImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static ChooseImageFragment newInstance() {
        ChooseImageFragment fragment = new ChooseImageFragment();
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
        View view = inflater.inflate(R.layout.fragment_choose_image, container, false);
        return view;
    }
}
