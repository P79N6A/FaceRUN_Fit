package com.fly.run.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.fly.run.R;

import java.util.List;

/**
 * Created by xinzhendi-031 on 2016/12/14.
 */
public class NavMainAdapter extends BaseAdapter {

    private Context mContext;
//    private List<String> datas = new ArrayList<>();

    //    private String[] navNames = {"我的行程", "位置可见", "音乐开关", "应用设置", "唤醒提示", "关于我们"};
//    private String[] navNames = {"我的行程", "跑步训练", "囚徒健身", "应用设置", "唤醒提示", "关于我们"};
    private String[] navNames;
    private int[] navDrawables = {R.drawable.ic_menu_send, R.mipmap.ic_launcher,
            R.mipmap.ic_menu_fit, R.drawable.ic_menu_manage, R.drawable.ic_menu_share};

    public NavMainAdapter(Context context) {
        this.mContext = context;
        navNames = context.getResources().getStringArray(R.array.array_nav_menu);
    }

    public void setData(List<Integer> icons, List<String> list) {
    }

    @Override
    public int getCount() {
        return navNames.length;
    }

    @Override
    public String getItem(int position) {
        return navNames[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_nav_main, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String content = getItem(position);
        viewHolder.tvName.setText(content);
        viewHolder.tvName.setTextColor(mContext.getResources().getColor(R.color.color_2E1E1C));
        viewHolder.navIcon.setImageResource(navDrawables[position]);
        viewHolder.navIcon.setColorFilter(mContext.getResources().getColor(R.color.color_2E1E1C));
//        if (getItem(position).equals(navNames[1])) {
//            boolean visiable = SharePreferceTool.getInstance().getBoolean("SwitchLocationVisible", true);
//            viewHolder.navSwitch.setChecked(visiable);
//            viewHolder.navSwitch.setVisibility(View.VISIBLE);
//            viewHolder.navSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    SharePreferceTool.getInstance().setCache("SwitchLocationVisible", isChecked);
//                }
//            });
//        } else if (getItem(position).equals(navNames[2])) {
//            boolean open = SharePreferceTool.getInstance().getBoolean("SwitchMusic", true);
//            viewHolder.navSwitch.setChecked(open);
//            viewHolder.navSwitch.setVisibility(View.VISIBLE);
//            viewHolder.navSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    SharePreferceTool.getInstance().setCache("SwitchMusic", isChecked);
//                }
//            });
//        } else {
//            viewHolder.navSwitch.setVisibility(View.GONE);
//        }
        return convertView;
    }

    private class ViewHolder {
        public RelativeLayout nav_item_layout;
        public TextView tvName;
        public ImageView navIcon;
        public Switch navSwitch;

        public ViewHolder(View view) {
            this.nav_item_layout = (RelativeLayout) view.findViewById(R.id.nav_item_layout);
            this.tvName = (TextView) view.findViewById(R.id.tv_nav_name);
            this.navIcon = (ImageView) view.findViewById(R.id.iv_nav_icon);
            this.navSwitch = (Switch) view.findViewById(R.id.nav_switch);
        }
    }
}
