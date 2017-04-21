package com.coship.app.mediaplayer.presenter.photo.impl;

import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.SimpleAdapter;

import com.coship.app.mediaplayer.R;
import com.coship.app.mediaplayer.bean.photo.Photo;
import com.coship.app.mediaplayer.model.impl.PhotoModelImpl;
import com.coship.app.mediaplayer.model.interfaces.IPhotoModel;
import com.coship.app.mediaplayer.presenter.photo.interfaces.IListPresenter;
import com.coship.app.mediaplayer.view.adapter.PhotoListAdapter;
import com.coship.app.mediaplayer.view.interfaces.photo.IListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


/**
 * Created by 980558 on 2017/4/5.
 */

public class ListPresenterImpl implements IListPresenter {
    private static final String TAG = "ListPresenterImpl";
    private IListView mView;
    private SimpleAdapter mFolderAdapter;
    private PhotoListAdapter mPhotoAdapter;
    //    private SimpleAdapter mPhotoAdapter;
    private Map<String, List<Photo>> mClassify = new HashMap<>();
    private List<Map<String, String>> mFolderData = new ArrayList<>();
    //    private List<Map<String, Object>> mPhotoData = new ArrayList<>();
    private IPhotoModel mPhotoModel;
//    private List<Photo> mPhotoData = new ArrayList<>();

    public ListPresenterImpl(IListView view) {
        this.mView = view;
        mPhotoModel = PhotoModelImpl.getInstance();
    }

    public void loadAllPhoto() {
        Photo photo = null;
        String id;
        String path;
        String folder;
        String thumbPath;
        mClassify.clear();
        List<Photo> photoList = new ArrayList<>();
        List<Photo> temp;
        Cursor cursor = mView.getContext().getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            thumbPath = queryThumbPath(id);
            photo = new Photo();
            photo.setId(id);
            photo.setPath(path);
            photo.setThumbPath(thumbPath);

            photoList.add(photo);

            folder = photo.getFolder();
            temp = mClassify.get(folder);
            if (temp == null) {
                temp = new ArrayList<>();
            }
            temp.add(photo);
            mClassify.put(folder, temp);
        }
        cursor.close();
        mClassify.put("所有图片", photoList);
        Log.i(TAG, "loadAllPhoto: " + photoList.size());
    }

    private String queryThumbPath(String imageId) {
        String result = null;
        String[] projection = {MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID,
                MediaStore.Images.Thumbnails.DATA};
        Cursor cursor = mView.getContext().getContentResolver()
                .query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI
                        , projection, MediaStore.Images.Thumbnails.IMAGE_ID + "=?"
                        , new String[]{imageId}, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
        }
        return result;
    }

    @Override
    public void loadFolderList() {
        mFolderData.clear();
        String[] from = {"folder", "count"};
        int[] to = {R.id.tv_photo_folder, R.id.tv_photo_folder_cnt};
        for (String key : mClassify.keySet()) {
            Map<String, String> item = new HashMap<>();
            item.put("folder", key);
            item.put("count", "共" + mClassify.get(key).size() + "张");
            mFolderData.add(item);
        }
        if (mFolderAdapter == null) {
            mFolderAdapter = new SimpleAdapter(mView.getContext(), mFolderData, R.layout.folder_photo_list_item, from, to);
            mView.showFolderList(mFolderAdapter);
        }

        Log.i(TAG, "loadFolderList: " + mFolderAdapter.getCount());
        mFolderAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadPhoto(int position) {
        Map<String, String> selectedItem = (HashMap<String, String>) mFolderAdapter.getItem(position);
        String folder = selectedItem.get("folder");
//        mPhotoData.clear();
//        mPhotoData.addAll(mClassify.get(folder));
        mPhotoModel.setPhotoList(mClassify.get(folder));
        if (mPhotoAdapter == null) {
            mPhotoAdapter = new PhotoListAdapter(mView.getContext(), mPhotoModel);
            mView.showPhoto(mPhotoAdapter);
        }
        mPhotoAdapter.notifyDataSetChanged();
    }


    @Override
    public void destroy() {
        mFolderData.clear();
    }

    public Map<String, Integer> loadFolderList1() {
        Map<String, Integer> result = new HashMap<>();
        Cursor cursor = mView.getContext().getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (cursor == null || cursor.getCount() <= 0) {
            return result;
        }
        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            int pos = path.indexOf('/');
            String folderPath = path.substring(0, pos);
            if (result.get(folderPath) != null) {
                int cnt = result.get(folderPath);
                result.put(folderPath, cnt++);
            } else {
                result.put(folderPath, 1);
            }
        }
        cursor.close();
        return result;
    }


    public List<Map<String, String>> loadAllPhotoList() {
        List<Map<String, String>> list = new ArrayList<>();
        Cursor cursor = getThumbnailsCursor();
        if (cursor == null || cursor.getCount() <= 0) {
            return list;
        }
        while (cursor.moveToNext()) {
            Map<String, String> item = new HashMap<>();
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
            int image_id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID));
            item.put("image_id", image_id + "");
            item.put("path", path);
            list.add(item);
        }
        cursor.close();
        return list;
    }

    public void loadfileList(String path) {
        Cursor cursor = getThumbnailsCursor();
    }

    private Cursor getThumbnailsCursor() {
        return mView.getContext().getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Thumbnails.IMAGE_ID, MediaStore.Images.Thumbnails.DATA},
                null, null, null);
    }
}
