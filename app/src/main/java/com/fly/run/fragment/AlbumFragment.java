package com.fly.run.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.fly.run.R;
import com.fly.run.adapter.GalleryAdapter;
import com.fly.run.bean.PhotoItem;

import java.util.ArrayList;

/**
 * @author tongqian.ni
 */
public class AlbumFragment extends Fragment {
    private ArrayList<PhotoItem> photos = new ArrayList<PhotoItem>();
    private GalleryAdapter adapter;

    public AlbumFragment() {
        super();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public static Fragment newInstance(ArrayList<PhotoItem> photos) {
        Fragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        args.putSerializable("photos", photos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_album, null);
        photos = (ArrayList<PhotoItem>) getArguments().getSerializable("photos");
        albums = (GridView) root.findViewById(R.id.albums);
        adapter = new GalleryAdapter(getActivity(), photos);
        albums.setAdapter(adapter);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

	private GridView albums;
}
