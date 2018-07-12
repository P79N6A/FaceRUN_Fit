package com.fly.run.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

import com.fly.run.R;
import com.fly.run.utils.ToastUtil;

/**
 * @author YoYo-SHUN
 * @ClassName: DialogAlertInput
 * @Description: Information dialog
 * @Copyright InventoryLab RJC LLC.  All Rights Reserved.
 * @date 2014-7-26 PM 11:43:43
 */
public class DialogTextContent extends Dialog {

    private EditText dialog_et_content;
    private ImageView dialog_iv_sure;

    public static final int ACTION_TYPE_NAME = 1001;
    public static final int ACTION_TYPE_SEX = 1002;
    public static final int ACTION_TYPE_DESC = 1003;
    private int mActionType;

    private OnEventListener mOnEventListener;

    public DialogTextContent(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initViews();
    }

    public DialogTextContent(Context context, int theme) {
        super(context, theme);
        initViews();
    }

    public DialogTextContent(Context context) {
        this(context, R.style.DialogFullScreen);
    }

    private void initViews() {
        setContentView(R.layout.dialog_text_content);
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
        dialog_et_content = (EditText) findViewById(R.id.dialog_et_content);
        dialog_iv_sure = (ImageView) findViewById(R.id.dialog_iv_sure);
        dialog_iv_sure.setOnClickListener(mClickListener);
        this.setCancelable(true);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        dialog_et_content.requestFocus();
    }

    public void setTypeLine(int line, int type){
        if (line == 1)
            dialog_et_content.setSingleLine();
        else {
            if (line > 5)
                line = 5;
            dialog_et_content.setMinLines(line);
        }
        mActionType = type;
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
                case R.id.dialog_iv_sure:
                    String strContent = dialog_et_content.getText().toString();
                    if (TextUtils.isEmpty(strContent) || TextUtils.isEmpty(strContent.trim())){
                        ToastUtil.show("请输入内容");
                        return;
                    }
                    dismiss();
                    if (mOnEventListener != null){
                        if (mActionType > 0)
                            mOnEventListener.result(strContent,mActionType);
                        else
                            mOnEventListener.result(strContent);
                    }
                    break;
            }
        }
    };

    public interface OnEventListener {
        /**
         * @param content
         */
        void result(String content);

        /**
         * @param content
         */
        void result(String content,int type);
    }

}
