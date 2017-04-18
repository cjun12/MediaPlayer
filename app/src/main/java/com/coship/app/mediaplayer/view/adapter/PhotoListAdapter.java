package com.coship.app.mediaplayer.view.adapter;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coship.app.mediaplayer.R;
import com.coship.app.mediaplayer.bean.photo.Photo;

import java.util.List;

/**
 * Created by 980558 on 2017/4/17.
 */

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.PhotoListHolder> {
    private List<Photo> mDatas;
    private Context mContext;
    private LayoutInflater inflater;

    public PhotoListAdapter(Context context, List<Photo> datas) {
        this.mContext = context;
        this.mDatas = datas;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public PhotoListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.photoes_photo_list_item,parent, false);
        PhotoListHolder holder= new PhotoListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PhotoListHolder holder, int position) {
        String path = mDatas.get(position).getFolder()+"/"+mDatas.get(position).getFileName();
        Uri uri = Uri.parse(path);
        holder.img.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
    class PhotoListHolder extends RecyclerView.ViewHolder {

        ImageView img;

        public PhotoListHolder(View view) {
            super(view);
            img=(ImageView) view.findViewById(R.id.item_field_image);
        }

    }
}
