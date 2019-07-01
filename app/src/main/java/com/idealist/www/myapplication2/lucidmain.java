package com.idealist.www.myapplication2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idealist.www.myapplication2.dream_chatlist.dream_chatlist_adapter;
import com.idealist.www.myapplication2.dream_chatlist.dream_chatlist_item;
import com.idealist.www.myapplication2.dream_friend.dream_friend_adapter;
import com.idealist.www.myapplication2.dream_friend.dream_friend_item;
import com.idealist.www.myapplication2.dream_friend_apply.dream_friend_apply_adapter;
import com.idealist.www.myapplication2.dream_friend_apply.dream_friend_apply_item;

import java.lang.reflect.Type;
import java.util.ArrayList;

import dream_diary.dream_diary;

public class lucidmain extends AppCompatActivity implements View.OnClickListener {


    public static String id;
    TextView nickname;

    TextView dream_diary, dream_alarm, dream_post, dream_friend_apply, dream_friend, dream_chat;

    Uri uri;
    String Uri_String;

    LinearLayout Li_nickname, Li_logout;
    public static LinearLayout dream_friend_apply_layout;
    public static LinearLayout dream_friend_list_layout;
    public static LinearLayout dream_friend_chatlist_layout;
    ImageView profile;

    ListView dream_friend_apply_listview;
    ListView dream_friend_listview;
    ListView dream_chat_listview;

    public static ArrayList<dream_friend_apply_item> friend_apply_listItem = new ArrayList<>();
    public static ArrayList<dream_friend_item> friend_listItem = new ArrayList<>();
    public static ArrayList<dream_chatlist_item> dream_chat_listItem = new ArrayList<>();

    dream_friend_apply_adapter friend_apply_adapter;
    dream_friend_adapter friend_adapter;
    dream_chatlist_adapter dream_chat_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucidmain);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("메인 화면");


        nickname = findViewById(R.id.Button_nickname);
        Li_nickname = findViewById(R.id.Li_nickname);
        Li_nickname.setOnClickListener(this);

        profile = findViewById(R.id.Button_profilechange);
        profile.setOnClickListener(this);

        Li_logout = findViewById(R.id.Li_logout);
        Li_logout.setOnClickListener(this);

        dream_friend_apply_listview = findViewById(R.id.dream_friend_apply_listview);
        dream_friend_listview = findViewById(R.id.dream_friend_listview);
        dream_chat_listview = findViewById(R.id.dream_friend_chat_listview);

        dream_friend_apply_layout = findViewById(R.id.dream_friend_apply_layout);
        dream_friend_list_layout = findViewById(R.id.dream_friend_list_layout);
        dream_friend_chatlist_layout = findViewById(R.id.dream_friend_chatlist_layout);

        // 꿈친추를 FrameLayout의 기본화면으로 설정
        dream_friend_apply_layout.setVisibility(View.VISIBLE);
        dream_friend_list_layout.setVisibility(View.INVISIBLE);
        dream_friend_chatlist_layout.setVisibility(View.INVISIBLE);

        load();


        /** 로그인시 환영합니다 메시지 컨트롤*/
        if (setting.loginsuccess == false) {
            Toast.makeText(this, id + "님 환영합니다!", Toast.LENGTH_SHORT).show();
        }
        setting.loginsuccess = true;


        dream_diary = findViewById(R.id.dream_diary);
        dream_alarm = findViewById(R.id.dream_alarm);
        dream_post = findViewById(R.id.dream_post);
        dream_friend_apply = findViewById(R.id.dream_friend_apply);
        dream_friend = findViewById(R.id.dream_friend);
        dream_chat = findViewById(R.id.dream_chat);
        dream_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(lucidmain.this, "해당 기능을 준비중입니다.", Toast.LENGTH_SHORT).show();
//                Intent myintent = new Intent(lucidmain.this, dream_diary.class);
//                startActivity(myintent);
            }
        });

        dream_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(lucidmain.this, "해당 기능을 준비중입니다 :)", Toast.LENGTH_SHORT).show();
