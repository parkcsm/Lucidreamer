package com.idealist.www.myapplication2.dream_post;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idealist.www.myapplication2.dream_post.dream_post_post;
import com.idealist.www.myapplication2.R;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class dream_post_post extends AppCompatActivity {


    String postText;
    String uri_String;

    private ProgressDialog simpleWaitDialog;
    private String id;
    Uri imageuri;

    ImageView imagePost;
    EditText textPost;
    Button btn_post;

    Bitmap bitmap; //고른사진이 Bitmap으로 임시로 저장되는 공간

    ArrayList<dream_post_item> listItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dream_post_post);

        SharedPreferences sharedPref = getSharedPreferences("Operation", MODE_PRIVATE);
        id = sharedPref.getString("Operation", null); // 로그인한 ID로 설정

        imagePost = findViewById(R.id.iv_post);
        textPost = findViewById(R.id.edt_text);
        btn_post = findViewById(R.id.btn_post);

        registerForContextMenu(imagePost);

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
            }
        });

        Toast.makeText(this, "사진을 길게 누르면 게시판에 사진을 등록할 수 있습니다.", Toast.LENGTH_SHORT).show();
        load();
    }


    class PostTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            simpleWaitDialog = ProgressDialog.show(dream_post_post.this, "잠시만 기다려주세요", "글 등록중...");
        }

        @Override
        protected Void doInBackground(Void... voids) {

            post();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            simpleWaitDialog.dismiss();
        }
    }


    private void post() {

        if (imageuri == null) {
            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
            imagePost.startAnimation(shake);
            Toast.makeText(this, "사진을 등록하지 않았습니다.", Toast.LENGTH_SHORT).show();

        } else {

            if (textPost.getText().toString().length() <= 4 || textPost.getText().toString().length() > 20) {
                Toast.makeText(this, "5자이상 20자 미만의 글자를 입력해주세요!", Toast.LENGTH_SHORT).show();
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                textPost.startAnimation(shake);
            } else {

                uri_String = imageuri.toString();
                postText = textPost.getText().toString();
                dream_post_item item = new dream_post_item(uri_String, id, postText, false, 0,0,gettime());
                listItem.add(0, item);

                //SAVE
                SharedPreferences sharedPreferences = getSharedPreferences("dream_post", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(listItem);
                editor.putString("postlist", json);
                editor.commit();

                Toast.makeText(this, "게시글을 등록했습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1) {
            imageuri = data.getData();
            Glide.with(this).load(imageuri).centerCrop().into(imagePost);
        }

        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {

            /** Uri로 하는 방법도 있고, bitmap으로 하는 방법도 있다.*/
            imageuri = data.getData();


            Glide.with(this).load(imageuri).centerCrop().into(imagePost);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // 컨텍스트 메뉴가 최초로 한번만 호출되는 콜백 메서드
        Log.d("test", "onCreateContextMenu");
//        getMenuInflater().inflate(R.menu.main, menu);

        menu.setHeaderTitle("포스트사진 등록방법을 선택하세요!");
        menu.add(0, 1, 100, "사진 촬영");
        menu.add(0, 2, 100, "갤러리에서 불러오기");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // 롱클릭했을 때 나오는 context Menu 의 항목을 선택(클릭) 했을 때 호출
        switch (item.getItemId()) {
            case 1:// 사진 촬영 선택시
                Intent CameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (CameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(CameraIntent, 1000);
                }
                return true;
            case 2:// 사진 불러오기 선택시
                Intent myintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(myintent, 1);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
    }


    public String gettime(){
        long mNow;
        Date mDate;
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd aa hh:mm:ss");

        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);}


}
