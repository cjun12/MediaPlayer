package com.coship.app.mediaplayer.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.coship.app.mediaplayer.R;
import com.coship.app.mediaplayer.view.adapter.SongAdapter;

import java.util.List;

/**
 * Created by 980558 on 2017/4/19.
 */

public class MusicListFragment extends Fragment {
    private View mView;

    private ListView lv_lists;
    private SongAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.music_list_fragment, container, false);
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        initList();
    }

    private void initList() {
        lv_lists = (ListView) mView.findViewById(R.id.lv_music_list);
        lv_lists.setAdapter(mAdapter);
    }

    public static MusicListFragment newInstance(SongAdapter adapter){
        MusicListFragment fragment = new MusicListFragment();
        fragment.mAdapter = adapter;
        return fragment;
    }
}
