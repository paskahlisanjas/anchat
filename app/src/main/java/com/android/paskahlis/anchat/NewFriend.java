package com.android.paskahlis.anchat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.paskahlis.anchat.adapter.AddFriendMethodsAdapter;

public class NewFriend extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
        ListView methodList = (ListView) findViewById(R.id.method_list_view);
        int[] icons = { R.drawable.ic_shake, R.drawable.ic_location, R.drawable.ic_search};
        String[] labels = {"Shake for friends", "Look for the closest one", "Find by id"};
        methodList.setAdapter(new AddFriendMethodsAdapter(this, labels, icons));
        methodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0 :
                        startActivity(new Intent(getApplicationContext(), ShakeFriendActivity.class));
                        break;
                    case 1 :

                        break;
                    case 2 :
                        break;
                }
            }
        });
    }
}