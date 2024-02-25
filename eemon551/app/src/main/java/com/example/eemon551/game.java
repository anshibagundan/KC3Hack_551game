package com.example.eemon551;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class game extends AppCompatActivity {

    private int currentQuestionLoc_id;
    private boolean currentQuestionIsKansai;

    private ApiService apiService;
    private TextView questionText;

    private ImageView seigoText;
    private LinearLayout touka_loading;
    private ImageView questionImage;
    private ImageView huti;
    private boolean seigo = false;

    private FrameLayout kaisetu;

    private FrameLayout toi;
    private int userbackgroundid;


    private TextView kaisetu_name;

    private ImageView kaisetu_img;
    private TextView kaisetu_text;
    private Button next;
    private Button button_left;
    private Button button_right;

    private TextView question_number;

    private int questionNumber = 1;

    private String questionNumber_Str = "第1問";
    private Set<Integer> displayedQuestionIds = new HashSet<>();
    private Set<Integer> TrueQuestionIds = new HashSet<>();

    private int genreId;
    private int locationId;
    private FrameLayout kekka;
    private LinearLayout card_over;
    private boolean button_caver = false;
    private Button finish;
    private ImageView takara;
    private boolean T_Q = false;
    private Integer randomValue;
    private ImageView get_card;
    private int collect_money;

    private TextView kekka_money;

    private ImageView image_3;
    private AtomicInteger pendingOverlays = new AtomicInteger(0);
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_game);

        questionText = findViewById(R.id.question_text);
        seigoText = findViewById(R.id.seigo);
        touka_loading = findViewById(R.id.touka_loading);
        questionImage = findViewById(R.id.Question_image);
        huti = findViewById(R.id.huti);
        kaisetu = findViewById(R.id.kaisetu);
        toi = findViewById(R.id.toi);
        kaisetu_name = findViewById(R.id.kaisetu_name);
        kaisetu_img = findViewById(R.id.kaisetu_img);
        kaisetu_text = findViewById(R.id.kaisetu_text);
        next = findViewById(R.id.next);
        button_left = findViewById(R.id.button_left);
        button_right = findViewById(R.id.button_right);
        question_number = findViewById(R.id.question_number);
        kekka = findViewById(R.id.kekka);
        card_over = findViewById(R.id.card_over);
        finish = findViewById(R.id.finish);
        takara = findViewById(R.id.takara);
        get_card = findViewById(R.id.get_card);
        kekka_money = findViewById(R.id.kekka_money);
        image_3 = findViewById(R.id.image_3);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100); // 進行状況の最大値を設定（例: 非同期タスクの総数に基づく）
        progressBar.setProgress(0); // 初期進行状況を0に設定

        // ApiServiceインスタンスを取得
        apiService = ApiClient.getApiService();

        // IntentからgenreIdとlocationIdを取得
        Intent intent = getIntent();
        genreId = intent.getIntExtra("genreId", 0);
        locationId = intent.getIntExtra("locationId", 0);

        loadQuestion();

        setBackgroundid(image_3);
    }

    private void loadQuestion() {
        incrementPendingAsyncTasks();
        Random random = new Random();
        int questionNo = random.nextInt(156); // 仮定する質問の数に応じて調整

        // APIリクエストを実行して質問をロード
        if (genreId == 0) {
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
                    decrementPendingAsyncTasks();
                }

                @Override
                public void onFailure(Call<List<Question>> call, Throwable t) {
                    decrementPendingAsyncTasks();
                    Log.e("LocationFetch", "APIリクエスト失敗: ", t);
                }
            });
        } else {
            apiService.getAllQuestions().enqueue(new Callback<List<Question>>() {
                @Override
                public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {

                        // 質問データをセット
                        questionText.setText("question Loading...");
                        //ここにloadingのフレーム入れて
                        Question question = response.body().get(questionNo);
                        if (!displayedQuestionIds.contains(question.getId())) {
                            if ((question.getGenre_id() == genreId)) {
                                DisplayQuestion(question);
                            } else {
                                loadQuestion();
                            }
                        } else {
                            //もし出題するidが出題済みidリストに存在したらもう一回ロード
                            loadQuestion();
                        }
                    }
                    decrementPendingAsyncTasks();
                }

                @Override
                public void onFailure(Call<List<Question>> call, Throwable t) {
                    decrementPendingAsyncTasks();
                    Log.e("LocationFetch", "APIリクエスト失敗: ", t);
                }
            });
        }
    }

    private void DisplayQuestion(Question question) {
        incrementPendingAsyncTasks();
        touka_loading.setVisibility(View.GONE);
        String name = question.getName();
        questionText.setText(name + "が～？");
        kaisetu_name.setText(name);


        String img = question.getImg().replace("\"", "").trim();
        int resourceId = getResources().getIdentifier(img, "drawable", getPackageName());
        questionImage.setImageResource(resourceId);
        kaisetu_img.setImageResource(resourceId);

        String txt = question.getTxt();
        kaisetu_text.setText(txt);

        currentQuestionLoc_id = question.getLoc_id();

        // locationデータを取得
        loadLocationData(currentQuestionLoc_id,question);

        Log.e("T_Q", "3" + T_Q);
        if (T_Q) {
//            TrueQuestionIds.add(question.getId());
            T_Q = false;
        }
        //出題済みidを格納
        displayedQuestionIds.add(question.getId());
        decrementPendingAsyncTasks();
        button_right.setVisibility(View.VISIBLE);
        button_left.setVisibility(View.VISIBLE);


    }

    private void loadLocationData(int locId, Question question) {
        incrementPendingAsyncTasks();
        apiService.getLocationById(locId).enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentQuestionIsKansai = response.body().isIskansai();
                    Log.e("LocationFetch", String.valueOf(currentQuestionIsKansai));
                    Button buttonLeft = findViewById(R.id.button_left);
                    Button buttonRight = findViewById(R.id.button_right);

                    if (touka_loading.getVisibility() == View.GONE) {
                        setupButtonListeners(buttonLeft, buttonRight, currentQuestionIsKansai, locId, question);
                    }
                    decrementPendingAsyncTasks();
                    Log.e("T_Q", "2" + T_Q);
                }
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                decrementPendingAsyncTasks();
                Log.e("LocationFetch", "locationデータ取得失敗: ", t);
            }
        });
    }


    //buttomと正誤判定
    private void setupButtonListeners(Button buttonLeft, Button buttonRight, Boolean currentQuestionIsKansai, int locId, Question question) {
        incrementPendingAsyncTasks();
        if (locationId == 0) {
            buttonLeft.setOnClickListener(view -> {
                if (currentQuestionIsKansai) {
                    // 正解の処理
                    Log.d("LocationFetch", "left true");
                    seigoText.setImageResource(R.drawable.wahhahhahha);
                    updateUserScore(10);
                    T_Q = true;
                    apiService.getQuestionById(question.getId()).enqueue(new Callback<Question>() {
                        @Override
                        public void onResponse(Call<Question> call, Response<Question> response) {
                            if (response.isSuccessful()&&response.body()!=null){
                                apiService.getLocationById(response.body().getLoc_id()).enqueue(new Callback<Location>() {
                                    @Override
                                    public void onResponse(Call<Location> call, Response<Location> response) {
                                        if (response.isSuccessful()&&response.body()!=null){
                                            if (response.body().isIskansai()){
                                                TrueQuestionIds.add(question.getId());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Location> call, Throwable t) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<Question> call, Throwable t) {

                        }
                    });

                } else {
                    // 不正解の処理
                    Log.d("LocationFetch", "left false");
                    seigoText.setImageResource(R.drawable.gaann);
                }
                seigoText.setVisibility(View.VISIBLE);
                seigo = true;
                button_right.setVisibility(View.GONE);
                button_left.setVisibility(View.GONE);
            });

            buttonRight.setOnClickListener(view -> {
                if (!currentQuestionIsKansai) {
                    Log.d("LocationFetch", "right true");
                    // 正解の処理
                    seigoText.setImageResource(R.drawable.wahhahhahha);
                    updateUserScore(10); // スコアを10加算する
                } else {
                    Log.d("LocationFetch", "right false");
                    // 不正解の処理
                    seigoText.setImageResource(R.drawable.gaann);
                    apiService.getQuestionById(question.getId()).enqueue(new Callback<Question>() {
                        @Override
                        public void onResponse(Call<Question> call, Response<Question> response) {
                            if (response.isSuccessful()&&response.body()!=null){
                                apiService.getLocationById(response.body().getLoc_id()).enqueue(new Callback<Location>() {
                                    @Override
                                    public void onResponse(Call<Location> call, Response<Location> response) {
                                        if (response.isSuccessful()&&response.body()!=null){
                                            if (response.body().isIskansai()){
                                                TrueQuestionIds.add(question.getId());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Location> call, Throwable t) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<Question> call, Throwable t) {

                        }
                    });
                }
                seigoText.setVisibility(View.VISIBLE);
                seigo = true;
                button_caver = false;
                button_right.setVisibility(View.GONE);
                button_left.setVisibility(View.GONE);
            });
            Log.d("kansai", "TrueQuestionIds" + TrueQuestionIds);
        } else {
            Log.d("LocationFetch", "locationId" + locationId);
            Log.d("LocationFetch", "locid" + locId);
            buttonLeft.setOnClickListener(view -> {

                if (locationId == locId) {
                    Log.d("LocationFetch", "left true");
                    // 正解の処理
                    seigoText.setImageResource(R.drawable.wahhahhahha);
                    updateUserScore(30);
                    T_Q = true;
                    apiService.getQuestionById(question.getId()).enqueue(new Callback<Question>() {
                        @Override
                        public void onResponse(Call<Question> call, Response<Question> response) {
                            if (response.isSuccessful()&&response.body()!=null){
                                apiService.getLocationById(response.body().getLoc_id()).enqueue(new Callback<Location>() {
                                    @Override
                                    public void onResponse(Call<Location> call, Response<Location> response) {
                                        if (response.isSuccessful()&&response.body()!=null){
                                            if (response.body().isIskansai()){
                                                TrueQuestionIds.add(question.getId());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Location> call, Throwable t) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<Question> call, Throwable t) {

                        }
                    });
                } else {
                    Log.d("LocationFetch", "left false");
                    // 不正解の処理
                    seigoText.setImageResource(R.drawable.gaann);
                    apiService.getQuestionById(question.getId()).enqueue(new Callback<Question>() {
                        @Override
                        public void onResponse(Call<Question> call, Response<Question> response) {
                            if (response.isSuccessful()&&response.body()!=null){
                                apiService.getLocationById(response.body().getLoc_id()).enqueue(new Callback<Location>() {
                                    @Override
                                    public void onResponse(Call<Location> call, Response<Location> response) {
                                        if (response.isSuccessful()&&response.body()!=null){
                                            if (response.body().isIskansai()){
                                                TrueQuestionIds.add(question.getId());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Location> call, Throwable t) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<Question> call, Throwable t) {

                        }
                    });
                }
                seigoText.setVisibility(View.VISIBLE);
                seigo = true;
                button_caver = false;
                button_right.setVisibility(View.GONE);
                button_left.setVisibility(View.GONE);
            });

            buttonRight.setOnClickListener(view -> {
                if (locId != locationId) {
                    // 正解の処理
                    seigoText.setImageResource(R.drawable.wahhahhahha);
                    updateUserScore(10); // スコアを10加算する
                    apiService.getQuestionById(question.getId()).enqueue(new Callback<Question>() {
                        @Override
                        public void onResponse(Call<Question> call, Response<Question> response) {
                            if (response.isSuccessful()&&response.body()!=null){
                                apiService.getLocationById(response.body().getLoc_id()).enqueue(new Callback<Location>() {
                                    @Override
                                    public void onResponse(Call<Location> call, Response<Location> response) {
                                        if (response.isSuccessful()&&response.body()!=null){
                                            if (response.body().isIskansai()){
                                                TrueQuestionIds.add(question.getId());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Location> call, Throwable t) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<Question> call, Throwable t) {

                        }
                    });
                } else {
                    // 不正解の処理
                    seigoText.setImageResource(R.drawable.gaann);
                    apiService.getQuestionById(question.getId()).enqueue(new Callback<Question>() {
                        @Override
                        public void onResponse(Call<Question> call, Response<Question> response) {
                            if (response.isSuccessful()&&response.body()!=null){
                                apiService.getLocationById(response.body().getLoc_id()).enqueue(new Callback<Location>() {
                                    @Override
                                    public void onResponse(Call<Location> call, Response<Location> response) {
                                        if (response.isSuccessful()&&response.body()!=null){
                                            if (response.body().isIskansai()){
                                                TrueQuestionIds.add(question.getId());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Location> call, Throwable t) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<Question> call, Throwable t) {

                        }
                    });
                }
                seigoText.setVisibility(View.VISIBLE);
                seigo = true;
                button_caver = false;
                button_right.setVisibility(View.GONE);
                button_left.setVisibility(View.GONE);
            });
            Log.d("kansai", "TrueQuestionIds" + TrueQuestionIds);
        }
        Log.e("T_Q", "1" + T_Q);
        decrementPendingAsyncTasks();
    }


    public void on(View view) {

    }

    public void onTap(View view) {

        if (seigo) {
            kaisetu.setVisibility(View.VISIBLE);
            touka_loading.setVisibility(View.VISIBLE);
            toi.setVisibility(View.GONE);

            if (questionNumber > 9) {
                next.setText("結果へ");
                System.out.println("ランダム前 空前");
                if (!TrueQuestionIds.isEmpty()) {
                    System.out.println("ランダム前 空後");
                    randomValue = getRandomElement(TrueQuestionIds);
                    System.out.println("ランダムに選ばれた値: " + randomValue);
                    SetUserQuestionData(randomValue, displayedQuestionIds);
                    runOnUiThread(() -> kekka_money.setText("x　???\n宝箱Tap！"));

                    apiService.getQuestionById(randomValue).enqueue(new Callback<Question>() {
                        @Override
                        public void onResponse(Call<Question> call, Response<Question> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                String img = response.body().getCard().replace("\"", "").trim();
                                int card_link = getResources().getIdentifier(img, "drawable", getPackageName());
                                get_card.setImageResource(card_link);
                            }
                        }

                        @Override
                        public void onFailure(Call<Question> call, Throwable t) {
                            Log.e("API Request Failure", "Error: ", t);
                        }
                    });
                }else{
                    System.out.println("ランダムじゃなくてこっち？");
                    get_card.setImageResource(R.drawable.gaann);
                }

            }
        }
    }

    public void onTap_next(View view) {
        questionNumber++;
        questionNumber_Str = "第" + questionNumber + "問";
        question_number.setText(questionNumber_Str);
        kaisetu.setVisibility(View.GONE);
        seigoText.setVisibility(View.GONE);


        if (questionNumber > 10) {
            kekka.setVisibility(View.VISIBLE);//あとで宝箱画面をVISIBLEにする
        } else {
            toi.setVisibility(View.VISIBLE);
            loadQuestion();
        }
        button_caver = false;
    }

    public void onTap_takara(View view) {
        button_caver = false;

        card_over.setVisibility(View.VISIBLE);
    }

    public void onTap_takara_back(View view) {
        card_over.setVisibility(View.GONE);
        finish.setVisibility(View.VISIBLE);
        runOnUiThread(() -> kekka_money.setText("x"+String.valueOf(collect_money)));
        if (!TrueQuestionIds.isEmpty()) {
            apiService.getQuestionById(randomValue).enqueue(new Callback<Question>() {
                @Override
                public void onResponse(Call<Question> call, Response<Question> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String img = response.body().getCard().replace("\"", "").trim();
                        int card_link = getResources().getIdentifier(img, "drawable", getPackageName());
                        takara.setImageResource(card_link);
                    }
                }

                @Override
                public void onFailure(Call<Question> call, Throwable t) {
                    Log.e("API Request Failure", "Error: ", t);
                }
            });
        }else {
            takara.setImageResource(R.drawable.gaann);
        }

        button_caver = true;
    }

    public void game_home(View view) {
        if (button_caver) {
            Intent intent = new Intent(game.this, activity_home.class);
            startActivity(intent);
        }
    }


    private void updateUserScore(int scoreToAdd) {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("UserId", 1);

        // 現在のユーザースコアを取得するAPIリクエストを想定
        apiService.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int currentScore = response.body().getMoney();
                    Log.e("money", String.valueOf(currentScore));
                    int newScore = currentScore + 30;
                    collect_money = collect_money + 30;
                    Log.e("money", "collect_money"+collect_money);
                    apiService.getUser(userId).enqueue((new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                String name = response.body().getName();
                                apiService.updateUserData(userId, new ApiService.UserUpdateRequest(name, newScore)).enqueue(new Callback<Void>() {
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

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    }));
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("UserMoneyFetch", "ユーザーのmoney取得失敗", t);
            }
        });
    }

    private <T> T getRandomElement(Set<T> set) {
        incrementPendingAsyncTasks();
        List<T> list = new ArrayList<>(set);
        Random random = new Random();
        decrementPendingAsyncTasks();
        return list.get(random.nextInt(list.size()));

    }

    private void SetUserQuestionData(int randomValue, Set<Integer> displayedQuestionIds) {
        incrementPendingAsyncTasks();
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("UserId", 1);

        // 正解の質問データを処理
        SearchQuestionData(userId, randomValue, searchResult -> {
            Log.e("UpdateQuestionData", "searchResult"+searchResult);

            if (searchResult == 2) {
                collect_money += 300;
                updateUserScore(300);
            } else if (searchResult == 1) {
                Log.d("UpdateQuestionData", "user_data_id"+ userId);
                Log.d("UpdateQuestionData", "qes_id"+ randomValue);
                UserQuestionDataUpdateRequest request = new UserQuestionDataUpdateRequest(true ,userId,randomValue);
                apiService.updateUserQuestionData(request).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            decrementPendingAsyncTasks();
                            Log.d("UpdateQuestionData", "Question data updated successfully.");
                        } else {
                            decrementPendingAsyncTasks();
                            Log.e("UpdateQuestionData", "Failed to update question data. Response code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        decrementPendingAsyncTasks();
                        Log.e("UpdateQuestionData", "API call failed.", t);
                    }
                });
            } else if (searchResult == 3 || searchResult == 4) {
                UserQuestionData data = new UserQuestionData(true, randomValue, userId);
                apiService.insertUserQuestionData(data).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            decrementPendingAsyncTasks();
                            Log.d("InsertQuestionData", "Question data inserted successfully.");
                        } else {
                            decrementPendingAsyncTasks();
                            Log.e("InsertQuestionData", "Failed to insert question data. Response code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        decrementPendingAsyncTasks();
                        Log.e("InsertQuestionData", "API call failed.", t);
                    }
                });
            }
        });

        // 表示されたが選択されなかった質問データを処理
        for (Integer questionId : TrueQuestionIds) {
            if (!questionId.equals(randomValue)) {
                SearchQuestionData(userId, questionId, searchResult -> {
                    if (searchResult == 3 || searchResult == 4) {
                        UserQuestionData data = new UserQuestionData(false, questionId, userId);
                        apiService.insertUserQuestionData(data).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    decrementPendingAsyncTasks();
                                    Log.d("InsertQuestionData", "Question data inserted successfully for non-selected question.");
                                } else {
                                    decrementPendingAsyncTasks();
                                    Log.e("InsertQuestionData", "Failed to insert question data for non-selected question. Response code: " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                decrementPendingAsyncTasks();
                                Log.e("InsertQuestionData", "API call failed for non-selected question.", t);
                            }
                        });
                    }
                });
            }
        }
    }


    interface SearchQuestionDataCallback {
        void onResult(int searchResult);
    }

    private void SearchQuestionData(int userId, int qes_id, SearchQuestionDataCallback callback) {
        incrementPendingAsyncTasks();
        apiService.getUserQuestionData(userId).enqueue(new Callback<List<UserQuestionData>>() {
            @Override
            public void onResponse(Call<List<UserQuestionData>> call, Response<List<UserQuestionData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<UserQuestionData> userQuestionData = response.body();
                    for (UserQuestionData data : userQuestionData) {
                        if (!data.get_cor() && data.get_qes_id() == qes_id) {
                            callback.onResult(1); // falseのデータがあった場合
                            return;
                        } else if (data.get_cor() && data.get_qes_id() == qes_id) {
                            callback.onResult(2); // trueのデータがあった場合
                            return;
                        }
                    }
                    callback.onResult(3); // trueもfalseもなかった場合
                } else {
                    callback.onResult(4); // データがなかった場合
                }
                decrementPendingAsyncTasks();
            }

            @Override
            public void onFailure(Call<List<UserQuestionData>> call, Throwable t) {
                Log.e("API Request Failure", "Error: ", t);
                callback.onResult(0); // 通信失敗
            }
        });
    }
    private void setBackgroundid(ImageView background_image){
        //DBから称号を取ってくる titleに格納
        incrementPendingAsyncTasks();

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("UserId", 1);
        apiService.getUserBackgrounds(userId).enqueue(new Callback<List<UserBackground>>() {
            @Override
            public void onResponse(Call<List<UserBackground>> call, Response<List<UserBackground>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("UserBackgroundId", "userId: " + userId);
                    for(UserBackground data:response.body()){
                        if (data.getUse()&&data.getUser_data_id()==userId) {
                            userbackgroundid = data.getBackground_id();
                            setBackground(userbackgroundid, background_image);
                            Log.e("UserBackgroundId", "userbackgroundid: " + userbackgroundid);
                            Log.e("UserBackgroundId", "user_data_id: " + data.getUser_data_id());
                            decrementPendingAsyncTasks();
                            break;
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserBackground>> call, Throwable t) {
                decrementPendingAsyncTasks();
                Log.e("UserBackgroundId", "API call failed: " + t.getMessage());
            }
        });


    }
    private void setBackground(int Userbackgroundid, ImageView background_image){
        incrementPendingAsyncTasks();

        apiService.getBackgrounds(Userbackgroundid).enqueue(new Callback<background>() {
            @Override
            public void onResponse(Call<background> call, Response<background> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String img = response.body().getImg().replace("\"", "").trim();
                    Log.e("UserBackgroundId", "img: " + img);
                    int resourceId = getResources().getIdentifier(img, "drawable", getPackageName());
                    if (resourceId != 0) { // リソースIDが有効な場合
                        // UIスレッド上で背景を設定
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                background_image.setBackgroundResource(resourceId);
                                decrementPendingAsyncTasks();
                            }
                        });

                    } else {
                        String img2 = "img".replace("\"", "").trim();
                        background_image.setImageResource(getResources().getIdentifier(img2, "drawable", getPackageName()));
                        decrementPendingAsyncTasks();
                        Log.e("UserBackgroundId", "Resource ID not found for: " + img);
                    }

                    Log.e("UserBackgroundId", "Resource ID: " + resourceId);

                }
            }
            @Override
            public void onFailure(Call<background> call, Throwable t) {
                decrementPendingAsyncTasks();
                Log.e("UserBackgroundId", "API call failed: " + t.getMessage());
            }
        });
    }

    private int totalAsyncTasks = 0; // 非同期タスクの総数
    private int completedAsyncTasks = 0; // 完了した非同期タスクの数

    private void updateProgressBar() {
        runOnUiThread(() -> {
            int progress = (int) (((double) completedAsyncTasks / totalAsyncTasks) * 100);
            progressBar.setProgress(progress);
        });
    }

    private void incrementPendingAsyncTasks() {
        synchronized (this) {
            totalAsyncTasks++;
            updateProgressBar();
        }
    }

    private void decrementPendingAsyncTasks() {
        synchronized (this) {
            completedAsyncTasks++;
            updateProgressBar();
            if (completedAsyncTasks == totalAsyncTasks) {
                runOnUiThread(() -> toi.setVisibility(View.VISIBLE));
            }
        }
    }

}

