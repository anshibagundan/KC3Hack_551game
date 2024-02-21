package com.example.eemon551;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class decoration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoration);
    }

    public void decoration_home(View view){
        Intent intent = new Intent(decoration.this, activity_home.class);
        startActivity(intent);
    }
    public void decoration_zukann(View view){
        Intent intent = new Intent(decoration.this, garally.class);
        startActivity(intent);
    }
    public void decoration_store(View view){
        Intent intent = new Intent(decoration.this, store.class);
        startActivity(intent);
    }
    public void decoration_introduce(View view){
        Intent intent = new Intent(decoration.this, introduce.class);
        startActivity(intent);
    }
}