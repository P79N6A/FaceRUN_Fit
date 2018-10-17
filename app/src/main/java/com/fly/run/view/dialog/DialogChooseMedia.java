package com.fly.run.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.fly.run.R;

/**
 * @author YoYo-SHUN
 * @ClassName: DialogAlertInput
 * @Description: Information dialog
 * @Copyright InventoryLab RJC LLC.  All Rights Reserved.
 * @date 2014-7-26 PM 11:43:43
 */
public class DialogChooseMedia extends Dialog {

    private TextView dialog_camera;
    private TextView dialog_album;

    private OnEventListener mOnEventListener;

    public DialogChooseMedia(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initViews();
    }

    public DialogChooseMedia(Context context, int theme) {
        super(context, theme);
        initViews();
    }

    public DialogChooseMedia(Context context) {
        this(context, R.style.AppTheme);
    }

    private void initViews() {
        setContentView(R.layout.dialog_choose_media);
        getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setBackgroundDrawable(new ColorDrawable(0x44000000));
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        lp.setMargins(DisplayUtil.dp2px(40), DisplayUtil.dp2px(320), DisplayUtil.dp2px(40), 0);
        View root = findViewById(R.id.dialog_root);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
//        root.setLayoutParams(lp);

        dialog_camera = (TextView) findViewById(R.id.dialog_camera);
        dialog_album = (TextView) findViewById(R.id.dialog_album);
        dialog_camera.setOnClickListener(mClickListener);
        dialog_album.setOnClickListener(mClickListener);
        this.setCancelable(true);
    }

    public void setOnEventListener(OnEventListener l) {
        this.mOnEventListener = l;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void cancel() {
        super.cancel();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_camera:
                    dismiss();
                    if (mOnEventListener != null)
                        mOnEventListener.result(1);
                    break;
                case R.id.dialog_album:
                    dismiss();
                    if (mOnEventListener != null)
                        mOnEventListener.result(2);
                    break;

            }
        }
    };

    public interface OnEventListener {
        /**
         * @param index
         */
        void result(int index);
    }

}
