package cn.knero.recyclerview.adapter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.knero.recyclerview.adapter.DefaultBaseRecyclerViewHolder;

/**
 * Created by Knero on 6/22/16.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RecyclerViewHolderMap {

    public RecyclerItem[] items();

    @interface RecyclerItem {

        public int type() default 0;

        public Class<?> className() default DefaultBaseRecyclerViewHolder.class;

        public int layoutResId() default -1;
    }
}
