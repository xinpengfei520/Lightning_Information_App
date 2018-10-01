package cn.knero.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.knero.recyclerview.DividerItemDecoration;
import cn.knero.recyclerview.RecyclerViewHelper;
import cn.knero.recyclerview.RecyclerViewHelper.OnClickListener;
import cn.knero.recyclerview.RecyclerViewHelper.OnLongClickListener;
import cn.knero.recyclerview.adapter.BaseRecyclerItemInfo;
import cn.knero.recyclerview.adapter.MultiBaseRecyclerAdapter;
import cn.knero.recyclerview.adapter.annotation.RecyclerViewHolderMap;
import cn.knero.recyclerview.adapter.annotation.RecyclerViewHolderMap.RecyclerItem;

public class MainActivity extends AppCompatActivity {

    private RecyclerViewHelper mRecyclerViewHelper;
    private List<BaseRecyclerItemInfo> mDatas;
    MultiBaseRecyclerAdapter adapter;

    protected void initData() {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                mDatas.add(new BaseRecyclerItemInfo(2, new OtherItem("other item:" + i)));
            }
            mDatas.add(new BaseRecyclerItemInfo(1, new TestItem("item" + i)));

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        adapter = new TestItemAdatper(this);
        mRecyclerViewHelper = new RecyclerViewHelper.Builder()
                .setRecyclerView(this, R.id.recycler_view)
                .setColumnsNum(2)
                .setOrientation(OrientationHelper.VERTICAL)
                .setAdapter(adapter)
                .setItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.ALL_LIST, getResources().getDrawable(R.drawable.custom_split_item)))
                .build(this);
        adapter.updateOriginalDatas(mDatas);
        mRecyclerViewHelper.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(ViewHolder viewHolder) {
                Log.d("TAG", "" + viewHolder.getAdapterPosition());
            }
        });
        mRecyclerViewHelper.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public void onLongClick(ViewHolder viewHolder) {
                Log.d("TAG", "onLongClick:" + viewHolder.getAdapterPosition());
            }
        });
    }

    @RecyclerViewHolderMap(items = {
            @RecyclerItem(layoutResId = R.layout.item_layout, type = 1, className = DefaultViewHolder.class),
            @RecyclerItem(layoutResId = R.layout.two_layout, type = 2, className = TwoViewHolder.class)
    })
    public static class TestItemAdatper extends MultiBaseRecyclerAdapter {

        public TestItemAdatper(Context context) {
            super(context);
        }
    }

}
