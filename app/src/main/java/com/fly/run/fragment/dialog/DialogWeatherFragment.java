package com.fly.run.fragment.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.fly.run.R;
import com.fly.run.bean.WeatherBean;
import com.fly.run.utils.TimeFormatUtils;
import com.fly.run.utils.WeatherUtil;

/**
 * Created by xinzhendi-031 on 2017/1/25.
 */
public class DialogWeatherFragment extends DialogFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageView ivHeader;
    private ImageView ivClose;
    //    private TextView tvTitle, tvContent;
    private TextView dialog_data_weather, dialog_data_air, dialog_data_temperature, dialog_data_sport, dialog_data_update_time;

    private OnEventListener mOnEventListener;
    private String title, content;

    public static DialogWeatherFragment newInstance(String title, String content) {
        DialogWeatherFragment fragment = new DialogWeatherFragment();
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
        View view = inflater.inflate(R.layout.dialog_weather_info, container, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        dialog_data_weather = (TextView) view.findViewById(R.id.dialog_data_weather);
        dialog_data_air = (TextView) view.findViewById(R.id.dialog_data_air);
        dialog_data_temperature = (TextView) view.findViewById(R.id.dialog_data_temperature);
        dialog_data_sport = (TextView) view.findViewById(R.id.dialog_data_sport);
        dialog_data_update_time = (TextView) view.findViewById(R.id.dialog_data_update_time);
        WeatherBean bean = WeatherUtil.weatherBean;
        if (bean != null) {
            dialog_data_weather.setText(bean.weather);
            dialog_data_air.setText(bean.airCondition + "（" + bean.getPollutionIndex() + "）");
            dialog_data_temperature.setText(bean.temperature);
            dialog_data_sport.setText(bean.exerciseIndex);
            dialog_data_update_time.setText("此次更新时间：" + TimeFormatUtils.dateFormatStr(bean.getUpdateTime()));
        }
        return view;
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
