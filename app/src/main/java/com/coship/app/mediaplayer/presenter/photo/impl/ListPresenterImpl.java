package com.coship.app.mediaplayer.presenter.photo.impl;

import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.SimpleAdapter;

import com.coship.app.mediaplayer.R;
import com.coship.app.mediaplayer.bean.photo.Photo;
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
    private IListView view;
    private SimpleAdapter folderAdapter;
    private PhotoListAdapter photoAdapter;
    private Map<String, List<Photo>> classify;

    public ListPresenterImpl(IListView view) {
        this.view = view;
    }

    public void loadAllPhoto() {
        Photo photo = null;
        classify = new HashMap<>();
        List<Photo> photoList = new ArrayList<>();
        List<Photo> temp;
        Cursor cursor = view.getContext().getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            Log.i(TAG, "loadAllPhoto: " + path);
            String folder = path.substring(0, path.lastIndexOf("/"));
            String fileName = path.substring(path.lastIndexOf("/") + 1);
            photo = new Photo();
            photo.setId(id);
            photo.setFolder(folder);
            photo.setFileName(fileName);
            photoList.add(photo);
            temp = classify.get(folder);
            if (temp == null) {
                temp = new ArrayList<>();
            }
            temp.add(photo);
            classify.put(folder, temp);
        }
        classify.put("所有图片", photoList);
    }


    @Override
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

    public Map<String, Integer> loadFolderList1() {
        Map<String, Integer> result = new HashMap<>();
        Cursor cursor = view.getContext().getContentResolver()
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

    @Override
    public void loadFolderList() {
        List<Map<String, String>> data = new ArrayList<>();
        String[] from = {"folder", "count"};
        int[] to = {R.id.tv_photo_folder, R.id.tv_photo_folder_cnt};
        for (String key : classify.keySet()) {
            Map<String, String> item = new HashMap<>();
            item.put("folder", key);
            item.put("count", "共" + classify.get(key).size() + "张");
            data.add(item);
        }
        folderAdapter = new SimpleAdapter(view.getContext(), data, R.layout.folder_photo_list_item, from, to);
        view.showFolderList(folderAdapter);
    }

    @Override
    public void loadPhoto(int position) {
        Map<String, String> selectedItem = (HashMap<String, String>) folderAdapter.getItem(position);
        String folder = selectedItem.get("folder");
        List<Photo> data= classify.get(folder);
        photoAdapter = new PhotoListAdapter(view.getContext(),data);
        view.showPhoto(photoAdapter);
    }

    @Override
    public void loadfileList(String path) {
        Cursor cursor = getThumbnailsCursor();
    }

    private Cursor getThumbnailsCursor() {
        return view.getContext().getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Thumbnails.IMAGE_ID, MediaStore.Images.Thumbnails.DATA},
                null, null, null);
    }
}
