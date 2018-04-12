package com.kakao.mycustomviewexample;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.kakao.mycustomviewexample.model.Item;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DraggablePanel draggablePanel = findViewById(R.id.draggable_panel);
        draggablePanel.render(createDummyData());
        draggablePanel.setLocationReverted(true);
    }

    private List<Item> createDummyData() {
        List<Item> itemList = new ArrayList<>();

        Item item = new Item();
        item.setTitle("1번 아이템 제목입니~");
        item.setDesc("3번 아이템 설명입니다~");
        itemList.add(item);

        item = new Item();
        item.setTitle("2번 아이템 제목입니~");
        item.setDesc("2번 아이템 설명입니다~sdkljfklsdjflkjwelkfjlksjdlfkjwlekjflksjlkfjkljdkljslkfjkldsjfkl");
        itemList.add(item);

        item = new Item();
        item.setTitle("3번 아이템 제목입니~");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            builder.append("adslkjaslkdalskdklasjkldjklsadjkladslkjaslkdalskdklasjkldjklsadjkl");
        }
        item.setDesc(builder.toString());
        itemList.add(item);

        item = new Item();
        item.setTitle("4번 아이템 제목입니~");
        item.setDesc("4번 아이템 설명입니다~");
        itemList.add(item);

        item = new Item();
        item.setTitle("2번 아이템 제목입니~");
        item.setDesc("2번 아이템 설명입니다~");
        itemList.add(item);

        item = new Item();
        item.setTitle("3번 아이템 제목입니~");
        item.setDesc("3번 아이템 설명입니다~");
        itemList.add(item);

        item = new Item();
        item.setTitle("4번 아이템 제목입니~");
        item.setDesc("4번 아이템 설명입니다~");
        itemList.add(item);

        item = new Item();
        item.setTitle("2번 아이템 제목입니~");
        item.setDesc("2번 아이템 설명입니다~");
        itemList.add(item);

        item = new Item();
        item.setTitle("3번 아이템 제목입니~");
        item.setDesc("3번 아이템 설명입니다~");
        itemList.add(item);

        item = new Item();
        item.setTitle("4번 아이템 제목입니~");
        item.setDesc("4번 아이템 설명입니다~");
        itemList.add(item);

        item = new Item();
        item.setTitle("2번 아이템 제목입니~");
        item.setDesc("2번 아이템 설명입니다~");
        itemList.add(item);

        item = new Item();
        item.setTitle("3번 아이템 제목입니~");
        item.setDesc("3번 아이템 설명입니다~");
        itemList.add(item);

        item = new Item();
        item.setTitle("4번 아이템 제목입니~");
        item.setDesc("4번 아이템 설명입니다~");
        itemList.add(item);



        return itemList;
    }
}
