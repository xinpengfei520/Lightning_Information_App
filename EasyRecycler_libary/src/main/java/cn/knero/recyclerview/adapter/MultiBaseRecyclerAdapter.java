package cn.knero.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Knero on 6/22/16.
 */
public class MultiBaseRecyclerAdapter<T> extends RecyclerView.Adapter<MultiBaseRecyclerViewHolder<T>> {

    protected List<BaseRecyclerItemInfo<T>> mDatas;

    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    private MultiBaseFactory mBaseViewHolderFactory;

    public MultiBaseRecyclerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mDatas = new ArrayList<>();
        mBaseViewHolderFactory = new MultiBaseFactory<BaseRecyclerItemInfo>(this);
    }

    public void registerViewHolder(int layoutResId) {
        registerViewHolder(null, layoutResId);
    }

    public <SUB extends MultiBaseRecyclerViewHolder<T>> void registerViewHolder(Class<SUB> viewHolderClassName, int layoutResId) {
        registerViewHolder(0, viewHolderClassName, layoutResId);
    }

    public <SUB extends MultiBaseRecyclerViewHolder<T>> void registerViewHolder(int type, Class<SUB> viewHolderClassName, int layoutResId) {
        mBaseViewHolderFactory.registerViewHolder(type, viewHolderClassName, layoutResId);
    }

    @Override
    public MultiBaseRecyclerViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        return mBaseViewHolderFactory.createViewHolder(viewType, LayoutInflater.from(mContext), parent);
    }


    @Override
    public void onBindViewHolder(MultiBaseRecyclerViewHolder<T> holder, int position) {
        if (holder != null) {
            holder.convert(getData(position), position);
        }
        convert(holder, getData(position), position);
    }

    public void convert(MultiBaseRecyclerViewHolder<T> holder, T item, int position) {

    }

    /**
     * 获取某个Position的数据
     *
     * @param position 所在下标
     * @return 返回数据
     */
    public T getData(int position) {
        BaseRecyclerItemInfo<T> itemInfo = mDatas.get(position);
        return itemInfo == null ? null : itemInfo.getObject();
    }

    /**
     * 添加一个数据项
     *
     * @param data data
     */
    public void addItem(int type, Object data) {
        if (data != null) {
            mDatas.add(new BaseRecyclerItemInfo(type, data));
            notifyItemInserted(mDatas.size() - 1);
        }
    }

    /**
     * 添加数据
     *
     * @param type  数据type
     * @param datas 要添加的数据据
     */
    public <T extends Object> void addItems(int type, Collection<T> datas) {
        if (datas != null) {
            for (Object data : datas) {
                addItem(type, data);
            }
            notifyDataSetChanged();
        }
    }

    /**
     * 移动Item 的位置
     *
     * @param fromPosition 从这个位置开始移动
     * @param toPosition   移动到的位置
     */
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mDatas, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * 删除 Item
     *
     * @param position item 所在位置
     */
    public void onItemRemove(int position) {
        if (position >= mDatas.size()) {
            return;
        }
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 添加数据
     *
     * @param datas 要添加的数据据
     */
    public void addItems(Collection<Object> datas) {
        addItems(0, datas);
    }


    /**
     * 更新数据
     *
     * @param datas 更新后的数据
     */
    public <T extends Object> void updateItems(Collection<T> datas) {
        mDatas.clear();
        addItems(0, datas);
    }

    /**
     * 更新最原始数据结构
     *
     * @param datas 数据内容
     */
    public void updateOriginalDatas(List<BaseRecyclerItemInfo<T>> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

}
