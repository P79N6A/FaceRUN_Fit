package com.fly.run.fragment.circle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fly.run.R;
import com.fly.run.view.actionbar.CommonActionBar;
import com.fly.run.view.dialog.DialogChooseMedia;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link CircleAttentionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CircleAttentionFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private CommonActionBar actionBar;

    public CircleAttentionFragment() {
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
    public static CircleAttentionFragment newInstance(String param1, String param2) {
        CircleAttentionFragment fragment = new CircleAttentionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static CircleAttentionFragment newInstance() {
        CircleAttentionFragment fragment = new CircleAttentionFragment();
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
        View view = inflater.inflate(R.layout.fragment_circle_attention, container, false);
        initActionBar(view);
        return view;
    }

    private void initActionBar(View view) {
        actionBar = (CommonActionBar) view.findViewById(R.id.common_action_bar);
        actionBar.setActionTitle("");
        actionBar.setActionLeftIconListenr(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }
}
