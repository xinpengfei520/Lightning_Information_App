package cn.knero.demo;

import android.view.View;

import cn.knero.recyclerview.adapter.MultiBaseRecyclerViewHolder;

/**
 * Created by Knero on 24/10/2016.
 */

public class DefaultViewHolder extends MultiBaseRecyclerViewHolder<TestItem> {

    /**
     * @param itemView itemView
     */
    public DefaultViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void convert(TestItem testItem, int position) {
        this.setText(R.id.show_text, testItem.getValue());
    }

    @Override
    public void onSelectedChanged(boolean selected) {
        super.onSelectedChanged(selected);
        if (selected) {
            getItemView().setBackgroundResource(R.color.colorPrimaryDark);
        } else {
            getItemView().setBackgroundResource(R.drawable.clickable_background);
        }
    }
}
