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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
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
    private List<Integer> all_QuestionList = new ArrayList<>();
    private FrameLayout store_screen;
    private FrameLayout card_screen;
    private FrameLayout title_screen;
    private FrameLayout back_screen;
    private GridLayout card_layout;
    private TextView back_cost,back_cost2;
    private ImageView buy_back,buy_back2;
    private TextView title,title2,title3,title_cost,title_cost2,title_cost3,buy_title,buy_title_cost;
    private LinearLayout card_ano,title_ano,back_ano;

    private int collect_money;
    private Integer randomValue;
    private int card_link;
    private ImageView card_1,card_2,card_3,card_4,buy_card;
    private TextView card_cost1,card_cost2,card_cost3,card_cost4,buy_card_cost;


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
        card_layout = findViewById(R.id.card_layout);
        card_ano = findViewById(R.id.card_ano);
        card_1 = findViewById(R.id.card_1);
        card_2 = findViewById(R.id.card_2);
        card_3 = findViewById(R.id.card_3);
        card_4 = findViewById(R.id.card_4);
        card_cost1 = findViewById(R.id.card_cost1);
        card_cost2 = findViewById(R.id.card_cost2);
        card_cost3 = findViewById(R.id.card_cost3);
        card_cost4 = findViewById(R.id.card_cost4);
        buy_card_cost = findViewById(R.id.buy_card_cost);
        buy_card = findViewById(R.id.buy_card);

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
        fetchQuestions();
    }
    private void fetchQuestions() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("UserId", 1);
        apiService.getUserQuestionData(userId).enqueue(new Callback<List<UserQuestionData>>() {
            @Override
            public void onResponse(Call<List<UserQuestionData>> call, Response<List<UserQuestionData>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    all_QuestionList.clear();
                    for (UserQuestionData u_q : response.body()) {
                        if(!u_q.get_cor() && u_q.getUser_data_id() == userId)
                            all_QuestionList.add(u_q.get_qes_id());
                    }
                    card_deco();
                }

            }
            @Override
            public void onFailure(Call<List<UserQuestionData>> call, Throwable t) {
                Log.e("API Request Failure", "Error: ", t);
            }
        });
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

    private int Res_cardId(){
        Log.d("API Request Failure", "random_be");
        randomValue = getRandomIntElement(all_QuestionList);
        Log.d("API Request Failure", "random_af");
        apiService.getQuestionById(randomValue).enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String img = response.body().getCard().replace("\"", "").trim();
                    card_link = getResources().getIdentifier(img, "drawable", getPackageName());
                }
            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {
                Log.e("API Request Failure", "Error: ", t);
            }
        });
        Log.d("API Request Failure", "all_af" + card_link);
        return card_link;
    }
    private int getRandomIntElement(List<Integer> set) {
        List<Integer> list = new ArrayList<>(set);
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }
    private void card_deco(){
        Log.d("API Request Failure", "card_deco_in");
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                Log.d("API Request Failure", "card_deco1");
                card_1.setImageResource(Res_cardId());
            } else if (i == 1) {
                card_2.setImageResource(Res_cardId());
            } else if (i == 2) {
                card_3.setImageResource(Res_cardId());
            } else if (i == 3) {
                card_4.setImageResource(Res_cardId());
            }
        }
    }

    public void buy_card(View view) {
        //int cost = Integer.parseInt(buy_card.getText().toString());
        int cost = Integer.parseInt(buy_card_cost.getText().toString());
        buy(cost,back_ano);
        back_store(view);
    }

    public void buy_back(View view) {
        int cost = Integer.parseInt(back_cost2.getText().toString());
        buy(cost,back_ano);
        back_store(view);
    }

    public void buy_title(View view) {
        int cost = Integer.parseInt(buy_title_cost.getText().toString());
        buy(cost,title_ano);
        back_store(view);
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
        });;
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
        buy_card_cost.setText(card_cost1.getText());
        //buy_card.setImageResource(buy_cards[0]);
        card_screen.setVisibility(View.GONE);
        store_screen.setVisibility(View.VISIBLE);
    }

    public void go_buy_card2(View view){
        buy_card_cost.setText(card_cost2.getText());
        //buy_card.setImageResource(buy_cards[1]);
        card_screen.setVisibility(View.GONE);
        store_screen.setVisibility(View.VISIBLE);
    }

    public void go_buy_card3(View view){
        buy_card_cost.setText(card_cost3.getText());
        //buy_card.setImageResource(buy_cards[2]);
        card_screen.setVisibility(View.GONE);
        store_screen.setVisibility(View.VISIBLE);
    }

    public void go_buy_card4(View view){
        buy_card_cost.setText(card_cost4.getText());
        //buy_card.setImageResource(buy_cards[4]);
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