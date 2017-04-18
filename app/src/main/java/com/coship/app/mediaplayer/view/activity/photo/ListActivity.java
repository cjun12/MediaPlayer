package com.coship.app.mediaplayer.view.activity.photo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.coship.app.mediaplayer.R;
import com.coship.app.mediaplayer.presenter.photo.impl.ListPresenterImpl;
import com.coship.app.mediaplayer.presenter.photo.interfaces.IListPresenter;
import com.coship.app.mediaplayer.view.adapter.PhotoListAdapter;
import com.coship.app.mediaplayer.view.interfaces.photo.IListView;

/**
 * Created by 980558 on 2017/4/5.
 */

public class ListActivity extends AppCompatActivity implements IListView {

    private IListPresenter presenter;

    private ListView lv_photoList;
    private RecyclerView rv_photoList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_list_activity);

        presenter = new ListPresenterImpl(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadAllPhoto();
        initListView();
        initRecycleView();
    }

    private void initListView() {
        lv_photoList = (ListView)findViewById(R.id.lv_photo_list);
        presenter.loadFolderList();
        lv_photoList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.loadPhoto(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initRecycleView() {
        rv_photoList = (RecyclerView) findViewById(R.id.rv_photo_list);
        rv_photoList.setLayoutManager(new GridLayoutManager(this,4));
    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showAllPhotoList() {

    }

    @Override
    public void showPhoto(PhotoListAdapter photoAdapter) {
        rv_photoList.setAdapter(photoAdapter);
        photoAdapter.notifyDataSetChanged();
    }

    @Override
    public void showFolderList(SimpleAdapter adapter) {
        lv_photoList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
