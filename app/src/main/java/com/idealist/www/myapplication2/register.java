package com.idealist.www.myapplication2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class register extends AppCompatActivity {

    public static final String TAG = "register";

    String spinner;

    String rg_id, rg_pswd, rg_name, rg_age, rg_sex;
    EditText edtrg_id, edtrg_pswd, edtrg_pswdcheck, edtrg_name, edtrg_age;
    Button btn_dpcheck, btn_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("회원가입");


        setting.login_duplicatecheck = false;

        edtrg_id = findViewById(R.id.edtrg_id);
        edtrg_pswd = findViewById(R.id.edtrg_pswd);
        edtrg_pswdcheck = findViewById(R.id.edtrg_pswdcheck);
        edtrg_name = findViewById(R.id.edtrg_name);
        edtrg_age = findViewById(R.id.edtrg_age);

        btn_dpcheck = findViewById(R.id.btn_dpcheck);
        btn_register = findViewById(R.id.btn_register);

        btn_dpcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpcheck();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

    }


    private void dpcheck() {

        SharedPreferences sharedPreferences = getSharedPreferences("info", MODE_PRIVATE);
        rg_id = edtrg_id.getText().toString();

        if (edtrg_id.length() < 5 || edtrg_id.length() >10) {

            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
            edtrg_id.startAnimation(shake);
            Toast.makeText(this, "5자이상 10자 이하의 아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
            edtrg_id.requestFocus();
        } else {
            if (sharedPreferences.getString(rg_id, null) == null) {
                Toast.makeText(this, "회원가입이 가능한 아이디 입니다.", Toast.LENGTH_LONG).show();
                setting.login_duplicatecheck = true;
            } else {
                Toast.makeText(this, "이미 해당 아이디가 존재합니다. 다른 아이디를 선택해주세요.", Toast.LENGTH_LONG).show();
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                edtrg_id.startAnimation(shake);

            }
        }
    }

    private void register() {

        if (setting.login_duplicatecheck == true) {
            if (edtrg_pswd.length() < 5) {
                Toast.makeText(this, "5자이상의 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                edtrg_pswd.startAnimation(shake);
                edtrg_pswd.requestFocus();
            } else {
                if (edtrg_pswd.getText().toString().equals(edtrg_pswdcheck.getText().toString())) {
                    if (edtrg_name.length() < 3) {
                        Toast.makeText(this, "3자이상의 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                        edtrg_name.requestFocus();
                        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                        edtrg_name.startAnimation(shake);

                    } else {
                        if (edtrg_age.length() == 0) {
                            Toast.makeText(this, "나이를 입력해주세요", Toast.LENGTH_SHORT).show();
                            edtrg_age.requestFocus();
                            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                            edtrg_age.startAnimation(shake);

                        } else {

                            Spinner s = (Spinner) findViewById(R.id.spinner);
                            s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view,
                                                           int position, long id) {

                                    spinner = (String) parent.getItemAtPosition(position);

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });


                            rg_id = edtrg_id.getText().toString();
                            rg_pswd = edtrg_pswd.getText().toString();
                            rg_name = edtrg_name.getText().toString();
                            rg_age = edtrg_age.getText().toString();
                            if (spinner == "남자") {
                                rg_sex = "남";
                            } else if (spinner == "여자") {
                                rg_sex = "여";
                            }
                            registeritem item = new registeritem(rg_id, rg_pswd, rg_name, rg_age, rg_sex);

                            SharedPreferences infosave = getSharedPreferences("info", MODE_PRIVATE);
                            SharedPreferences.Editor infosaveEditor = infosave.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(item);
                            infosaveEditor.putString(rg_id, json);
                            infosaveEditor.commit();

                            SharedPreferences info_pswd = getSharedPreferences("pswd", MODE_PRIVATE);
                            SharedPreferences.Editor info_pswdEditor = info_pswd.edit();
                            info_pswdEditor.putString(rg_id, rg_pswd);
                            info_pswdEditor.commit();

                            SharedPreferences info_name = getSharedPreferences("name", MODE_PRIVATE);
                            SharedPreferences.Editor info_nameEditor = info_name.edit();
                            info_nameEditor.putString(rg_id, rg_name);
                            info_nameEditor.commit();

                            SharedPreferences info_age = getSharedPreferences("age", MODE_PRIVATE);
                            SharedPreferences.Editor info_ageEditor = info_age.edit();
                            info_ageEditor.putString(rg_id, rg_age);
                            info_ageEditor.commit();

                            SharedPreferences info_sex = getSharedPreferences("sex", MODE_PRIVATE);
                            SharedPreferences.Editor info_sexEditor = info_sex.edit();
                            info_sexEditor.putString(rg_id, rg_sex);
                            info_sexEditor.commit();


                            Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show();
                            //회원가입 정보저장


                            Intent myintent = new Intent(register.this, registerfinish.class);
                            startActivity(myintent);
                            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                            finish();

                        }
                    }

                } else {
                    Toast.makeText(this, "비밀번호와 비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    edtrg_pswd.requestFocus();
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    edtrg_pswd.startAnimation(shake);
                    edtrg_pswdcheck.startAnimation(shake);

                }
            }

        } else {
            Toast.makeText(this, "중복확인을 해주세요!", Toast.LENGTH_LONG).show();
            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
            btn_dpcheck.startAnimation(shake);

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "OnStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "OnRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "OnResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "OnPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "OnStop");
        edtrg_pswd.setText("");
        edtrg_pswdcheck.setText("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }

}
