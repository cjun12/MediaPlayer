package com.coship.app.mediaplayer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by 980558 on 2017/4/13.
 */

public class MediaReceiver extends BroadcastReceiver {
    private static final String TAG = "MediaReceiver";

    public MediaReceiver() {
        super();
        Log.i(TAG, "MediaReceiver: onReceive");
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.i(TAG, "onReceive: " + intent.getAction());
        Handler handler = new Handler();
        if (Intent.ACTION_MEDIA_UNMOUNTED.equals(intent.getAction())) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    context.getContentResolver().notifyChange(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null);
                }
            }, 1000);
        }else {
            context.getContentResolver().notifyChange(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null);
        }
    }

}
