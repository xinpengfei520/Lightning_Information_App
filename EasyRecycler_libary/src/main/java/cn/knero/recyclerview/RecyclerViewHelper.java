package cn.knero.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.OnItemTouchListener;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;

import cn.knero.recyclerview.listener.ItemTouchHelperGestureListener;

/**
 * Created by Knero on 14/10/2016.
 */

public class RecyclerViewHelper {

    private OnClickListener mOnClickListener = null;
    private OnLongClickListener mOnLongClickListener = null;
    private Adapter mAdapter;
    private Builder mBuilder;
    private RecyclerView mRecyclerView;
    private EasySimpleItemCallback mCallback;

    private RecyclerViewHelper(Builder builder) {
        mBuilder = builder;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        mOnLongClickListener = onLongClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setMoveable(boolean moveable) {
        mCallback.setMoveable(moveable);
    }

    private void apply(Context context) {
        mRecyclerView = mBuilder.mRecyclerView;
        if (mBuilder.mLayoutManager == null) {
            if (mBuilder.mColumsNum <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context, mBuilder.mOrientation, mBuilder.mReverseLayout));
            } else {
                GridLayoutManager layoutManager = new GridLayoutManager(context, mBuilder.mColumsNum, mBuilder.mOrientation, false);
                mRecyclerView.setLayoutManager(layoutManager);
            }
        } else {
            mRecyclerView.setLayoutManager(mBuilder.mLayoutManager);
        }
        if (mBuilder.isUseDefaultDecoration) {
            mBuilder.mItemDecoration = mBuilder.getDefaultDecoration(context);
        }
        if (mBuilder.mItemDecoration != null) {
            mRecyclerView.addItemDecoration(mBuilder.mItemDecoration);
        }
        mOnClickListener = mBuilder.mOnClickListener;
        mOnLongClickListener = mBuilder.mOnLongClickListener;
        final GestureDetectorCompat gestureDetector = new GestureDetectorCompat(context, new ItemTouchHelperGestureListener(mRecyclerView) {
            @Override
            public void onItemClick(ViewHolder viewHolder) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(viewHolder);
                }
            }

            @Override
            public void onLongClick(ViewHolder viewHolder) {
                if (mOnLongClickListener != null) {
                    mOnLongClickListener.onLongClick(viewHolder);
                }
            }
        });
        mRecyclerView.addOnItemTouchListener(new OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                gestureDetector.onTouchEvent(e);
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                gestureDetector.onTouchEvent(e);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        mAdapter = mBuilder.mAdapter;
        if (mAdapter != null) {
            mRecyclerView.setAdapter(mAdapter);
        }
        mCallback = new EasySimpleItemCallback();
        mCallback.setMoveable(mBuilder.mMovable);
        ItemTouchHelper touchHelper = new ItemTouchHelper(mCallback);
        touchHelper.attachToRecyclerView(mRecyclerView);
    }


    public static class Builder {

        private int mRecyclerViewResId = -1;
        private RecyclerView mRecyclerView;

        // 设置RecyclerView布局参数
        private int mColumsNum = -1;
        private int mOrientation = OrientationHelper.VERTICAL;
        private boolean mReverseLayout = false;
        private LayoutManager mLayoutManager = null;

        // 分割线
        private boolean isUseDefaultDecoration = false;
        private ItemDecoration mItemDecoration = null;

        // 点击事件回调
        private OnClickListener mOnClickListener = null;
        private OnLongClickListener mOnLongClickListener = null;

        // Adapter
        private Adapter mAdapter;

        private boolean mMovable = false;// 默认不可拖动

        public Builder setRecyclerView(Activity activity, int resId) {
            mRecyclerView = (RecyclerView) activity.findViewById(resId);
            return this;
        }

        public Builder setRecyclerView(View rootView, int resId) {
            mRecyclerView = (RecyclerView) rootView.findViewById(resId);
            return this;
        }

        public Builder setRecyclerView(RecyclerView recyclerView) {
            mRecyclerView = recyclerView;
            return this;
        }

        public Builder setColumnsNum(int columnsNum) {
            mColumsNum = columnsNum;
            return this;
        }

        public Builder setOrientation(int orientation) {
            mOrientation = orientation;
            return this;
        }

        public Builder setReverseLayout(boolean reverseLayout) {
            mReverseLayout = reverseLayout;
            return this;
        }

        public Builder setLayoutManager(LayoutManager layoutManager) {
            mLayoutManager = layoutManager;
            return this;
        }

        public Builder useDefaultDecoration() {
            this.isUseDefaultDecoration = true;
            return this;
        }

        public Builder setItemDecoration(ItemDecoration itemDecoration) {
            mItemDecoration = itemDecoration;
            return this;
        }

        public Builder setOnClickListener(OnClickListener clickListener) {
            this.mOnClickListener = clickListener;
            return this;
        }

        public Builder setOnLongClickListener(OnLongClickListener longClickListener) {
            this.mOnLongClickListener = longClickListener;
            return this;
        }

        public Builder setAdapter(Adapter adapter) {
            this.mAdapter = adapter;
            return this;
        }

        /**
         * @return 返回decoration
         */
        private ItemDecoration getDefaultDecoration(Context context) {
            DividerItemDecoration decoration = new DividerItemDecoration(context, DividerItemDecoration.ALL_LIST);
            return decoration;
        }

        public RecyclerViewHelper build(Context context) {
            RecyclerViewHelper recyclerViewHelper = new RecyclerViewHelper(this);
            recyclerViewHelper.apply(context);
            return recyclerViewHelper;
        }

        public Builder movable() {
            mMovable = true;
            return this;
        }
    }

    public interface OnClickListener {

        public void onClick(ViewHolder viewHolder);
    }

    public interface OnLongClickListener {

        public void onLongClick(ViewHolder viewHolder);
    }
}
