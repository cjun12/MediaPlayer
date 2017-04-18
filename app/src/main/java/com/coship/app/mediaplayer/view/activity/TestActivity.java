package com.coship.app.mediaplayer.view.activity;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.coship.app.mediaplayer.R;
import com.coship.app.mediaplayer.view.activity.music.ListActivity;
import com.coship.app.mediaplayer.view.activity.music.PlayActivity;

import java.io.File;


/**
 * Created by 980558 on 2017/4/7.
 */

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TestActivity";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        findViewById(R.id.Home).setOnClickListener(this);
        findViewById(R.id.music_list).setOnClickListener(this);
        findViewById(R.id.music_play).setOnClickListener(this);
        findViewById(R.id.photo_list).setOnClickListener(this);
//        String[] paths = new String[]{Environment.getExternalStorageDirectory().getAbsolutePath()};
//        MediaScannerConnection.scanFile(this, paths, null,new MediaScannerConnection.OnScanCompletedListener(){
//            @Override
//            public void onScanCompleted(String path, Uri uri) {
//                Log.d(TAG, "onScanCompleted: path"+path+" uri :"+uri);
//                listFile(path);
//            }
//        });
    }

    void listFile(String path){
        Log.i(TAG, "listFile: "+path);
        File file = new File(path);
        if(file.isDirectory()) {
            File[] f = file.listFiles();
            for(File item :f){
                listFile(item.getPath());
            }
        }
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.Home:
                intent  =  new Intent(TestActivity.this,HomeActivity.class);
                startActivity(intent);
                break;
            case R.id.music_list:
                intent =  new Intent(TestActivity.this,ListActivity.class);
                startActivity(intent);
                break;
            case R.id.music_play:
                 intent  =  new Intent(TestActivity.this,PlayActivity.class);
                startActivity(intent);
                break;
            case R.id.photo_list:
                intent  =  new Intent(TestActivity.this, com.coship.app.mediaplayer.view.activity.photo.ListActivity.class);
                startActivity(intent);
                break;
            case R.id.photo_play:
                intent  =  new Intent(TestActivity.this,HomeActivity.class);
                startActivity(intent);
                break;
            case R.id.file_list:
                intent  =  new Intent(TestActivity.this,HomeActivity.class);
                startActivity(intent);
                break;
            case R.id.file_play:
                intent  =  new Intent(TestActivity.this,HomeActivity.class);
                startActivity(intent);
                break;
            case R.id.video_list:
                intent  =  new Intent(TestActivity.this,HomeActivity.class);
                startActivity(intent);
                break;
            case R.id.video_play:
                intent  =  new Intent(TestActivity.this,HomeActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
        System.exit(0);
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop: ");
        super.onStop();
    }
}
