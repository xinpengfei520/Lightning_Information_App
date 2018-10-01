package com.atguigu.guoqingjie_app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.guoqingjie_app.R;
import com.atguigu.guoqingjie_app.bean.Data;
import com.atguigu.guoqingjie_app.bean.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.atguigu.guoqingjie_app.R.id.tv_item_date;
import static com.atguigu.guoqingjie_app.R.id.tv_item_title;
import static com.atguigu.guoqingjie_app.R.id.tv_item_top;

/**
 * Created by xpf on 2016/11/20 :)
 * Wechat:18091383534
 * Function:显示闻数据的适配器
 */

public class NewsDetailsAdapter extends BaseAdapter {

    private Context mContext;
    private List<Data> list;
    private ImageLoader imageLoader;

    public NewsDetailsAdapter(Context context, List<Data> list) {
        mContext = context;
        this.list = list;
//        imageLoader = new ImageLoader(mContext, R.drawable.loading, R.drawable.error);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_news, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Data data = list.get(position);

        holder.tvItemTitle.setText(data.getTitle());
        holder.tvItemTop.setText(data.getType());
        holder.tvItemDate.setText(data.getDate());

        // 加载图片
        imageLoader = new ImageLoader(mContext, R.drawable.loading, R.drawable.error);
        imageLoader.loadImage(data.getThumbnail_pic_s(), holder.ivItemPic);

        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.iv_item_pic)
        ImageView ivItemPic;
        @Bind(tv_item_title)
        TextView tvItemTitle;
        @Bind(tv_item_top)
        TextView tvItemTop;
        @Bind(tv_item_date)
        TextView tvItemDate;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
