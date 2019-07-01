package com.idealist.www.myapplication2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class privateinformation extends AppCompatActivity {

    String spinner;

    ProgressDialog simpleWaitDialog;
    String id, pswd, npswd, name, age, sex;
    TextView rv_id;
    EditText rv_ppswd, rv_npswd, rv_npswdc, rv_name, rv_age;
    Button btn_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privateinformation);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("개인정보 수정");

        rv_id = findViewById(R.id.rv_id);
        rv_ppswd = findViewById(R.id.rv_pswd);
        rv_npswd = findViewById(R.id.rv_npswd);
        rv_npswdc = findViewById(R.id.rv_npswdc);
        rv_name = findViewById(R.id.rv_name);
        rv_age = findViewById(R.id.rv_age);
        btn_rv = findViewById(R.id.btn_rv);


        SharedPreferences rv_SharePref = getSharedPreferences("Operation", MODE_PRIVATE);
        id = rv_SharePref.getString("Operation", null);
        rv_id.setText(id);

        SharedPreferences rv_Sharedname = getSharedPreferences("name", MODE_PRIVATE);
        name = rv_Sharedname.getString(id, null);
        rv_name.setText(name);

        SharedPreferences rv_Sharedage = getSharedPreferences("age", MODE_PRIVATE);
        age = rv_Sharedage.getString(id, null);
        rv_age.setText(age);

        SharedPreferences rv_Sharedsex = getSharedPreferences("sex", MODE_PRIVATE);
        sex = rv_Sharedsex.getString(id, null);


        btn_rv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rv();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();

        rv_ppswd.setText("");
        rv_npswd.setText("");
        rv_npswdc.setText("");
    }

    private void rv() {

        SharedPreferences rv_Sharedpswd = getSharedPreferences("pswd", MODE_PRIVATE);
        pswd = rv_Sharedpswd.getString(id, null);

        if (rv_ppswd.getText().toString().equals(pswd)) {

            if (rv_npswd.length() < 5) {
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                rv_npswd.startAnimation(shake);
                Toast.makeText(this, "5자이상의 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                rv_npswd.requestFocus();
            } else {
                if (rv_npswd.getText().toString().equals(rv_npswdc.getText().toString())) {
                    npswd = rv_npswd.getText().toString();
                    if (rv_name.length() < 3) {
                        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                        rv_name.startAnimation(shake);
                        Toast.makeText(this, "3자이상의 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                        rv_name.requestFocus();
                    } else {
                        if (rv_age.length() == 0) {
                            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                            rv_age.startAnimation(shake);
                            Toast.makeText(this, "나이를 입력해주세요", Toast.LENGTH_SHORT).show();
                            rv_age.requestFocus();
                        } else {

                            final Spinner s = (Spinner) findViewById(R.id.spinner2);

                            s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view,
                                                           int position, long id) {


                                    spinner = (String) parent.getItemAtPosition(position);
                                    parent.setSelection(1);


                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                    parent.setSelection(1);

                                }
                            });


                            if (spinner == "남자") {
                                sex = "남";
                            } else if (spinner == "여자") {
                                sex = "여";
                            }

                            name = rv_name.getText().toString();
                            age = rv_age.getText().toString();


                            PostTask postTask = new PostTask();
                            postTask.execute();

//
//                                Toast.makeText(this, "회원정보 수정이 완료되었습니다.", Toast.LENGTH_LONG).show();
//                                //회원정보 수정


                        }
                    }

                } else {

                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    rv_npswd.startAnimation(shake);
                    rv_npswdc.startAnimation(shake);
                    Toast.makeText(this, "새로운 비밀번호와 새로운 비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    rv_npswd.requestFocus();
                }

            }
        } else {
            Toast.makeText(this, "현재 비밀번호를 잘못입력하셨습니다. 다시 입력해주세요", Toast.LENGTH_SHORT).show();
            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
            rv_ppswd.startAnimation(shake);

        }


    }

    class PostTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            simpleWaitDialog = ProgressDialog.show(privateinformation.this, "잠시만 기다려주세요", "정보 저장중...");
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

            onBackPressed();
            //Intent myintent = new Intent(privateinformation.this, lucidmain.class);
            //startActivity(myintent);
            finish();
            Toast.makeText(privateinformation.this, "개인 정보가 변경되었습니다.", Toast.LENGTH_SHORT).show();

        }
    }

    private void save() {
        registeritem item = new registeritem(id, npswd, name, age, sex);

        SharedPreferences infosave = getSharedPreferences("info", MODE_PRIVATE);
        SharedPreferences.Editor infosaveEditor = infosave.edit();
        Gson gson = new Gson();
        String json = gson.toJson(item);
        infosaveEditor.putString(id, json);
        infosaveEditor.commit();

        SharedPreferences info_pswd = getSharedPreferences("pswd", MODE_PRIVATE);
        SharedPreferences.Editor info_pswdEditor = info_pswd.edit();
        info_pswdEditor.putString(id, npswd);
        info_pswdEditor.commit();

        SharedPreferences info_name = getSharedPreferences("name", MODE_PRIVATE);
        SharedPreferences.Editor info_nameEditor = info_name.edit();
        info_nameEditor.putString(id, name);
        info_nameEditor.commit();

        SharedPreferences info_age = getSharedPreferences("age", MODE_PRIVATE);
        SharedPreferences.Editor info_ageEditor = info_age.edit();
        info_ageEditor.putString(id, age);
        info_ageEditor.commit();

        SharedPreferences info_sex = getSharedPreferences("sex", MODE_PRIVATE);
        SharedPreferences.Editor info_sexEditor = info_sex.edit();
        info_sexEditor.putString(id, sex);
        info_sexEditor.commit();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
    }


}
