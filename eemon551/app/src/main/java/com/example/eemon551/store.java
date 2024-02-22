package com.example.eemon551;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class store extends AppCompatActivity {

    private TextView money;
    private ApiService apiService;
    private GridLayout gridLayout_1;
    private Set<Integer> displayedQuestionIds = new HashSet<>();
    private FrameLayout store_screen;
    private FrameLayout card_screen;
    private FrameLayout title_screen;

    private int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        apiService = ApiClient.getApiService();
        gridLayout_1 = findViewById(R.id.card_layout);
        money =findViewById(R.id.money);
        gridLayout_1.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout_1.setColumnCount(3);
        store_screen = findViewById(R.id.store);
        card_screen = findViewById(R.id.buy_card_screen);
        title_screen = findViewById(R.id.buy_title_screen);

        GetMoney();
        for (int i = 0; i < 6; i++) {
            fetchQuestions();
        }
    }

    private void GetMoney(){
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("UserId", 1);
        apiService.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int currentScore = response.body().getMoney();
                    money.setText(String.valueOf(currentScore));
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("API Request Failure", "Error: ", t);
            }
        });
    }


    private void fetchQuestions() {

            apiService.getAllQuestions().enqueue(new Callback<List<Question>>() {
                @Override
                public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {

                        Random random = new Random();
                        int questionNo = random.nextInt(10);
                    if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                        Question question = response.body().get(questionNo);

                            if (!displayedQuestionIds.contains(question.getId()) && question.getLoc_id() >= 1 && question.getLoc_id() <= 6) {
                                DisplayQuestion(question);
                                displayedQuestionIds.add(question.getId());
                            } else {
                                //もし出題するidが出題済みidリストに存在したらもう一回ロード
                                fetchQuestions();
                            }
                        }

                }


                @Override
                public void onFailure(Call<List<Question>> call, Throwable t) {
                    Log.e("API Request Failure", "Error: ", t);
                }
            });
        }
    private void DisplayQuestion(Question question) {

            ImageView imageView = new ImageView(this);
            String img = question.getCard().replace("\"", "").trim();
            int imageResId = getResources().getIdentifier(img, "drawable", getPackageName());
            Glide.with(this)
                    .load(imageResId)
                    .override(600, 400) // 画像の解像度を指定
                    .into(imageView);
            gridLayout_1.addView(imageView);


    }

    public void buy_card(View view) {
    }

    public void buy_back(View view) {
    }

    public void buy_title(View view) {
    }

    //購入画面からストアに戻る
    public void back_store(View view) {
        store_screen.setVisibility(View.VISIBLE);
        card_screen.setVisibility(View.GONE);
        title_screen.setVisibility(View.GONE);
    }

    public void go_buy_title(View view) {
        title_screen.setVisibility(View.VISIBLE);
        store_screen.setVisibility(View.GONE);
    }

    public void go_buy_card(View view){
        card_screen.setVisibility(View.GONE);
        store_screen.setVisibility(View.VISIBLE);
    }

//    ホームデータ遷移
    public void store_zukann(View view){
    Intent intent = new Intent(store.this, garally.class);
    startActivity(intent);
    }
    public void store_introduce(View view){
        Intent intent = new Intent(store.this, introduce.class);
        startActivity(intent);
    }
    public void store_home(View view){
        Intent intent = new Intent(store.this, activity_home.class);
        startActivity(intent);
    }
    public void store_deco(View view) {
        Intent intent = new Intent(store.this, decoration.class);
        startActivity(intent);
    }

}