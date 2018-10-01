package cn.knero.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * This class is from the v7 samples of the Android SDK. It's not by me!
 * <p/>
 * See the license above for details.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    public static final int HORIZONTAL_LIST = 0;

    public static final int VERTICAL_LIST = 1;

    public static final int ALL_LIST = 2;

    private Drawable mDivider;

    private int mColorHeight = -1;

    private int mOrientation;

    /**
     * 利用theme中定义的分割线
     */
    public DividerItemDecoration(Context context, int orientation) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
    }

    /**
     * 传分割线图片
     */
    public DividerItemDecoration(Context context, int orientation, Drawable divider) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = divider;
        a.recycle();
        setOrientation(orientation);
    }

    /**
     * 传颜色值和线的高度
     */
    public DividerItemDecoration(Context context, int orientation, int color, int height) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = new ColorDrawable(color);
        mColorHeight = height;
        a.recycle();
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST && orientation != ALL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
        if (mOrientation == VERTICAL_LIST) {
            drawHorizontal(c, parent);
        } else if (mOrientation == HORIZONTAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
            drawVertical(c, parent);
        }

    }


    /**
     * 画横线
     *
     * @param c      canvas
     * @param parent recyclerview
     */
    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount && i < parent.getAdapter().getItemCount(); i++) {
            final View child = parent.getChildAt(i);
            if (child.getWidth() <= 0) {
                continue;
            }
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * 画竖线
     *
     * @param c      canvas
     * @param parent recyclerView
     */
    public void drawVertical(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();

        final int childCount = parent.getChildCount();
//        final View lastView = parent.getChildAt(childCount - 1);
//        final RecyclerView.LayoutParams childParams = (RecyclerView.LayoutParams) lastView
//                .getLayoutParams();
//        final int bottom = lastView.getBottom() + childParams.bottomMargin;
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if (child.getHeight() <= 0) {
                continue;
            }
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    private boolean isLastColum(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int orientation = ((GridLayoutManager) layoutManager).getOrientation();
            if (orientation == OrientationHelper.VERTICAL) {
                return (pos + 1) % spanCount == 0;
            } else {
                int lastColumnCount = childCount % spanCount;
                lastColumnCount = lastColumnCount == 0 ? spanCount : lastColumnCount;
                return pos >= childCount - lastColumnCount;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                {
                    return true;
                }
            } else {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
                    return true;
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            int orientation = ((LinearLayoutManager) layoutManager).getOrientation();
            if (orientation == OrientationHelper.VERTICAL) {
                return true;
            } else if ((pos + 1) % childCount == 0) {
                return true;
            }
        }
        return false;
    }

    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
                              int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int orientation = ((GridLayoutManager) layoutManager).getOrientation();
            if (orientation == OrientationHelper.VERTICAL) {
                int lastColumnCount = childCount % spanCount;
                lastColumnCount = lastColumnCount == 0 ? spanCount : lastColumnCount;
                return pos >= childCount - lastColumnCount;
            } else {
                return (pos + 1) % spanCount == 0;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                if (pos >= childCount)
                    return true;
            } else
            // StaggeredGridLayoutManager 且横向滚动
            {
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            int orientation = ((LinearLayoutManager) layoutManager).getOrientation();
            if (orientation == OrientationHelper.HORIZONTAL) {
                return true;
            } else if ((pos + 1) % childCount == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition,
                               RecyclerView parent) {
        int spanCount = getSpanCount(parent);
        boolean hasHeader = isOneLine(parent, itemPosition);
        int sub = hasHeader ? 1 : 0;
        if (hasHeader) {
            spanCount = 1;
        }
        int childCount = parent.getAdapter().getItemCount();
        int right = mDivider.getIntrinsicWidth();
        int bottom = mDivider.getIntrinsicHeight();
        if (isLastRaw(parent, itemPosition - sub, spanCount, childCount))// 如果是最后一行，则不需要绘制底部
        {
            bottom = 0;
        }
        if (isLastColum(parent, itemPosition - sub, spanCount, childCount))// 如果是最后一列，则不需要绘制右边
        {
            right = 0;
        }
        outRect.set(0, 0, right, bottom);
    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }

    private boolean isOneLine(RecyclerView parent, int itemPosition) {
        boolean isOneLine = false;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            isOneLine = ((GridLayoutManager) layoutManager).getSpanSizeLookup().getSpanSize(itemPosition) == getSpanCount(parent);
        }
        return isOneLine;
    }

}
