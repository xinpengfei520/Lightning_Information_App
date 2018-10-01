package cn.knero.recyclerview.adapter;

import android.view.View;

/**
 * Created by Knero on 6/23/16.
 */
public class DefaultBaseRecyclerViewHolder extends MultiBaseRecyclerViewHolder<Object> {

    /**
     * @param itemView itemView
     */
    public DefaultBaseRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void convert(Object o, int position) {

    }
}
