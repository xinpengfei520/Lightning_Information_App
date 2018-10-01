package cn.knero.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Knero on 8/16/15.
 */
public class RectFrameLayout extends FrameLayout {

    /**
     * 正方型的FrameLayout
     *
     * @param context context
     */
    public RectFrameLayout(Context context) {
        super(context);
    }

    /**
     * 正方型的FrameLayout
     *
     * @param context context
     * @param attrs   attrs
     */
    public RectFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 正方型的FrameLayout
     *
     * @param context      context
     * @param attrs        attrs
     * @param defStyleAttr defStyleAttr
     */
    public RectFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(width, height);
        this.setMeasuredDimension(size, size);
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChildWithMargins(child,
                        MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),
                        0,
                        MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),
                        0);
            }
        }
    }
}
