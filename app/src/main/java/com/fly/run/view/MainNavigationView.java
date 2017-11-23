package com.fly.run.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.fly.run.R;
import com.fly.run.adapter.NavMainAdapter;

/**
 * Created by xinzhendi-031 on 2016/10/26.
 */
public class MainNavigationView extends RelativeLayout {

    private ListView navListView;
    private NavMainAdapter navMainAdapter;

    public MainNavigationView(Context context) {
        super(context);
        init();
    }

    public MainNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.nav_main_view, this);
        navListView = (ListView) view.findViewById(R.id.nav_list);
        navMainAdapter = new NavMainAdapter(getContext());
        navListView.setAdapter(navMainAdapter);
    }

    private ClickListener clickListener;

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        public void run();
    }
}
