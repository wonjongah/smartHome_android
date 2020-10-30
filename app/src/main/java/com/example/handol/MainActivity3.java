package com.example.handol;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.tabs.TabLayout;

public class MainActivity3 extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout) ;

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // tab 상태가 선택 상태로 변경
                int position = tab.getPosition();
                // changeView(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // tab 상태가 선택되지 않음으로 변경
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // 이미 선택된 tab이 다시 선택됨
            }
        });
    }


}
