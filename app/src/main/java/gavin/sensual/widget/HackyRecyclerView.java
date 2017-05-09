package gavin.sensual.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HackyRecyclerView extends RecyclerView {

    public HackyRecyclerView(Context context) {
        super(context);
    }

    public HackyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HackyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}