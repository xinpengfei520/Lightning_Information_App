package com.atguigu.guoqingjie_app.domain;

import java.util.List;

/**
 * Created by xinpengfei on 2016/10/7.
 * <p>
 * Function :
 */

public class Root {
    private List<Trailers> trailers;

    public void setTrailers(List<Trailers> trailers) {
        this.trailers = trailers;
    }

    public List<Trailers> getTrailers() {
        return this.trailers;
    }
}
