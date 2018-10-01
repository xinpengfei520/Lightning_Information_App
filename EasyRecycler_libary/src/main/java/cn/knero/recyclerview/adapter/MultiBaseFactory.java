package cn.knero.recyclerview.adapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;

import cn.knero.recyclerview.adapter.annotation.RecyclerViewHolderMap;
import cn.knero.recyclerview.adapter.annotation.RecyclerViewHolderMap.RecyclerItem;
import cn.knero.recyclerview.adapter.annotation.ViewHolderValue;

/**
 * Created by Knero on 6/22/16.
 */
public class MultiBaseFactory<D> {

    private MultiBaseRecyclerAdapter mAdapter;
    private SparseArray<Class> mViewHolders = new SparseArray<>();
    private SparseArray<Integer> mLayoutResIds = new SparseArray<>();

    public MultiBaseFactory(MultiBaseRecyclerAdapter adapter) {
        this(adapter, true);
    }

    public MultiBaseFactory(MultiBaseRecyclerAdapter adapter, boolean withAnnotation) {
        mAdapter = adapter;
        mViewHolders.clear();
        if (withAnnotation) {
            RecyclerItem[] items = null;
            RecyclerViewHolderMap annotation = mAdapter.getClass().getAnnotation(RecyclerViewHolderMap.class);
            if (annotation != null) {
                items = annotation.items();
            }
            if (items != null) {
                for (RecyclerItem item : items) {
                    mViewHolders.put(item.type(), item.className());
                    int layoutResId = item.layoutResId();
                    if (layoutResId > 0) {
                        mLayoutResIds.put(item.type(), item.layoutResId());
                    }
                }
            }
        }
    }

    public <T extends MultiBaseRecyclerViewHolder<D>> void registerViewHolder(int layoutResId) {
        registerViewHolder(null, layoutResId);
    }

    public <T extends MultiBaseRecyclerViewHolder<D>> void registerViewHolder(Class<T> viewHolderClassName, int layoutResId) {
        registerViewHolder(0, viewHolderClassName, layoutResId);
    }

    public <T extends MultiBaseRecyclerViewHolder<D>> void registerViewHolder(int type, Class<T> viewHolderClassName, int layoutResId) {
        if (viewHolderClassName == null) {
            mViewHolders.put(type, DefaultBaseRecyclerViewHolder.class);
        } else {
            mViewHolders.put(type, viewHolderClassName);
        }
        mLayoutResIds.put(type, layoutResId);
    }

    public MultiBaseRecyclerViewHolder<D> createViewHolder(int viewType, LayoutInflater inflater, ViewGroup parent) {
        Class<MultiBaseRecyclerViewHolder<D>> clazz = mViewHolders.get(viewType);
        if (clazz == null) {
            throw new IllegalArgumentException("viewholder with type#" + viewType + " not register at adapter created.");
        }
        Integer layoutResId = mLayoutResIds.get(viewType);
        if (layoutResId == null) {
            ViewHolderValue value = clazz.getAnnotation(ViewHolderValue.class);
            if (value != null) {
                layoutResId = value.layoutResId();
            }
        }
        if (layoutResId == null) {
            throw new IllegalArgumentException("can not find viewholder layout res id with type#" + viewType);
        } else {
            mLayoutResIds.put(viewType, layoutResId);
        }
        try {
            Class[] paramsType = {View.class};
            Constructor con = clazz.getConstructor(paramsType);
            View view = inflater.inflate(layoutResId, parent, false);
            Object[] params = {view};
            return (MultiBaseRecyclerViewHolder<D>) con.newInstance(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("create viewholder failed.");
        }
    }

    class RecyclerItemInfo<T extends MultiBaseRecyclerViewHolder<D>> {

        Class<T> className;
        int layoutResId;

        public RecyclerItemInfo(Class<T> className, int layoutResId) {
            this.className = className;
            this.layoutResId = layoutResId;
        }
    }
}
