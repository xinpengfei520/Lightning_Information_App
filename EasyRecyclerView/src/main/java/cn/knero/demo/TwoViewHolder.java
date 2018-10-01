package cn.knero.demo;

import android.view.View;

import cn.knero.recyclerview.adapter.MultiBaseRecyclerViewHolder;

/**
 * Created by Knero on 24/10/2016.
 */

public class TwoViewHolder extends MultiBaseRecyclerViewHolder<OtherItem> {

    /**
     * @param itemView itemView
     */
    public TwoViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void convert(OtherItem otherItem, int position) {
        this.setText(R.id.show_text, otherItem.getValue());
    }
}
