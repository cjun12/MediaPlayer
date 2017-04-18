package com.coship.app.mediaplayer.Toolkit;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by 980558 on 2017/4/14.
 */

public class AudioHelper {
    Context context;

    public AudioHelper(Context context) {
        this.context = context;
    }

    public String getAlbumImagePath(String albumId) {
        Uri album = Uri.parse("content://media/external/audio/albums/" + albumId);
        Cursor cursor = context.getContentResolver()
                .query(album, new String[]{"album_art"}, null, null, null);
        String path = null;
        if(cursor.getCount()>0&&cursor.getColumnCount() > 0){
            cursor.moveToNext();
            path= cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM_ART));
            Log.i(TAG, "getAlbumImagePath: "+path);
        }
        cursor.close();
        return path;
    }

}