//                Intent myintent = new Intent(lucidmain.this, dream_alarm.class);
//                startActivity(myintent);
            }
        });

        dream_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(lucidmain.this, com.idealist.www.myapplication2.dream_post.dream_post.class);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                startActivity(myintent);
            }
        });

        dream_friend_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dream_friend_apply_layout.setVisibility(View.VISIBLE);
                dream_friend_list_layout.setVisibility(View.INVISIBLE);
                dream_friend_chatlist_layout.setVisibility(View.INVISIBLE);


            }
        });


        dream_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 친구목록 로드부분
                // 저장하기위해서는 기존 목록을 불러와야한다.
                SharedPreferences share = getSharedPreferences(lucidmain.id, MODE_PRIVATE);  // 친구하고 싶은사람 id에
                Gson gsons = new Gson();
                String jsons = share.getString("friend_list", null);
                Type types = new TypeToken<ArrayList<dream_friend_item>>() {
                }.getType();
                friend_listItem = gsons.fromJson(jsons, types);
                if (friend_listItem == null) {
                    friend_listItem = new ArrayList<>();
                }
                friend_adapter = new dream_friend_adapter(lucidmain.this, friend_listItem);
                dream_friend_listview.setAdapter(friend_adapter);


                dream_friend_apply_layout.setVisibility(View.INVISIBLE);
                dream_friend_list_layout.setVisibility(View.VISIBLE);
                dream_friend_chatlist_layout.setVisibility(View.INVISIBLE);

            }
        });

        dream_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dream_friend_apply_layout.setVisibility(View.INVISIBLE);
                dream_friend_list_layout.setVisibility(View.INVISIBLE);
                dream_friend_chatlist_layout.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.Li_nickname) {
            Intent myIntent = new Intent(this, privateinformation.class);
            startActivity(myIntent);
            overridePendingTransition(R.anim.anim_slide_in_top, R.anim.anim_slide_out_bottom);
            //개인정보보기
        }

        if (view.getId() == R.id.Li_logout) {
            show();
            // 로그아웃하기(어플종료)
        }


        if (view.getId() == R.id.Button_profilechange) {

            Intent myintent = new Intent(this, profileinfo.class);
            startActivity(myintent);
            overridePendingTransition(R.anim.anim_slide_in_top, R.anim.anim_slide_out_bottom);
        }


    }


    @Override
    protected void onResume() {
        super.onResume();

        load();

        friend_apply_adapter = new dream_friend_apply_adapter(this, friend_apply_listItem);
        dream_friend_apply_listview.setAdapter(friend_apply_adapter);

        friend_adapter = new dream_friend_adapter(this, friend_listItem);
        dream_friend_listview.setAdapter(friend_adapter);

        dream_chat_adapter = new dream_chatlist_adapter(this, dream_chat_listItem);
        dream_chat_listview.setAdapter(dream_chat_adapter);


    }


    private void load() {

        SharedPreferences sharedPref = getSharedPreferences("Operation", MODE_PRIVATE);
        id = sharedPref.getString("Operation", null);
        nickname.setText(id, null);

        SharedPreferences sharedPreferences = getSharedPreferences(id, MODE_PRIVATE); // 로그인한 id 저장소를 불러오기 load, 이 때 불려와지는 friend list는 각각 다름
        if (sharedPreferences.getString("profilephoto", null) != null) {
            String savefile = sharedPreferences.getString("profilephoto", null);
            Uri_String = savefile;
            uri = Uri.parse(Uri_String);
            Glide.with(this).load(uri).centerCrop().into(profile);
        }


        SharedPreferences sharedPreference = getSharedPreferences(id, MODE_PRIVATE); // 친구추가 신청한 사람들 요청 불러오기 load
        Gson gson = new Gson();
        String json = sharedPreference.getString("friend_apply_list", null);
        Type type = new TypeToken<ArrayList<dream_friend_apply_item>>() {
        }.getType();
        friend_apply_listItem = gson.fromJson(json, type);
        if (friend_apply_listItem == null) {
            friend_apply_listItem = new ArrayList<>();
        }

        // 친구목록 로드부분
        // 저장하기위해서는 기존 목록을 불러와야한다.
        SharedPreferences share = getSharedPreferences(lucidmain.id, MODE_PRIVATE);  // 친구하고 싶은사람 id에
        Gson gsons = new Gson();
        String jsons = share.getString("friend_list", null);
        Type types = new TypeToken<ArrayList<dream_friend_item>>() {
        }.getType();
        friend_listItem = gsons.fromJson(jsons, types);
        if (friend_listItem == null) {
            friend_listItem = new ArrayList<>();
        }

        // 채팅목록 로드부분
        // 저장하기위해서는 기존 목록을 불러와야한다.
        SharedPreferences share_chatlist = getSharedPreferences(lucidmain.id, MODE_PRIVATE);  // 친구하고 싶은사람 id에
        Gson gson_chatlist = new Gson();
        String json_chatlist = share_chatlist.getString("chat_list", null);
        Type type_chatlist = new TypeToken<ArrayList<dream_chatlist_item>>() {
        }.getType();
        dream_chat_listItem = gson_chatlist.fromJson(json_chatlist, type_chatlist);
        if (dream_chat_listItem == null) {
            dream_chat_listItem = new ArrayList<>();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }


    void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("정말 로그아웃 하시겠습니까?");
        builder.setMessage("로그아웃시 자동으로 정보가 저장됩니다.");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        onBackPressed();
                        finish();
                        setting.loginsuccess = false;
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }
}
