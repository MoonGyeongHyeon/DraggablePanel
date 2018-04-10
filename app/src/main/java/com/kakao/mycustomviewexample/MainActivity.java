package com.kakao.mycustomviewexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.kakao.mycustomviewexample.model.Item;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final DraggablePanel draggablePanel = findViewById(R.id.draggable_panel);
        draggablePanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", "click!");
                draggablePanel.render(createDummyData());
            }
        });
//        dㅌraggablePanel.setLocationReverted(true);
    }

    private List<Item> createDummyData() {
        List<Item> itemList = new ArrayList<>();

        Item item = new Item();
        item.setTitle("1번 아이템 제목입니~");
        item.setDesc("1번 아이템 설명입니다~");
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
