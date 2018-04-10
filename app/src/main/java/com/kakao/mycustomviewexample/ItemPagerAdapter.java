package com.kakao.mycustomviewexample;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kakao.mycustomviewexample.model.Item;

import java.util.List;

/**
 * Created by khan.moon on 2018. 4. 10..
 */

public class ItemPagerAdapter extends PagerAdapter {
    private List<Item> itemList;

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View layout = LayoutInflater.from(container.getContext()).inflate(R.layout.item, container, false);

        TextView title = layout.findViewById(R.id.title);
        TextView desc = layout.findViewById(R.id.desc);

        Item item = itemList.get(position);
        title.setText(item.getTitle());
        desc.setText(item.getDesc());

        container.addView(layout);

        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return itemList != null ? itemList.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
