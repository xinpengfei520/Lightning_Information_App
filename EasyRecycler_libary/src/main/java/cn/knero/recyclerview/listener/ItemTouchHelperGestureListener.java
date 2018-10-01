package cn.knero.recyclerview.listener;

import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Knero on 5/17/16.
 */
public abstract class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {

    private RecyclerView mRecyclerView;

    public ItemTouchHelperGestureListener(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (mRecyclerView == null) {
            return super.onSingleTapUp(e);
        } else {
            View childView = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (childView != null) {
                RecyclerView.ViewHolder viewHolder = mRecyclerView.getChildViewHolder(childView);
                onItemClick(viewHolder);
            }
            return true;
        }
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (mRecyclerView == null) {
            super.onLongPress(e);
        } else {
            View childView = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (childView != null) {
                RecyclerView.ViewHolder viewHolder = mRecyclerView.getChildViewHolder(childView);
                onLongClick(viewHolder);
            }
        }
    }

    public abstract void onItemClick(RecyclerView.ViewHolder viewHolder);

    public abstract void onLongClick(RecyclerView.ViewHolder viewHolder);
}
