package com.coship.app.mediaplayer.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.UiThread;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.coship.app.mediaplayer.R;
import com.coship.app.mediaplayer.bean.photo.Photo;
import com.coship.app.mediaplayer.control.ImageRecycleView;
import com.coship.app.mediaplayer.model.interfaces.IPhotoModel;
import com.coship.app.mediaplayer.toolkit.BitmapUtil;
import com.coship.app.mediaplayer.view.activity.photo.ShowActivity;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by 980558 on 2017/4/17.
 */

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.PhotoListHolder> implements View.OnClickListener{
    private IPhotoModel mDatas;
    private Context mContext;
    private Bitmap mPlaceHolderBitmap;
    private int mSelectedIndex;
    private LayoutInflater inflater;
    private static final String TAG = "PhotoListAdapter";

    public PhotoListAdapter(Context context, IPhotoModel datas) {
        this.mContext = context;
        this.mDatas = datas;
        mPlaceHolderBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_image);
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public PhotoListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.photoes_photo_list_item, parent, false);
        final PhotoListHolder holder = new PhotoListHolder(view);
        view.setClickable(true);
        return holder;
    }

    @Override
    public void onBindViewHolder(final PhotoListHolder holder, final int position) {
//        String path = mDatas.get(position).getPath();
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
//        options.inJustDecodeBounds = false;
//        options.inSampleSize = BitmapUtil.computeSampleSize(options, -1, 200 * 200);
//        bitmap = BitmapFactory.decodeFile(path, options);
//        holder.img.setImageBitmap(bitmap);
        loadBitmap(mDatas.get(position).getPath(), holder.img);

        holder.itemView.setFocusable(true);

        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mSelectedIndex = holder.getLayoutPosition();
                Log.i(TAG, "onFocusChange: "+mSelectedIndex);
            }
        });
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick: "+mSelectedIndex);
        Intent intent = new Intent(mContext, ShowActivity.class);
        intent.putExtra("pos",mSelectedIndex);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onViewRecycled(PhotoListHolder holder) {
        super.onViewRecycled(holder);
        Bitmap bitmap = holder.img.getBitmap();
        if (bitmap != null) {
            bitmap.recycle();
        }
        holder.img.setImageDrawable(null);
    }


    class PhotoListHolder extends RecyclerView.ViewHolder {

        ImageRecycleView img;

        public PhotoListHolder(View view) {
            super(view);
            img = (ImageRecycleView) view.findViewById(R.id.item_field_image);

        }

    }

    public void loadBitmap(String path, ImageRecycleView imageView) {
        if (cancelPotentialWork(path, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView);

            final AsyncDrawable asyncDrawable =
                    new AsyncDrawable(mContext.getResources(), mPlaceHolderBitmap, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(path);
        }
    }

    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap,
                             BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference =
                    new WeakReference<>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    public static boolean cancelPotentialWork(String path, ImageRecycleView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final String bitmapData = bitmapWorkerTask.path;
            if (bitmapData != path) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageRecycleView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    public static class BitmapWorkerTask extends AsyncTask<String, Integer, Bitmap> {
        private ImageRecycleView mView;

        public BitmapWorkerTask(ImageRecycleView view) {
            this.mView = view;
        }

        public String path;

        @Override
        protected Bitmap doInBackground(String[] params) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(params[0].toString(), options);
            options.inJustDecodeBounds = false;
            options.inSampleSize = BitmapUtil.computeSampleSize(options, -1, 150*150);
            bitmap = BitmapFactory.decodeFile(params[0], options);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap o) {
            mView.setImageBitmap(o);
        }
    }
}
