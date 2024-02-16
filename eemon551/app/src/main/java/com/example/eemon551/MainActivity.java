package com.example.eemon551;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button tapButton = findViewById(R.id.tap);

        tapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intentを作成してGameActivityを起動
                Intent intent = new Intent(MainActivity.this, activity_home.class);
                startActivity(intent);
            }
        });

        // TextViewを取得
        TextView textView = findViewById(R.id.app_name);


        // ApiServiceインスタンスを取得
        ApiService apiService = ApiClient.getApiService();

        // APIリクエストを実行

        textView.setText("551ゲーム");

    }
}
