package com.coship.app.mediaplayer.presenter.video.impl;

import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.SimpleAdapter;

import com.coship.app.mediaplayer.R;
import com.coship.app.mediaplayer.bean.photo.Photo;
import com.coship.app.mediaplayer.bean.video.VideoBean;
import com.coship.app.mediaplayer.model.impl.VideoModelImpl;
import com.coship.app.mediaplayer.model.interfaces.IVideoModel;
import com.coship.app.mediaplayer.presenter.video.interfaces.IListPresenter;
import com.coship.app.mediaplayer.view.adapter.PhotoListAdapter;
import com.coship.app.mediaplayer.view.adapter.VideoListAdapter;
import com.coship.app.mediaplayer.view.interfaces.video.IListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 980558 on 2017/4/21.
 */

public class ListPresenterImpl implements IListPresenter {
    private static final String TAG = "ListPresenterImpl";
    private IListView mView;
    private IVideoModel mModel;

    private SimpleAdapter mFolderAdapter;
    private VideoListAdapter mVideoAdapter;
    private Map<String, List<VideoBean>> mClassify = new HashMap<>();
    private List<Map<String, String>> mFolderData = new ArrayList<>();

    public ListPresenterImpl(IListView mView) {
        this.mView = mView;
        mModel = VideoModelImpl.getInstance();
    }

    @Override
    public void loadAllVideo() {
        mClassify.clear();
        List<VideoBean> videoList = new ArrayList<>();
        List<VideoBean> temp = null;
        String name = null;
        String path = null;
        String folder = null;
        VideoBean videoBean = null;
        Cursor cursor = mView.getContext().getContentResolver()
                .query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
            videoBean = new VideoBean();
            videoBean.setName(name);
            videoBean.setPath(path);

            videoList.add(videoBean);

            folder = videoBean.getFolder();
            temp = mClassify.get(folder);
            if (temp == null) {
                temp = new ArrayList<>();
            }
            temp.add(videoBean);
            mClassify.put(folder, temp);
        }
        cursor.close();
        mClassify.put("所有视频", videoList);
    }

    @Override
    public void loadFolderList() {
        mFolderData.clear();
        String[] from = {"folder", "count"};
        int[] to = {R.id.tv_photo_folder, R.id.tv_photo_folder_cnt};
        for (String key : mClassify.keySet()) {
            Map<String, String> item = new HashMap<>();
            item.put("folder", key);
            item.put("count", "共" + mClassify.get(key).size() + "个");
            mFolderData.add(item);
        }
        if (mFolderAdapter == null) {
            mFolderAdapter = new SimpleAdapter(mView.getContext(), mFolderData, R.layout.folder_photo_list_item, from, to);
            mView.showFolderList(mFolderAdapter);
        }
        mFolderAdapter.notifyDataSetChanged();
    }


    @Override
    public void loadVideo(int position) {
        Map<String, String> selectedItem = (HashMap<String, String>) mFolderAdapter.getItem(position);
        String folder = selectedItem.get("folder");
        mModel.setVideoList(mClassify.get(folder));
        if (mVideoAdapter == null) {
            mVideoAdapter = new VideoListAdapter(mView.getContext(), mModel);
            mView.showVideo(mVideoAdapter);
        }
        mVideoAdapter.notifyDataSetChanged();
    }

    @Override
    public void destroy() {

    }
}
