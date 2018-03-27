package com.kakao.mycustomviewexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final DraggablePanel draggablePanel = findViewById(R.id.draggable_panel);
        draggablePanel.setSwitchOnBackgroundResId(R.drawable.img_switchon);
        draggablePanel.setSwitchOffBackgroundResId(R.drawable.img_switchoff);
        draggablePanel.setOnSwitchListener(new OnSwitchListener() {
            @Override
            public void onSwitch(DraggablePanel panel) {
            }
        });
        draggablePanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "LOG!");
            }
        });
//        draggablePanel.setLocationReverted(true);
    }
}
