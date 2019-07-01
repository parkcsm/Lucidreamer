package com.idealist.www.myapplication2.dream_chatroom;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idealist.www.myapplication2.R;
import com.idealist.www.myapplication2.dream_chatlist.dream_chatlist_item;
import com.idealist.www.myapplication2.dream_friend.dream_friend_item;
import com.idealist.www.myapplication2.dream_friend_apply.dream_friend_apply_adapter;
import com.idealist.www.myapplication2.lucidmain;
import com.idealist.www.myapplication2.privateinformation;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by 박종원 on 2018-04-22.
 */

public class dream_chatting extends AppCompatActivity {

    String friend_id;
    int position;
    String friend_profile_photo;
    String chat_room_subject;
    String message;
    Button chat_message_send;
    EditText chat_message_edit;

    dream_chatroom_adapter dream_chatroom_adapter;
    ListView dream_chatroom_listview;

    public ArrayList<dream_chatroom_item> chatroom_listItem;
    public ArrayList<dream_chatlist_item> chatlist_listItem;
    public ArrayList<dream_chatlist_item> chatlist_listItem2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dream_chatting);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("꿈 채팅방");



        dream_chatroom_listview = findViewById(R.id.chatroom_listview);
        Intent myintent = getIntent();
        friend_id = myintent.getStringExtra("FRIENDID");

        chat_message_send = findViewById(R.id.chat_message_send);
        chat_message_edit = findViewById(R.id.chat_message_edit);
        chat_message_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //SAVE
                //내 채팅방에 메시지 저장
                message = chat_message_edit.getText().toString();
                dream_chatroom_item chatroom_item = new dream_chatroom_item(lucidmain.id, gettime(), message);
                chatroom_listItem.add(chatroom_listItem.size() + 0, chatroom_item);
                SharedPreferences share_message = getSharedPreferences(lucidmain.id, MODE_PRIVATE);
                SharedPreferences.Editor edit0 = share_message.edit();
                Gson gson0 = new Gson();
                String json0 = gson0.toJson(chatroom_listItem);
                edit0.putString("chatroom_list" + friend_id, json0);
                edit0.commit();

                //SAVE
                //친구 채팅방에 메시지 저장
                SharedPreferences share_message2 = getSharedPreferences(friend_id, MODE_PRIVATE);
                SharedPreferences.Editor edit00 = share_message2.edit();
                Gson gson00 = new Gson();
                String json00 = gson00.toJson(chatroom_listItem);
                edit00.putString("chatroom_list" + lucidmain.id, json00);
                edit00.commit();

                chat_message_edit.setText(""); //채팅한번 쳤으면 초기화


