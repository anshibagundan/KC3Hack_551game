package com.example.eemon551;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class introduce extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);
    }

    public void introduce_home(View view){
        Intent intent = new Intent(introduce.this, activity_home.class);
        startActivity(intent);
    }
}