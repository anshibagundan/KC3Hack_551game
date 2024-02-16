package com.example.eemon551;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class game extends AppCompatActivity {

    private int currentQuestionId; // 現在表示されている質問のID
    private int currentQuestionLoc_id;
    private boolean currentQuestioniskansai;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Button buttonLeft = findViewById(R.id.button_left);
        Button buttonRight = findViewById(R.id.button_right);
        TextView questionText = findViewById(R.id.question_text);
        ImageView questionImage = findViewById(R.id.Question_image);

        // ApiServiceインスタンスを取得
        ApiService apiService = ApiClient.getApiService();

        // APIリクエストを実行して質問をロード
        apiService.getAllQuestions().enqueue(new Callback<List<Question>>() {
            Random random = new Random();
            int questionNo = random.nextInt(10);

            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // 質問のデータをセット
                    String name = response.body().get(questionNo).getName();
                    questionText.setText(name);

                    String img = response.body().get(questionNo).getImg().replace("\"", "").trim();
                    int resourceId = getResources().getIdentifier(img, "drawable", getPackageName());
                    questionImage.setImageResource(resourceId);

                    currentQuestionId = response.body().get(questionNo).getId();
                    currentQuestionLoc_id = response.body().get(questionNo).getLoc_id();

                    // ここでlocationデータを取得
                    apiService.getLocationById(currentQuestionLoc_id).enqueue(new Callback<Location>() {
                        @Override
                        public void onResponse(Call<Location> call, Response<Location> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                currentQuestioniskansai = response.body().isIskansai();
                            }
                        }

                        @Override
                        public void onFailure(Call<Location> call, Throwable t) {
                            Log.e("LocationFetch", "locationデータ取得失敗: ", t);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Log.e("GameActivity", "APIリクエスト失敗: ", t);
            }
        });
    }


//        // button_leftがクリックされたときの処理
//        buttonLeft.setOnClickListener(view -> {
//            if (currentQuestioniskansai){
//                TextView.settext("正解")；
//            }
//
//            apiService.insertUserQuestionData(data).enqueue(new Callback<Void>() {
//                @Override
//                public void onResponse(Call<Void> call, Response<Void> response) {
//                    if (response.isSuccessful()) {
//                        // データ挿入成功の処理
//                        Log.d("game", "データ挿入成功");
//                        questionText.setText("cor: true");
//                    } else {
//                        // サーバーからのエラーレスポンスの処理
//                        Log.e("game", "データ挿入失敗: " + response.message());
//                        questionText.setText("Error: " + response.code());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Void> call, Throwable t) {
//                    Log.e("game", "データ挿入失敗: ", t);
//                    questionText.setText("Network Error");
//                }
//            });
//
//            // button_leftがクリックされたときの処理
//            buttonRight.setOnClickListener(view -> {
//
//
//                    @Override
//                    public void onFailure(Call<Void> call, Throwable t) {
//                        Log.e("game", "データ挿入失敗: ", t);
//                        questionText.setText("Network Error");
//                    }
//                });
//            });
//        });
    }