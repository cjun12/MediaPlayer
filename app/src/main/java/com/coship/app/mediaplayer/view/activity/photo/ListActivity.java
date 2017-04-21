package com.coship.app.mediaplayer.view.activity.photo;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.coship.app.mediaplayer.R;
import com.coship.app.mediaplayer.control.FocusGridLayoutManager;
import com.coship.app.mediaplayer.control.SpaceItemDecoration;
import com.coship.app.mediaplayer.presenter.photo.impl.ListPresenterImpl;
import com.coship.app.mediaplayer.presenter.photo.interfaces.IListPresenter;
import com.coship.app.mediaplayer.view.adapter.PhotoListAdapter;
import com.coship.app.mediaplayer.view.interfaces.photo.IListView;

/**
 * Created by 980558 on 2017/4/5.
 */

public class ListActivity extends AppCompatActivity implements IListView {
    private static final String TAG = "ListActivity";

    private IListPresenter mPresenter;

    private ListView lv_photoList;
    private RecyclerView rv_photoList;
    //    private GridView gv_photoList;
    private ImageContentObserve mObserve;
    private int mPos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_list_activity);
        mPresenter = new ListPresenterImpl(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mObserve == null) {
            mObserve = new ImageContentObserve(new Handler());
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
                mPresenter.loadAllPhoto();
                handler.sendEmptyMessage(0x0001);
            }
        });
    }

    private void initListView() {
        lv_photoList = (ListView) findViewById(R.id.lv_photo_list);
        mPresenter.loadFolderList();
        lv_photoList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.loadPhoto(position);
                mPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        lv_photoList.requestFocus();
    }

    private void initRecycleView() {
        rv_photoList = (RecyclerView) findViewById(R.id.rv_photo_list);
        rv_photoList.setLayoutManager(new FocusGridLayoutManager(this, 6));
        rv_photoList.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (rv_photoList.getChildCount() > 0) {
                        rv_photoList.getChildAt(0).requestFocus();
                    }
                }
            }
        });
    }

//    private void initGridView(){
//        gv_photoList = (GridView) findViewById(R.id.gv_photo_list);
//    }


    @Override
    public Context getContext() {
        return this;
    }


    @Override
    public void showPhoto(PhotoListAdapter photoAdapter) {
        rv_photoList.setAdapter(photoAdapter);
        photoAdapter.notifyDataSetChanged();
    }
//    @Override
//    public void showPhoto(SimpleAdapter adapter){
//        gv_photoList.setAdapter(adapter);
//    }

    @Override
    public void showFolderList(SimpleAdapter adapter) {
        lv_photoList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
        getContentResolver().unregisterContentObserver(mObserve);
    }

    private class ImageContentObserve extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public ImageContentObserve(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            Log.i(TAG, "onChange: ");
            mPresenter.loadAllPhoto();
            mPresenter.loadFolderList();
            mPresenter.loadPhoto(mPos);
        }
    }
}
