package com.coship.app.mediaplayer.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by 980558 on 2017/4/20.
 */

public class ImageRecycleView extends android.support.v7.widget.AppCompatImageView {
    private Bitmap bitmap;

    public ImageRecycleView(Context context) {
        super(context);
    }

    public ImageRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageRecycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        this.bitmap = bm;
        super.setImageBitmap(bm);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

}
