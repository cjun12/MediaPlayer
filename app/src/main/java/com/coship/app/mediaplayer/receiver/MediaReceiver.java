package com.coship.app.mediaplayer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by 980558 on 2017/4/13.
 */

public class MediaReceiver extends BroadcastReceiver {
    private static final String TAG = "MediaReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: "+intent.getAction());
    }
}
