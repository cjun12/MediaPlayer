package com.coship.app.mediaplayer.control;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by 980558 on 2017/4/17.
 */
class LyricObject {
    public int begintime; // 开始时间
    public int endtime; // 结束时间
    public int timeline; // 单句歌词用时
    public String lrc; // 单句歌词
}

public class LyricView extends View {
    private static final String TAG = "LyricView";
    private static TreeMap<Integer, LyricObject> lrc_map;
    private float mX; //屏幕X轴的中点，此值固定，保持歌词在X中间显示
    private float offsetY; //歌词在Y轴上的偏移量，此值会根据歌词的滚动变小
    private static boolean blLrc = false;
    private float touchY; //当触摸歌词View时，保存为当前触点的Y轴坐标
    private float touchX;
    private boolean blScrollView = false;
    private int lrcIndex = 0; //保存歌词TreeMap的下标
    private int SIZEWORD = 0;//显示歌词文字的大小值
    private int INTERVAL = 45;//歌词每行的间隔
    Paint paint = new Paint();//画笔，用于画不是高亮的歌词
    Paint paintHL = new Paint(); //画笔，用于画高亮的歌词，即当前唱到这句歌词

    public LyricView(Context context) {
        super(context);
        init();
    }

    public LyricView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void onDraw(Canvas canvas) {
        if (blLrc) {
            paintHL.setTextSize(SIZEWORD);
            paint.setTextSize(SIZEWORD);
            LyricObject temp = lrc_map.get(lrcIndex);
            canvas.drawText(temp.lrc, mX, offsetY + (SIZEWORD + INTERVAL) * lrcIndex, paintHL);
// 画当前歌词之前的歌词
            for (int i = lrcIndex - 1; i >= 0; i--) {
                temp = lrc_map.get(i);
                if (offsetY + (SIZEWORD + INTERVAL) * i < 0) {
                    break;
                }
                canvas.drawText(temp.lrc, mX, offsetY + (SIZEWORD + INTERVAL) * i, paint);
            }
// 画当前歌词之后的歌词
            for (int i = lrcIndex + 1; i < lrc_map.size(); i++) {
                temp = lrc_map.get(i);
                if (offsetY + (SIZEWORD + INTERVAL) * i > 600) {
                    break;
                }
                canvas.drawText(temp.lrc, mX, offsetY + (SIZEWORD + INTERVAL) * i, paint);
            }
        } else {
            paint.setTextSize(25);
            canvas.drawText("找不到歌词", mX, 310, paint);
        }
        super.onDraw(canvas);
    }

