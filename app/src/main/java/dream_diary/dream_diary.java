package dream_diary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.idealist.www.myapplication2.R;

public class dream_diary extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dream_diary);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("꿈 일기장");


    }
}
