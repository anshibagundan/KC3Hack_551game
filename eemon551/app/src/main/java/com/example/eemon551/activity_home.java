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

    private Button startButton;
    private TextView textGenre;
    private ImageView img1;
    private int genreId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeViews();
        loadFirstQuestionGenre();
    }

    //ここにIDの呼び出しかいてね
    private void initializeViews() {
        startButton = findViewById(R.id.start);
        textGenre = findViewById(R.id.genre);
        img1 = findViewById(R.id.right_genre);
    }

    public void setButtonClickListener(View view) {
        // Intentを作成してGameActivityを起動
        Intent intent = new Intent(activity_home.this, game.class);
        startActivity(intent);
    }

    private void loadFirstQuestionGenre() {
        ApiService apiService = ApiClient.getApiService();

        // APIリクエストを実行
        apiService.getAllQuestions().enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // 最初の質問のgenre_idを取得してTextViewにセット
//                    int genreId = response.body().get(0).getGenre_id();
                    String genre = getGenreName(genreId);
                    textGenre.setText(genre);
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                // エラーハンドリング
                Log.e("activity_home", "APIリクエスト失敗: ", t);
            }
        });
    }

    private String getGenreName(int genreId) {
        switch (genreId) {
            case 1:
                return "食べ物";
            case 2:
                return "建物";
            case 3:
                return "人";
            case 4:
                return "土地";
            case 5:
                return "文化";
            default:
                return "全て";
        }
    }

    public void genre_right(View view){
        genreId++;
        genreId = genreId % 6;
    }
    public void genre_left(View view){
        genreId--;
        genreId = genreId % 6;
    }
}
