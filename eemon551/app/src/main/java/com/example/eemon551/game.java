package com.example.eemon551;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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
    private boolean currentQuestionIsKansai;

    private ApiService apiService;
    private TextView questionText;

    private TextView seigoText;
    private ImageView questionImage;
    private boolean seigo = false;

    private FrameLayout kaisetu;

    private FrameLayout toi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        questionText = findViewById(R.id.question_text);
        seigoText = findViewById(R.id.seigo);
        questionImage = findViewById(R.id.Question_image);
        kaisetu = findViewById(R.id.kaisetu);



        // ApiServiceインスタンスを取得
        apiService = ApiClient.getApiService();

        loadQuestion();
    }

    private void loadQuestion() {
        Random random = new Random();
        int questionNo = random.nextInt(10); // 仮定する質問の数に応じて調整

        // APIリクエストを実行して質問をロード
        apiService.getAllQuestions().enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // 質問データをセット
                    Question question = response.body().get(questionNo);
                    String name = question.getName() + "が～？";
                    questionText.setText(name);

                    String img = question.getImg().replace("\"", "").trim();
                    int resourceId = getResources().getIdentifier(img, "drawable", getPackageName());
                    questionImage.setImageResource(resourceId);

                    currentQuestionId = question.getId();
                    currentQuestionLoc_id = question.getLoc_id();

                    // locationデータを取得
                    loadLocationData(currentQuestionLoc_id);
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Log.e("LocationFetch", "APIリクエスト失敗: ", t);
            }
        });
    }

    private void loadLocationData(int locId) {
        apiService.getLocationById(locId).enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentQuestionIsKansai = response.body().isIskansai();
                    Log.e("LocationFetch", String.valueOf(currentQuestionIsKansai));
                    Button buttonLeft = findViewById(R.id.button_left);
                    Button buttonRight = findViewById(R.id.button_right);
                    setupButtonListeners(buttonLeft, buttonRight, currentQuestionIsKansai);
                }
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                Log.e("LocationFetch", "locationデータ取得失敗: ", t);
            }
        });
    }

    private void setupButtonListeners(Button buttonLeft, Button buttonRight,Boolean currentQuestionIsKansai) {
        Log.e("LocationFetch", "button" +currentQuestionIsKansai);
        buttonLeft.setOnClickListener(view -> {
            if (currentQuestionIsKansai) {
                // 正解の処理
                Log.d("LocationFetch", "left true");
                seigoText.setText("正解！画像をセット");
                updateUserScore(10);
            } else {
                // 不正解の処理
                Log.d("LocationFetch", "left false");
                seigoText.setText("不正解画像をセット");
            }
            seigoText.setVisibility(View.VISIBLE);
            seigo = true;
        });

        buttonRight.setOnClickListener(view -> {
            if (!currentQuestionIsKansai) {
                Log.d("LocationFetch", "right true");
                // 正解の処理
                seigoText.setText("正解！画像をセット");
                updateUserScore(10); // スコアを10加算する
            } else {
                Log.d("LocationFetch", "right false");
                // 不正解の処理
                seigoText.setText("不正解画像をセット");
            }
            seigoText.setVisibility(View.VISIBLE);
            seigo = true;
        });
    }

    public void onTap(View view){
        if(seigo){
//            toi.setVisibility(View.GONE);
            kaisetu.setVisibility(View.VISIBLE);

        }
    }

    private void updateUserScore(int scoreToAdd) {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("UserId", 0);
        Log.e("UserID", String.valueOf(userId));

        // 現在のユーザースコアを取得するAPIリクエストを想定
        apiService.getUserMoney(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int currentScore = response.body().getMoney();
                    Log.e("money", String.valueOf(currentScore));
                    int newScore = currentScore +10;
                    SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    String name = prefs.getString("UserName", "デフォルト名");
                    apiService.updateUserData(userId,  new ApiService.UserUpdateRequest(name,newScore)).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Log.d("UserDataUpdate", "ユーザーデータ更新成功");
                            } else {
                                Log.e("UserDataUpdate", "ユーザーデータ更新失敗: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("UserDataUpdate", "ユーザーデータ更新失敗", t);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("UserMoneyFetch", "ユーザーのmoney取得失敗", t);
            }
        });
    }

}
