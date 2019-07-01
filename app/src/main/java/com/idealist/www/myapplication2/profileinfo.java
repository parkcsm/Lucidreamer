package com.idealist.www.myapplication2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class profileinfo extends AppCompatActivity {

    private ProgressDialog simpleWaitDialog;
    private String id;

    Button btn_save;
    ImageView profilephoto;
    EditText profilepr;

    Bitmap bitmap; //고른사진이 Bitmap으로 임시로 저장되는 공간
    Bitmap ResourceSave; // Glide.centercrop으로 Bitmap을 저장하는 메소드의 resource저장소
    String uri_String; // Bitmap사진이 String으로 바뀌어서 임시로 저장되는 공간
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileinfo);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("프로필 수정");


        SharedPreferences sharedPref = getSharedPreferences("Operation", MODE_PRIVATE);
        id = sharedPref.getString("Operation", null); // 로그인한 ID로 설정

        profilephoto = findViewById(R.id.iv_profilephoto);
        profilepr = findViewById(R.id.tv_profiletext);
        btn_save = findViewById(R.id.btn_profilesave);


        registerForContextMenu(profilephoto);


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                save();
//                PostTask postTask = new PostTask();
//                postTask.execute();

            }
        });

        Toast.makeText(this, "동그라미를 길게 누르면 프로필사진을 등록할 수 있습니다.", Toast.LENGTH_SHORT).show();

        load();
    }


    /**
     * 사진불러오는 Request를 처리한다.
     */


    class PostTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            simpleWaitDialog = ProgressDialog.show(profileinfo.this, "잠시만 기다려주세요", "정보 저장중...");

        }

        @Override
        protected Void doInBackground(Void... voids) {
            save();
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            simpleWaitDialog.dismiss();

            //    Intent myintent = new Intent(profileinfo.this, lucidmain.class);
            //    startActivity(myintent);
            onBackPressed();
            finish();
            Toast.makeText(profileinfo.this, "프로필 정보를 저장했습니다.", Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1) {
            uri = data.getData();

            Glide.with(this).load(uri).centerCrop().into(profilephoto);

        }

        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {

            /** Uri로 하는 방법도 있고, bitmap으로 하는 방법도 있다.*/
            uri = data.getData();

            Glide.with(this).load(uri).centerCrop().into(profilephoto);

        }
    }

    private String getStringFromBitmap(Bitmap bitmapPicture) {
 /*
 * This functions converts Bitmap picture to a string which can be
 * JSONified.
 * */
        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }


    private Bitmap getBitmapFromString(String json) {
/*
* This Function converts the String back to Bitmap
* */
        byte[] decodedString = Base64.decode(json, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    private void save() {

        if (uri == null) {
            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
            profilephoto.startAnimation(shake);
            Toast.makeText(this, "프로필 사진을 등록해주세요.", Toast.LENGTH_SHORT).show();
        } else {
            if (profilepr.getText().toString().length() > 18) {
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                profilepr.startAnimation(shake);
                Toast.makeText(this, "상태메세지는 18자 이내로 작성해주시기 바랍니다.", Toast.LENGTH_SHORT).show();
            } else {
                //저장
                SharedPreferences sharedPreferences = getSharedPreferences(id, MODE_PRIVATE); // 로그인한 id 저장소를 불러오기, 이 때 불려와지는 friend list는 각각 다름
                SharedPreferences.Editor editor = sharedPreferences.edit();
                uri_String = uri.toString();
                editor.putString("profilephoto", uri_String);
                editor.putString("profilepr", profilepr.getText().toString());
                editor.commit();


                //    Intent myintent = new Intent(profileinfo.this, lucidmain.class);
                //    startActivity(myintent);
                onBackPressed();
                finish();
                Toast.makeText(profileinfo.this, "프로필 정보를 저장했습니다.", Toast.LENGTH_SHORT).show();

            }
            //bytebitmap에 저장
        }
    }

    private void load() {

        SharedPreferences sharedPreferences = getSharedPreferences(id, MODE_PRIVATE); // 로그인한 id 저장소를 불러오기, 이 때 불려와지는 friend list는 각각 다름
        if (sharedPreferences.getString("profilephoto", null) != null) {
            String savefile = sharedPreferences.getString("profilephoto", null);
            uri_String = savefile;
            uri = Uri.parse(uri_String);
            Glide.with(this).load(uri).centerCrop().into(profilephoto);
        }

        if (sharedPreferences.getString("profilepr", null) != null) {
            String savetext = sharedPreferences.getString("profilepr", null);
            profilepr.setText(savetext);
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // 컨텍스트 메뉴가 최초로 한번만 호출되는 콜백 메서드
        Log.d("test", "onCreateContextMenu");
//        getMenuInflater().inflate(R.menu.main, menu);

        menu.setHeaderTitle("프로필사진 등록방법을 선택하세요!");
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
}
