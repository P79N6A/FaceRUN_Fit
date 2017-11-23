package com.fly.run.fragment.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.fly.run.R;

/**
 * Created by xinzhendi-031 on 2017/1/25.
 */
public class DialogAlertFragment extends DialogFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageView ivHeader;
    private ImageView ivClose;
    private TextView tvTitle, tvContent;

    private OnEventListener mOnEventListener;
    private String title, content;

    public static DialogAlertFragment newInstance(String title, String content) {
        DialogAlertFragment fragment = new DialogAlertFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, title);
        args.putSerializable(ARG_PARAM2, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        //设置动画
        params.windowAnimations = R.style.NearbyDialogFragment;
        window.setAttributes(params);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM1);
            content = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_alert_runner, container, false);
        tvTitle = (TextView) view.findViewById(R.id.dialog_title);
        tvContent = (TextView) view.findViewById(R.id.dialog_content);
//        this.setCancelable(false);
        tvTitle.setText(title);
        tvContent.setText(content);
        return view;
    }

    public void setTvTitle(String name) {
        if (tvTitle != null && !TextUtils.isEmpty(name)) {
            tvTitle.setText(name);
        }
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public interface OnEventListener {

        /**
         * @param sure Are you sure
         */
        void result(boolean sure);
    }
}
