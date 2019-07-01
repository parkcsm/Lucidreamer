package com.idealist.www.myapplication2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idealist.www.myapplication2.dream_friend_apply.dream_friend_apply_item;
import com.idealist.www.myapplication2.dream_post.dream_post_item;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class friend_info extends AppCompatActivity {


    public static ArrayList<dream_friend_apply_item> listItem = new ArrayList<>();

    //여기서 id는 친구 id
    private String Friendid;
    boolean Check_Apply;
    boolean Check_Friendlist;

    ImageView friend_photo;
    TextView friend_introduce, friend_id;
    Button friend_apply;

    String uri_String; // Bitmap사진이 String으로 바뀌어서 임시로 저장되는 공간
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("친구 정보");


        Intent myintent = getIntent();

        //여기서 id는 친구 id
        Friendid = myintent.getStringExtra("FRIENDID");


        friend_photo = findViewById(R.id.friend_photo);
        friend_introduce = findViewById(R.id.friend_introduce);
        friend_id = findViewById(R.id.friend_id);
        friend_apply = findViewById(R.id.friend_apply);


        if (Friendid.equals(lucidmain.id)) {
            friend_apply.setVisibility(View.INVISIBLE);
        } else {
            Toast.makeText(this, "친구 신청후 친구가 되기위해서는 상대방의 허락이 필요합니다.", Toast.LENGTH_SHORT).show();
        }

        friend_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //신청했는지 여부를 확인한다. false => 현재 로그인된 아이디가 다시 친추를 받을 수 있다.
                SharedPreferences SharedPrefs = getSharedPreferences(lucidmain.id, MODE_PRIVATE);
                Check_Apply = SharedPrefs.getBoolean(lucidmain.id + Friendid + "apply", false);

                //친구신청을 받아서 이미 친구목록에 있는지를 확인한다. false => 현재 로그인된 아이디가 다시 친추를 받을 수 있다.
                SharedPreferences SharedPref = getSharedPreferences(lucidmain.id, MODE_PRIVATE);
                Check_Friendlist = SharedPref.getBoolean(lucidmain.id + Friendid, false);


                if (Friendid.equals(lucidmain.id)) {
                    Toast.makeText(friend_info.this, "자기 자신에게 친구신청을 할 수는 없습니다.", Toast.LENGTH_SHORT).show();
                } else if (Check_Friendlist == true) {
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    friend_apply.startAnimation(shake);
                    Toast.makeText(friend_info.this, "이미 친구목록에 있습니다.", Toast.LENGTH_SHORT).show();
                } else if (Check_Apply == true) {
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    friend_apply.startAnimation(shake);
                    Toast.makeText(friend_info.this, "이미 친구신청을 했습니다..", Toast.LENGTH_SHORT).show();
                } else {

                    //SAVE
                    //신청했으면 신청한 부분을 true로 표시해준다.
                    SharedPreferences SharedPrefss = getSharedPreferences(lucidmain.id, MODE_PRIVATE);
                    SharedPreferences.Editor edit = SharedPrefss.edit();
                    edit.putBoolean(lucidmain.id + Friendid + "apply", true);
                    edit.commit();

                    friend_apply();
                }

            }


        });


        load();
    }

    private void friend_apply() {

        SharedPreferences sharedPreference = getSharedPreferences(Friendid, MODE_PRIVATE);  // 친구하고 싶은사람 id에
        Gson gson = new Gson();
        String json = sharedPreference.getString("friend_apply_list", null);
        Type type = new TypeToken<ArrayList<dream_friend_apply_item>>() {
        }.getType();
        listItem = gson.fromJson(json, type);
        if (listItem == null) {
            listItem = new ArrayList<>();
        }
        // 여기위까지는 로드부분


        dream_friend_apply_item item = new dream_friend_apply_item(lucidmain.id);    //  내 정보 lucidmain.id 전송
        listItem.add(0, item);

        //SAVE 여기부터는 기존 friend_apply_list에 새로운 apply_list 추가하기
        SharedPreferences sharedPreferences = getSharedPreferences(Friendid, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gsons = new Gson();
        String jsons = gsons.toJson(listItem);
        editor.putString("friend_apply_list", jsons);
        editor.commit();
        Toast.makeText(this, "친구 신청을 완료했습니다.", Toast.LENGTH_SHORT).show();


    }


    private void load() {

        SharedPreferences sharedPreferences = getSharedPreferences(Friendid, MODE_PRIVATE); // 로그인한 id 저장소를 불러오기, 이 때 불려와지는 friend list는 각각 다름
        if (sharedPreferences.getString("profilephoto", null) != null) {
            String savefile = sharedPreferences.getString("profilephoto", null);
            uri_String = savefile;
            uri = Uri.parse(uri_String);
            Glide.with(this).load(uri).centerCrop().into(friend_photo);
        }

        if (sharedPreferences.getString("profilepr", null) != null) {
            String savetext = sharedPreferences.getString("profilepr", null);
            friend_introduce.setText(savetext);
        }

        friend_id.setText(Friendid);

    }

}
