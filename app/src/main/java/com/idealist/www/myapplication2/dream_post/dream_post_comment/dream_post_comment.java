package com.idealist.www.myapplication2.dream_post.dream_post_comment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idealist.www.myapplication2.R;
import com.idealist.www.myapplication2.dream_post.dream_post_item;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class dream_post_comment extends AppCompatActivity {

    static int position;
    static int commentCount;
    static int likecount;
    ImageView cv_iv;
    TextView cv_id, cv_tv;
    EditText cv_cm;

    ImageView btn_cm;
    dream_post_comment_adapter comment_adapter;
    ListView comment_listview;
    public static ArrayList<dream_post_item> listitems;
    static ArrayList<dream_post_comment_item> listItem = new ArrayList<>();

    Uri uri;
    String id;

    static String image_string;
    public static String writer;
    public static String text;
    public static String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dream_post_comment);


        SharedPreferences sharepref = getSharedPreferences("Operation", MODE_PRIVATE);
        id = sharepref.getString("Operation", null);

        Intent myintent = getIntent();

        image_string = myintent.getStringExtra("IMAGE");
        writer = myintent.getStringExtra("ID");
        text = myintent.getStringExtra("TEXT");
        position = myintent.getIntExtra("POSITION",0);
        likecount = myintent.getIntExtra("LIKECOUNT",0);
        time = myintent.getStringExtra("TIME");

        cv_iv = findViewById(R.id.cv_iv);
        cv_id = findViewById(R.id.cv_id);
        cv_tv = findViewById(R.id.cv_tv);
        cv_cm = findViewById(R.id.cv_cm);

        uri = Uri.parse(image_string);
        Glide.with(this).load(uri).centerCrop().into(cv_iv);
        cv_id.setText(writer);
        cv_tv.setText(text);

        btn_cm = findViewById(R.id.btn_cm);
        btn_cm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });

        comment_listview = findViewById(R.id.comment_listview);

    }


    private void add() {

        if (cv_cm.getText().toString().length() < 5) {

            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
            cv_cm.startAnimation(shake);
            Toast.makeText(this, "댓글을 5자이상 입력해주세요!", Toast.LENGTH_SHORT).show();

        } else {

            dream_post_comment_item item = new dream_post_comment_item(id, cv_cm.getText().toString(),time);
            listItem.add(0, item);
            comment_number();
            comment_adapter = new dream_post_comment_adapter(this, listItem);
            comment_listview.setAdapter(comment_adapter);
            cv_cm.setText("");

            //SAVE
            SharedPreferences sharedPreferences = getSharedPreferences("dream_post_comment", MODE_PRIVATE); // 로그인한 id 저장소를 불러오기, 이 때 불려와지는 friend list는 각각 다름
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(listItem);
            editor.putString(writer + text+time, json); //postnumber로 구분을 해준다!
            editor.commit();

            Toast.makeText(this, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        load();


        comment_adapter = new dream_post_comment_adapter(this, listItem);
        comment_listview.setAdapter(comment_adapter);
    }

    private void load() {
        SharedPreferences sharedPreferences = getSharedPreferences("dream_post_comment", MODE_PRIVATE); // 로그인한 id 저장소를 불러오기, 이 때 불려와지는 friend list는 각각 다름
        Gson gson = new Gson();
        String json = sharedPreferences.getString(writer + text + time, null);
        Type type = new TypeToken<ArrayList<dream_post_comment_item>>() {
        }.getType();
        listItem = gson.fromJson(json, type);
        if (listItem == null) {
            listItem = new ArrayList<>();
        }


        SharedPreferences sharedPref = getSharedPreferences("dream_post", MODE_PRIVATE); // 로그인한 id 저장소를 불러오기, 이 때 불려와지는 friend list는 각각 다름
        Gson gsons = new Gson();
        String jsons = sharedPref.getString("postlist", null);
        Type types = new TypeToken<ArrayList<dream_post_item>>() {
        }.getType();
        listitems = gsons.fromJson(jsons, types);
        if (listitems == null) {
            listitems = new ArrayList<>();
        }


    }




     public void comment_number() {

        if (listItem.size() >= 1000) {
            commentCount = 999;
        } else {
            commentCount = listItem.size();
        }


        dream_post_item items = new dream_post_item(image_string, writer, text, false, likecount, commentCount,time);
        listitems.remove(position);
        listitems.add(position, items);

        //SAVE
        SharedPreferences sharedPre = getSharedPreferences("dream_post", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPre.edit();
        Gson gsons = new Gson();
        String jsons = gsons.toJson(listitems);
        editor.putString("postlist", jsons);
        editor.commit();

    }

}
