package com.atguigu.guoqingjie_app.bean;

import java.util.List;

/**
 * Created by xinpengfei on 2016/10/5.
 * <p>
 * Function :
 */
public class Result {

    private String stat;

    private List<Data> data;

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getStat() {
        return this.stat;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public List<Data> getData() {
        return this.data;
    }
}
