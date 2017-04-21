package com.coship.app.mediaplayer.control;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 980558 on 2017/4/20.
 */

public class FocusGridLayoutManager extends GridLayoutManager {
    private int mSpanCount;

    public FocusGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public FocusGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public FocusGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }


    @Override
    public View onInterceptFocusSearch(View focused, int direction) {
        if (direction == View.FOCUS_DOWN) {
            int pos = getPosition(focused);
            int nextPos = getNextViewPos(pos, direction);
            int size = getChildCount();
            int count = getItemCount();
            if (size > 0) {
                int startIndex = 0;
                if (size >= mSpanCount) {
                    startIndex = size - mSpanCount;
                }
                View view;
                for (int i = startIndex; i < size; i++) {
                    view = getChildAt(i);
                    if (view == focused) {
                        int lastVisibleItemPos = findLastCompletelyVisibleItemPosition();
                        if (pos > lastVisibleItemPos) { //lastVisibleItemPos==-1 ||
                            return focused;
                        } else {
                            int lastLineStartIndex = 0;
                            if (count >= mSpanCount) {
                                lastLineStartIndex = count - mSpanCount;
                            }
                            if (pos >= lastLineStartIndex && pos < count) { //最后一排的可见view时,返回当前view
                                return focused;
                            }
                            break;
                        }
                    }
                }
            } else {
                return focused;
            }
        }
        return super.onInterceptFocusSearch(focused, direction);
    }

    private int getNextViewPos(int pos, int direction) {
        if (direction == View.FOCUS_DOWN)
            return pos + mSpanCount;
        else if (direction == View.FOCUS_LEFT)
            return pos == 0 ? pos : pos - 1;
        else if (direction == View.FOCUS_UP)
            return pos < mSpanCount ? pos : pos - mSpanCount;
        else
            return pos + 1;
    }
}
