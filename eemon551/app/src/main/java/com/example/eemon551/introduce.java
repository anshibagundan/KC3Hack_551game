package com.example.eemon551;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

public class introduce extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_introduce);
    }

    public void introduce_home(View view){
        Intent intent = new Intent(introduce.this, activity_home.class);
        startActivity(intent);
    }
    public void introduce_zukann(View view){
        Intent intent = new Intent(introduce.this, garally.class);
        startActivity(intent);
    }
    public void introduce_store(View view){
        Intent intent = new Intent(introduce.this, store.class);
        startActivity(intent);
    }
    public void introduce_decoration(View view){
        Intent intent = new Intent(introduce.this, decoration.class);
        startActivity(intent);
    }
}