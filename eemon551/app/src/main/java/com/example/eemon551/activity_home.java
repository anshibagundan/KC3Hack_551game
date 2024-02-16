package com.example.eemon551;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button startButton = findViewById(R.id.start);
        TextView text_genre= findViewById(R.id.genre);
        ImageView img1 = findViewById(R.id.right_genre);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intentを作成してGameActivityを起動
                Intent intent = new Intent(activity_home.this, game.class);
                startActivity(intent);
            }
        });

        ApiService apiService = ApiClient.getApiService();

        // APIリクエストを実行
        apiService.getAllQuestions().enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // 最初の質問のnameを取得してTextViewにセット
                    int genre_id = response.body().get(0).getGenre_id();
                    String genre;
                    if(genre_id == 1){
                        genre = "食べ物";
                    } else if (genre_id == 2) {
                        genre = "建物";
                    } else if (genre_id == 3) {
                        genre = "人";
                    } else if (genre_id == 4) {
                        genre = "土地";
                    } else if (genre_id == 5) {
                        genre = "文化";
                    } else{
                        genre = "全て";
                    }
                    text_genre.setText(genre);
                }
            }
            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                // エラーハンドリング
                Log.e("MainActivity", "APIリクエスト失敗: ", t);
            }
        });

        apiService.getAllQuestions().enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // 最初の質問のnameを取得してTextViewにセット
                    String img = response.body().get(0).getImg();
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
}