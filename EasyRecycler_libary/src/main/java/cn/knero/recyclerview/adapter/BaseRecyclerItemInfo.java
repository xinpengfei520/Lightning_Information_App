package cn.knero.recyclerview.adapter;

/**
 * Created by Knero on 6/22/16.
 */
public class BaseRecyclerItemInfo<T> {

    private int mType;
    private T mObject;

    public BaseRecyclerItemInfo() {
    }

    public BaseRecyclerItemInfo(int type, T object) {
        mType = type;
        mObject = object;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public T getObject() {
        return mObject;
    }

    public void setObject(T object) {
        mObject = object;
    }
}
