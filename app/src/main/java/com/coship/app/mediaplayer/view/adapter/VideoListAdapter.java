package com.coship.app.mediaplayer.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coship.app.mediaplayer.R;
import com.coship.app.mediaplayer.control.ImageRecycleView;
import com.coship.app.mediaplayer.model.interfaces.IPhotoModel;
import com.coship.app.mediaplayer.model.interfaces.IVideoModel;
import com.coship.app.mediaplayer.toolkit.BitmapUtil;
import com.coship.app.mediaplayer.view.activity.photo.ShowActivity;
import com.coship.app.mediaplayer.view.activity.video.PlayActivity;

import java.lang.ref.WeakReference;

/**
 * Created by 980558 on 2017/4/21.
 */

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoListHolder> implements View.OnClickListener{
    private static final String TAG = "VideoListAdapter";
    private IVideoModel mDatas;
    private Context mContext;
    private Bitmap mPlaceHolderBitmap;
    private int mSelectedIndex;
    private LayoutInflater inflater;

    public VideoListAdapter(Context context, IVideoModel datas) {
        this.mContext = context;
        this.mDatas = datas;
        mPlaceHolderBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_image);
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    public VideoListAdapter.VideoListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.video_list_item, parent, false);
        final VideoListAdapter.VideoListHolder holder = new VideoListAdapter.VideoListHolder(view);
        view.setClickable(true);
        return holder;
    }

    @Override
    public void onBindViewHolder(final VideoListHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: "+mDatas.get(position).getName());
        loadBitmap(mDatas.get(position).getPath(), holder.img);
        holder.txt.setText(mDatas.get(position).getName());
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

    class VideoListHolder extends RecyclerView.ViewHolder {
        ImageRecycleView img;
        TextView txt;

        public VideoListHolder(View view) {
            super(view);
            img = (ImageRecycleView) view.findViewById(R.id.item_field_image);
            txt = (TextView) view.findViewById(R.id.item_field_name);
        }
    }
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick: "+mSelectedIndex);
        Intent intent = new Intent(mContext, PlayActivity.class);
        intent.putExtra("pos",mSelectedIndex);
        mContext.startActivity(intent);
    }

    public void loadBitmap(String path, ImageRecycleView imageView) {
        if (cancelPotentialWork(path, imageView)) {
            final VideoListAdapter.BitmapWorkerTask task = new VideoListAdapter.BitmapWorkerTask(imageView);

            final VideoListAdapter.AsyncDrawable asyncDrawable =
                    new VideoListAdapter.AsyncDrawable(mContext.getResources(), mPlaceHolderBitmap, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(path);
        }
    }

    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<VideoListAdapter.BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap,
                             VideoListAdapter.BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference =
                    new WeakReference<>(bitmapWorkerTask);
        }

        public VideoListAdapter.BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    public static boolean cancelPotentialWork(String path, ImageRecycleView imageView) {
        final VideoListAdapter.BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

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

    private static VideoListAdapter.BitmapWorkerTask getBitmapWorkerTask(ImageRecycleView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof VideoListAdapter.AsyncDrawable) {
                final VideoListAdapter.AsyncDrawable asyncDrawable = (VideoListAdapter.AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    public static class BitmapWorkerTask extends AsyncTask<String, Integer, Bitmap> {
        private ImageRecycleView mView;
        private float mDensity;

        public BitmapWorkerTask(ImageRecycleView view) {
            this.mView = view;
            mDensity = view.getResources().getDisplayMetrics().density;
        }

        public String path;

        @Override
        protected Bitmap doInBackground(String[] params) {
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(
                    params[0], MediaStore.Video.Thumbnails.MINI_KIND);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap o) {
            mView.setImageBitmap(o);
        }
    }
}
