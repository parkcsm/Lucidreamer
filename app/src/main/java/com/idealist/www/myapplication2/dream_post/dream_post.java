package com.idealist.www.myapplication2.dream_post;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idealist.www.myapplication2.R;
import com.idealist.www.myapplication2.dream_friend_apply.dream_friend_apply_adapter;
import com.idealist.www.myapplication2.dream_friend_apply.dream_friend_apply_item;
import com.idealist.www.myapplication2.lucidmain;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class dream_post extends AppCompatActivity {

    static String id;
    public static ListView dream_post_listview;

    public static ArrayList<dream_post_item> listItem = new ArrayList<>();
    public static ArrayList<dream_friend_apply_item> listItem2 = new ArrayList<>();

    dream_post_adapter adapter;
    dream_friend_apply_adapter adapter2;
    ImageView fab_post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dream_post);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("꿈 게시판");


        SharedPreferences sharepref = getSharedPreferences("Operation", MODE_PRIVATE);
        id = sharepref.getString("Operation", null);

        dream_post_listview = findViewById(R.id.dream_post_listview);

        fab_post = findViewById(R.id.fab_post);


        fab_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScreenToPost();
            }
        });

        Toast.makeText(this, "접속한 ID : " + lucidmain.id, Toast.LENGTH_LONG).show();

    }


    private void ScreenToPost() {
        Intent myintent = new Intent(this, dream_post_post.class);
        startActivity(myintent);
        overridePendingTransition(R.anim.anim_slide_in_top, R.anim.anim_slide_out_bottom);
    }



    @Override
    protected void onResume() {
        super.onResume();
        load();
        adapter = new dream_post_adapter(this, listItem);
        dream_post_listview.setAdapter(adapter);

    }


    private void load() {
        SharedPreferences sharedPreferences = getSharedPreferences("dream_post", MODE_PRIVATE); // 로그인한 id 저장소를 불러오기, 이 때 불려와지는 friend list는 각각 다름
        Gson gson = new Gson();
        String json = sharedPreferences.getString("postlist", null);
        Type type = new TypeToken<ArrayList<dream_post_item>>() {
        }.getType();
        listItem = gson.fromJson(json, type);
        if (listItem == null) {
            listItem = new ArrayList<>();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }
}

