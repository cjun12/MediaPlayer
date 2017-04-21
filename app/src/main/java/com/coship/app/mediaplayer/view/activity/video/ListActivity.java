package com.coship.app.mediaplayer.view.activity.video;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.coship.app.mediaplayer.R;
import com.coship.app.mediaplayer.control.FocusGridLayoutManager;
import com.coship.app.mediaplayer.presenter.video.impl.ListPresenterImpl;
import com.coship.app.mediaplayer.presenter.video.interfaces.IListPresenter;
import com.coship.app.mediaplayer.view.adapter.VideoListAdapter;
import com.coship.app.mediaplayer.view.interfaces.video.IListView;

/**
 * Created by 980558 on 2017/4/21.
 */

public class ListActivity extends AppCompatActivity implements IListView{

    private IListPresenter mPresenter;
    private VideoContentObserve mObserve;
    private int mPos;

    private ListView lvFolder;
    private RecyclerView rvVideo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_list_activity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter = new ListPresenterImpl(this);
        if (mObserve == null) {
            mObserve = new VideoContentObserve(new Handler());
        }
        getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , true, mObserve);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                initListView();
                initRecycleView();
//                initGridView();
            }
        };
        handler.post(new Runnable() {
            @Override
            public void run() {
                mPresenter.loadAllVideo();
                handler.sendEmptyMessage(0x0001);
            }
        });
    }

    private void initListView() {
        lvFolder = (ListView) findViewById(R.id.lv_video_list);
        mPresenter.loadFolderList();
        lvFolder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.loadVideo(position);
                mPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        lvFolder.requestFocus();
    }

    private void initRecycleView() {
        rvVideo = (RecyclerView) findViewById(R.id.rv_video_list);
        rvVideo.setLayoutManager(new FocusGridLayoutManager(this, 3));
        rvVideo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (rvVideo.getChildCount() > 0) {
                        rvVideo.getChildAt(0).requestFocus();
                    }
                }
            }
        });
    }
    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showFolderList(SimpleAdapter adapter) {
        lvFolder.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showVideo(VideoListAdapter photoAdapter) {
        rvVideo.setAdapter(photoAdapter);
        photoAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
        getContentResolver().unregisterContentObserver(mObserve);
    }
    private class VideoContentObserve extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public VideoContentObserve(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            mPresenter.loadAllVideo();
            mPresenter.loadFolderList();
            mPresenter.loadVideo(mPos);
        }
    }
}