//                chatroom_info_apply();
                //채팅방이 이미 생성되어있으면 생성되지 못하게 막음
                SharedPreferences check = getSharedPreferences(lucidmain.id, MODE_PRIVATE);
                String checkpoint = check.getString("chatroom_check" + friend_id, "");
                if (checkpoint.equals("yes")) {

//                    Toast.makeText(dream_chatting.this, checkpoint, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(dream_chatting.this, "채팅방 생성막음", Toast.LENGTH_SHORT).show();
                } else {


//                    Toast.makeText(dream_chatting.this, checkpoint, Toast.LENGTH_SHORT).show();

                    edit0.putString("chatroom_check" + friend_id, "yes");
                    edit00.putString("chatroom_check" + lucidmain.id, "yes");
                    edit0.commit();
                    edit00.commit();


//                    Toast.makeText(dream_chatting.this, "채팅방 생성안막음", Toast.LENGTH_SHORT).show();

                    //SAVE
                    //내 아이디 채팅리스트에 채팅방 생성
                    SharedPreferences sharedPreferences = getSharedPreferences(friend_id, MODE_PRIVATE);
                    friend_profile_photo = sharedPreferences.getString("profilephoto", null);
                    chat_room_subject = friend_id;
                    dream_chatlist_item chat_item = new dream_chatlist_item(friend_profile_photo, chat_room_subject, 2, gettime(), lucidmain.id + " : " + message);
                    chatlist_listItem.add(0, chat_item);
                    SharedPreferences sharedPref = getSharedPreferences(lucidmain.id, MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPref.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(chatlist_listItem);
                    edit.putString("chat_list", json);
                    edit.commit();

                    //SAVE
                    //친구아이디 채팅리스트에 채팅방 생성
                    SharedPreferences sharedPre = getSharedPreferences(lucidmain.id, MODE_PRIVATE);
                    friend_profile_photo = sharedPre.getString("profilephoto", null);
                    chat_room_subject = lucidmain.id;
                    dream_chatlist_item chat_item2 = new dream_chatlist_item(friend_profile_photo, chat_room_subject, 2, gettime(), lucidmain.id + " : " + message);
                    chatlist_listItem2.add(0, chat_item2);
                    SharedPreferences sharedPref2 = getSharedPreferences(friend_id, MODE_PRIVATE);
                    SharedPreferences.Editor edit2 = sharedPref2.edit();
                    Gson gson2 = new Gson();
                    String json2 = gson2.toJson(chatlist_listItem2);
                    edit2.putString("chat_list", json2);
                    edit2.commit();
                    //여기까지 내 아이디 친구리스트에 해당친구 저장하기
                }
                load();


                dream_chatroom_adapter = new dream_chatroom_adapter(dream_chatting.this, chatroom_listItem);
                dream_chatroom_adapter.notifyDataSetChanged();
                dream_chatroom_listview.setAdapter(dream_chatroom_adapter);

            }


            private void chatroom_info_apply() {


                //SAVE
                //내 아이디 채팅리스트에 채팅방 생성
                SharedPreferences sharedPreferences = getSharedPreferences(friend_id, MODE_PRIVATE);
                friend_profile_photo = sharedPreferences.getString("profilephoto", null);
                chat_room_subject = friend_id;
                dream_chatlist_item chat_item = new dream_chatlist_item(friend_profile_photo, chat_room_subject, 2, gettime(), lucidmain.id + " : " + message);
                chatlist_listItem.add(0, chat_item);
                SharedPreferences sharedPref = getSharedPreferences(lucidmain.id, MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPref.edit();
                Gson gson = new Gson();
                String json = gson.toJson(chatlist_listItem);
                edit.putString("chat_list", json);
                edit.commit();

                //SAVE
                //친구아이디 채팅리스트에 채팅방 생성
                SharedPreferences sharedPre = getSharedPreferences(lucidmain.id, MODE_PRIVATE);
                friend_profile_photo = sharedPre.getString("profilephoto", null);
                chat_room_subject = lucidmain.id;
                dream_chatlist_item chat_item2 = new dream_chatlist_item(friend_profile_photo, chat_room_subject, 2, gettime(), message);
                chatlist_listItem2.add(0, chat_item2);
                SharedPreferences sharedPref2 = getSharedPreferences(friend_id, MODE_PRIVATE);
                SharedPreferences.Editor edit2 = sharedPref2.edit();
                Gson gson2 = new Gson();
                String json2 = gson2.toJson(chatlist_listItem2);
                edit2.putString("chat_list", json2);
                edit2.commit();
                //여기까지 내 아이디 친구리스트에 해당친구 저장하기
            }
        });
    }

    private void load() {

        // 내아이디 친구와의 채팅방 대화내용불러오기
        // 저장하기위해서는 기존 목록을 불러와야한다.
        SharedPreferences share = getSharedPreferences(lucidmain.id, MODE_PRIVATE);  // 친구하고 싶은사람 id에
        Gson gson = new Gson();
        String json = share.getString("chatroom_list" + friend_id, null);
        Type type = new TypeToken<ArrayList<dream_chatroom_item>>() {
        }.getType();
        chatroom_listItem = gson.fromJson(json, type);
        if (chatroom_listItem == null) {
            chatroom_listItem = new ArrayList<>();
        }

        //  내아이디 채팅방리스트 불러오기
        // 저장하기위해서는 기존 목록을 불러와야한다.
        SharedPreferences shared = getSharedPreferences(lucidmain.id, MODE_PRIVATE);  // 친구하고 싶은사람 id에
        Gson gson2 = new Gson();
        String json2 = shared.getString("chat_list", null);
        Type type2 = new TypeToken<ArrayList<dream_chatlist_item>>() {
        }.getType();
        chatlist_listItem = gson2.fromJson(json2, type2);
        if (chatlist_listItem == null) {
            chatlist_listItem = new ArrayList<>();
        }

        //  친구아이디 채팅방리스트 불러오기
        // 저장하기위해서는 기존 목록을 불러와야한다.
        SharedPreferences shared2 = getSharedPreferences(lucidmain.id, MODE_PRIVATE);  // 친구하고 싶은사람 id에
        Gson gson3 = new Gson();
        String json3 = shared2.getString("chat_list", null);
        Type type3 = new TypeToken<ArrayList<dream_chatlist_item>>() {
        }.getType();
        chatlist_listItem2 = gson3.fromJson(json3, type3);
        if (chatlist_listItem2 == null) {
            chatlist_listItem2 = new ArrayList<>();
        }


    }


    public String getday() {
        long mNow;
        Date mDate;
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");

        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

    public String gettime() {
        long mNow;
        Date mDate;
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd aa hh:mm:ss");

        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
        dream_chatroom_adapter = new dream_chatroom_adapter(this, chatroom_listItem);
        dream_chatroom_listview.setAdapter(dream_chatroom_adapter);

    }


}
