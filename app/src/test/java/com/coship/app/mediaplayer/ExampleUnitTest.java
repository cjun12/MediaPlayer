package com.coship.app.mediaplayer;

import org.junit.Test;

import java.io.File;

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