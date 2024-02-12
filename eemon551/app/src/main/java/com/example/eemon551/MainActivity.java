package com.example.eemon551;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call; // Callをインポート
import retrofit2.Callback; // Callbackをインポート
import retrofit2.Response; // Responseをインポート


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TextViewを取得
        TextView textView = findViewById(R.id.app_name);

        // ApiServiceインスタンスを取得
        ApiService apiService = ApiClient.getApiService();

        // APIリクエストを実行
        apiService.getAllQuestions().enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // 最初の質問のnameを取得してTextViewにセット
                    String name = response.body().get(0).getName();
                    textView.setText(name);
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                // エラーハンドリング
                Log.e("MainActivity", "APIリクエスト失敗: ", t);
            }
        });
    }
}
