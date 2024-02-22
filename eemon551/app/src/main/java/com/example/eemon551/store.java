package com.example.eemon551;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private FrameLayout back_screen;
    private TextView back_cost,back_cost2;
    private ImageView buy_back,buy_back2;
    private TextView title,title2,title3,title_cost,title_cost2,title_cost3,buy_title,buy_title_cost;
    private LinearLayout card_ano,title_ano,back_ano;

    private int collect_money;

    private int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // アクティビティの方向を縦向きに設定する
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_store);
        apiService = ApiClient.getApiService();
        gridLayout_1 = findViewById(R.id.card_layout);
        money =findViewById(R.id.money);
        store_screen = findViewById(R.id.store);
        //カード関連
        card_screen = findViewById(R.id.buy_card_screen);

        card_ano = findViewById(R.id.card_ano);

        //background関連
        back_screen = findViewById(R.id.buy_back_screen);
        back_cost = findViewById(R.id.back_cost1);
        back_cost2 = findViewById(R.id.back_cost2);
        buy_back = findViewById(R.id.buy_back);
        buy_back2 = findViewById(R.id.buy_back2);
        back_ano = findViewById(R.id.back_ano);
        //title関連
        title_screen = findViewById(R.id.buy_title_screen);
        title = findViewById(R.id.shop_title1);
        title2 = findViewById(R.id.shop_title2);
        title3 = findViewById(R.id.shop_title3);
        title_cost = findViewById(R.id.title_cost);
        title_cost2 = findViewById(R.id.title_cost2);
        title_cost3 = findViewById(R.id.title_cost3);
        buy_title = findViewById(R.id.buy_title);
        buy_title_cost = findViewById(R.id.buy_title_cost);
        title_ano = findViewById(R.id.title_ano);



        GetMoney();
//        for (int i = 0; i < 6; i++) {
//            fetchQuestions();
//        }
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


//    private void fetchQuestions() {
//
//            apiService.getAllQuestions().enqueue(new Callback<List<Question>>() {
//                @Override
//                public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
//
//                        Random random = new Random();
//                        int questionNo = random.nextInt(10);
//                    if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
//                        Question question = response.body().get(questionNo);
//
//                            if (!displayedQuestionIds.contains(question.getId()) && question.getLoc_id() >= 1 && question.getLoc_id() <= 6) {
//                                DisplayQuestion(question);
//                                displayedQuestionIds.add(question.getId());
//                            } else {
//                                //もし出題するidが出題済みidリストに存在したらもう一回ロード
//                                fetchQuestions();
//                            }
//                        }
//
//                }
//
//
//                @Override
//                public void onFailure(Call<List<Question>> call, Throwable t) {
//                    Log.e("API Request Failure", "Error: ", t);
//                }
//            });
//        }
//    private void DisplayQuestion(Question question) {
//
//            ImageView imageView = new ImageView(this);
//            String img = question.getCard().replace("\"", "").trim();
//            int imageResId = getResources().getIdentifier(img, "drawable", getPackageName());
//            Glide.with(this)
//                    .load(imageResId)
//                    .override(600, 400) // 画像の解像度を指定
//                    .into(imageView);
//            gridLayout_1.addView(imageView);
//
//    }

    public void buy_card(View view) {
        //int cost = Integer.parseInt(buy_card.getText().toString());
    }

    public void buy_back(View view) {
        int cost = Integer.parseInt(back_cost2.getText().toString());
        buy(cost,back_ano);
    }

    public void buy_title(View view) {
        int cost = Integer.parseInt(buy_title_cost.getText().toString());
        buy(cost,title_ano);

    }

    //購入
    private void buy(int cost, LinearLayout linearLayout) {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("UserId", 1);

        // 現在のユーザースコアを取得するAPIリクエストを想定
        apiService.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int currentMoney = response.body().getMoney();
                    Log.e("money", String.valueOf(currentMoney));
                    if(currentMoney < cost){
                        make_ano(linearLayout);
                    }
                    else {
                        //お金更新
                        int newMoney = currentMoney - cost;
                        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                        String name = prefs.getString("UserName", "test_user");
                        apiService.updateUserData(userId, new ApiService.UserUpdateRequest(name, newMoney)).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Log.d("UserDataUpdate", "ユーザーデータ更新成功");
                                } else {
                                    Log.e("UserDataUpdate", "ユーザーデータ更新失敗1: " + response.message());
                                }
                            }
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("UserDataUpdate", "ユーザーデータ更新失敗2", t);
                            }
                        });
                    }
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("UserMoneyFetch", "ユーザーのmoney取得失敗", t);
            }
        });
    }

    private void make_ano(LinearLayout l){
        // TextViewを新しく作成
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(android.view.Gravity.CENTER);
        textView.setText("お金が足りていません！！");
        textView.setTextSize(30);
        textView.setTextColor(0xFFFF0000); // 赤色

        // LinearLayoutにTextViewを追加
        l.addView(textView);
    }

    //購入画面からストアに戻る
    public void back_store(View view) {
        store_screen.setVisibility(View.VISIBLE);
        card_screen.setVisibility(View.GONE);
        title_screen.setVisibility(View.GONE);
        back_screen.setVisibility(View.GONE);
        card_ano.removeAllViews();
        title_ano.removeAllViews();
        back_ano.removeAllViews();
    }

    //card購入
    public void go_buy_card(View view){
        card_screen.setVisibility(View.GONE);
        store_screen.setVisibility(View.VISIBLE);
    }

    //title購入
    public void go_buy_title(View view) {
        buy_title_cost.setText(title_cost.getText());
        buy_title.setText(title.getText());
        title_screen.setVisibility(View.VISIBLE);
        store_screen.setVisibility(View.GONE);
    }

    public void go_buy_title2(View view) {
        buy_title_cost.setText(title_cost2.getText());
        buy_title.setText(title2.getText());
        title_screen.setVisibility(View.VISIBLE);
        store_screen.setVisibility(View.GONE);
    }

    public void go_buy_title3(View view) {
        buy_title_cost.setText(title_cost3.getText());
        buy_title.setText(title3.getText());
        title_screen.setVisibility(View.VISIBLE);
        store_screen.setVisibility(View.GONE);
    }

    //背景購入
    public void go_buy_back(View view){
        buy_back2.setImageResource(buy_back.getImageAlpha());
        back_cost2.setText(back_cost.getText());
        back_screen.setVisibility(View.GONE);
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