    public void init() {
        lrc_map = new TreeMap<Integer, LyricObject>();
        offsetY = 320;
        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.GREEN);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setAlpha(180);
        paintHL = new Paint();
        paintHL.setTextAlign(Paint.Align.CENTER);
        paintHL.setColor(Color.RED);
        paintHL.setAntiAlias(true);
        paintHL.setAlpha(255);
    }

    public void SetTextSize() {
        if (!blLrc) {
            return;
        }
        int max = lrc_map.get(0).lrc.length();
        for (int i = 1; i < lrc_map.size(); i++) {
            LyricObject lrcStrLength = lrc_map.get(i);
            if (max < lrcStrLength.lrc.length()) {
                max = lrcStrLength.lrc.length();
            }
        }
        SIZEWORD = 320 / max;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mX = w * 0.5f;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public Float SpeedLrc() {
        float speed = 0;
        if (offsetY + (SIZEWORD + INTERVAL) * lrcIndex > 220) {
            speed = ((offsetY + (SIZEWORD + INTERVAL) * lrcIndex - 220) / 20);
        } else if (offsetY + (SIZEWORD + INTERVAL) * lrcIndex < 120) {
            speed = 0;
        }
        return speed;
    }

    public int SelectIndex(int time) {
        if (!blLrc) {
            return 0;
        }
        int index = 0;
        for (int i = 0; i < lrc_map.size(); i++) {
            LyricObject temp = lrc_map.get(i);
            if (temp.begintime < time) {
                ++index;
            }
        }
        lrcIndex = index - 1;
        if (lrcIndex < 0) {
            lrcIndex = 0;
        }
        return lrcIndex;
    }

    public static void read(String file) {
        Log.i(TAG, "read: "+file);
        if(file==null){
            return;
        }
        TreeMap<Integer, LyricObject> lrc_read = new TreeMap<Integer, LyricObject>();
        String data = "";
        try {
            File saveFile = new File(file);
            if (!saveFile.isFile()) {
                blLrc = false;
                return;
            }
            blLrc = true;
            FileInputStream stream = new FileInputStream(saveFile);// context.openFileInput(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            int i = 0;
            Pattern pattern = Pattern.compile("\\d{2}");
            while ((data = br.readLine()) != null) {
                data = data.replace("[", "");//将前面的替换成后面的
                data = data.replace("]", "@");
                String splitdata[] = data.split("@");//分隔
                if (data.endsWith("@")) {
                    for (int k = 0; k < splitdata.length; k++) {
                        String str = splitdata[k];
                        str = str.replace(":", ".");
                        str = str.replace(".", "@");
                        String timedata[] = str.split("@");
                        Matcher matcher = pattern.matcher(timedata[0]);
                        if (timedata.length == 3 && matcher.matches()) {
                            int m = Integer.parseInt(timedata[0]); //分
                            int s = Integer.parseInt(timedata[1]); //秒
                            int ms = Integer.parseInt(timedata[2]); //毫秒
                            int currTime = (m * 60 + s) * 1000 + ms * 10;
                            LyricObject item1 = new LyricObject();
                            item1.begintime = currTime;
                            item1.lrc = "";
                            lrc_read.put(currTime, item1);
                        }
                    }
                } else {
                    String lrcContenet = splitdata[splitdata.length - 1];
                    for (int j = 0; j < splitdata.length - 1; j++) {
                        String tmpstr = splitdata[j];
                        tmpstr = tmpstr.replace(":", ".");
                        tmpstr = tmpstr.replace(".", "@");
                        String timedata[] = tmpstr.split("@");
                        Matcher matcher = pattern.matcher(timedata[0]);
                        if (timedata.length == 3 && matcher.matches()) {
                            int m = Integer.parseInt(timedata[0]); //分
                            int s = Integer.parseInt(timedata[1]); //秒
                            int ms = Integer.parseInt(timedata[2]); //毫秒
                            int currTime = (m * 60 + s) * 1000 + ms * 10;
                            LyricObject item1 = new LyricObject();
                            item1.begintime = currTime;
                            item1.lrc = lrcContenet;
                            lrc_read.put(currTime, item1);// 将currTime当标签 item1当数据 插入TreeMap里
                            i++;
                        }
                    }
                }
            }
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
/*
* 遍历hashmap 计算每句歌词所需要的时间
*/
        lrc_map.clear();
        data = "";
        Iterator<Integer> iterator = lrc_read.keySet().iterator();
        LyricObject oldval = null;
        int i = 0;
        while (iterator.hasNext()) {
            Object ob = iterator.next();
            LyricObject val = (LyricObject) lrc_read.get(ob);
            if (oldval == null)
                oldval = val;
            else {
                LyricObject item1 = new LyricObject();
                item1 = oldval;
                item1.timeline = val.begintime - oldval.begintime;
                lrc_map.put(new Integer(i), item1);
                i++;
                oldval = val;
            }
            if (!iterator.hasNext()) {
                lrc_map.put(new Integer(i), val);
            }
        }
    }
    public static boolean isBlLrc() {
        return blLrc;
    }
    /**
     * @return the offsetY
     */
    public float getOffsetY() {
        return offsetY;
    }
    /**
     * @param offsetY the offsetY to set
     */
    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }
    /**
     * @return 返回歌词文字的大小
     */
    public int getSIZEWORD() {
        return SIZEWORD;
    }
    /**
     * 设置歌词文字的大小
     * @param sIZEWORD the sIZEWORD to set
     */
    public void setSIZEWORD(int sIZEWORD) {
        SIZEWORD = sIZEWORD;
    }
}