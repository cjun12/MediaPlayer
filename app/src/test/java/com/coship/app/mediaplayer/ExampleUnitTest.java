package com.coship.app.mediaplayer;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.coship.app.mediaplayer.Toolkit.StringConvertor;
import com.coship.app.mediaplayer.bean.music.Song;

import org.junit.Test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.id.list;
import static android.content.ContentValues.TAG;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void loadLrcPath(){
        String result = null;
        String path = "C:\\Users\\980558\\Desktop";
        File file = new File(path);
        File [] fileList= file.listFiles();
        for(File item : fileList){
            if(item.isDirectory()){
                continue;
            }
            String FileName=  item.getName();
            System.out.println(item);
            String fileFormat = FileName.substring(FileName.lastIndexOf("."));
            if(".lrc".equals(fileFormat)&&FileName.contains("告白气球")){
                result = FileName;
            }
        }
        System.out.println(result);
    }
}