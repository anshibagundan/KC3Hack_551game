package com.example.eemon551;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
    private TextView touka_loading;
    private ImageView questionImage;
    private boolean seigo = false;

    private FrameLayout kaisetu;

    private FrameLayout toi;

    private TextView kaisetu_name;

    private ImageView kaisetu_img;
    private TextView kaisetu_text;
    private Button next;

    private TextView question_number;

    private int questionNumber = 1;

    private String questionNumber_Str = "第1問";
    private Set<Integer> displayedQuestionIds = new HashSet<>();

    private int genreId;
    private FrameLayout kekka;
    private LinearLayout card_over;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        questionText = findViewById(R.id.question_text);
        seigoText = findViewById(R.id.seigo);
        touka_loading = findViewById(R.id.touka_loading);
        questionImage = findViewById(R.id.Question_image);
        kaisetu = findViewById(R.id.kaisetu);
        toi = findViewById(R.id.toi);
        kaisetu_name = findViewById(R.id.kaisetu_name);
        kaisetu_img = findViewById(R.id.kaisetu_img);
        kaisetu_text = findViewById(R.id.kaisetu_text);
        next = findViewById(R.id.next);
        question_number = findViewById(R.id.question_number);
        kekka = findViewById(R.id.kekka);
        card_over = findViewById(R.id.card_over);


        // ApiServiceインスタンスを取得
        apiService = ApiClient.getApiService();

        // IntentからgenreIdとlocationIdを取得
        Intent intent = getIntent();
        genreId = intent.getIntExtra("genreId", 0);

        loadQuestion();
    }

    private void loadQuestion() {
        Log.e("genreId", "genreId"+genreId );
        Random random = new Random();
        int questionNo = random.nextInt(10); // 仮定する質問の数に応じて調整

        // APIリクエストを実行して質問をロード
        if(genreId == 0) {
            apiService.getAllQuestions().enqueue(new Callback<List<Question>>() {
                @Override
                public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                        // 質問データをセット
                        Question question = response.body().get(questionNo);
                        if (!displayedQuestionIds.contains(question.getId())) {
                            DisplayQuestion(question);
                        } else {
                            //もし出題するidが出題済みidリストに存在したらもう一回ロード
                            loadQuestion();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Question>> call, Throwable t) {
                    Log.e("LocationFetch", "APIリクエスト失敗: ", t);
                }
            });
        } else{
            apiService.getAllQuestions().enqueue(new Callback<List<Question>>() {
                @Override
                public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {

                        // 質問データをセット
                        questionText.setText("question Loading...");
                        //ここにloadingのフレーム入れて
                        Question question = response.body().get(questionNo);
                        if (!displayedQuestionIds.contains(question.getId())){
                            if((question.getGenre_id()==genreId)) {
                                DisplayQuestion(question);
                            }else{
                                loadQuestion();
                            }
                        } else {
                            //もし出題するidが出題済みidリストに存在したらもう一回ロード
                            loadQuestion();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Question>> call, Throwable t) {
                    Log.e("LocationFetch", "APIリクエスト失敗: ", t);
                }
            });
        }

    }

    private void DisplayQuestion(Question question){
        touka_loading.setVisibility(View.GONE);
        String name = question.getName();
        questionText.setText(name+ "が～？");
        kaisetu_name.setText(name);


        String img = question.getImg().replace("\"", "").trim();
        int resourceId = getResources().getIdentifier(img, "drawable", getPackageName());
        questionImage.setImageResource(resourceId);
        kaisetu_img.setImageResource(resourceId);

        String txt = question.getTxt();
        kaisetu_text.setText(txt);


        currentQuestionId = question.getId();
        currentQuestionLoc_id = question.getLoc_id();

        // locationデータを取得
        loadLocationData(currentQuestionLoc_id);

        //出題済みidを格納
        displayedQuestionIds.add(question.getId());

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
            kaisetu.setVisibility(View.VISIBLE);
            touka_loading.setVisibility(View.VISIBLE);
            toi.setVisibility(View.GONE);
            if (questionNumber>9){
                next.setText("結果へ");
            }
        }
    }

    public void onTap_next(View view){
        questionNumber ++;
        questionNumber_Str = "第" +questionNumber+"問";
        question_number.setText(questionNumber_Str);
        kaisetu.setVisibility(View.GONE);
        seigoText.setVisibility(View.GONE);
        if(questionNumber>10){
            kekka.setVisibility(View.VISIBLE);//あとで宝箱画面をVISIBLEにする
        }else{
            toi.setVisibility(View.VISIBLE);
            loadQuestion();
        }
    }

    public void onTap_takara(View view){
        card_over.setVisibility(View.VISIBLE);
    }

    public void onTap_takara_back(View view){
        card_over.setVisibility(View.GONE);
    }

    public void game_home(View view){
        Intent intent = new Intent(game.this, activity_home.class);
        startActivity(intent);
    }


    private void updateUserScore(int scoreToAdd) {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("UserId", 1);

        // 現在のユーザースコアを取得するAPIリクエストを想定
        apiService.getUserMoney(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int currentScore = response.body().getMoney();
                    Log.e("money", String.valueOf(currentScore));
                    int newScore = currentScore +10;
                    SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    String name = prefs.getString("UserName", "test_user");
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
