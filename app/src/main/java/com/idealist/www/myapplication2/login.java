package com.idealist.www.myapplication2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity {

    String loginid, loginpswd;

    EditText edtlg_id, edtlg_pswd;
    Button btn_login, btn_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("어플 소개 및 꿈 음악");


        edtlg_id = findViewById(R.id.edtlg_id);
        edtlg_pswd = findViewById(R.id.edtlg_pswd);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });


    }



    private void login() {


        loginid = edtlg_id.getText().toString();
        loginpswd = edtlg_pswd.getText().toString();

        SharedPreferences Sharedpref = getSharedPreferences("pswd", MODE_PRIVATE);

        if (Sharedpref.getString(loginid, "randomvalue911013").equals(loginpswd)) {

            SharedPreferences Operation = getSharedPreferences("Operation", MODE_PRIVATE);
            SharedPreferences.Editor OperationEdit = Operation.edit();
            OperationEdit.putString("Operation", loginid);
            OperationEdit.commit();


            Intent myintent = new Intent(login.this, lucidmain.class);
            startActivity(myintent);
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            finish();
        } else {
            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
            edtlg_id.startAnimation(shake);
            edtlg_pswd.startAnimation(shake);
            Toast.makeText(this, "아이디와 비밀번호가 일치하지 않습니다. 다시 입력해주세요!", Toast.LENGTH_SHORT).show();
        }
    }


    private void register() {
        Intent myintent = new Intent(login.this, register.class);
        startActivity(myintent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
//
//        edtlg_id.setText("");
        edtlg_pswd.setText("");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }

}
