package com.example.eemon551;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class game extends AppCompatActivity {

    private int currentQuestionLoc_id;
    private boolean currentQuestionIsKansai;

    private ApiService apiService;
    private TextView questionText;

    private ImageView seigoText;
    private TextView touka_loading;
    private ImageView questionImage;
    private ImageView huti;
    private boolean seigo = false;

    private FrameLayout kaisetu;

    private FrameLayout toi;

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

    private int search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // ApiServiceインスタンスを取得
        apiService = ApiClient.getApiService();

        // IntentからgenreIdとlocationIdを取得
        Intent intent = getIntent();
        genreId = intent.getIntExtra("genreId", 0);
        locationId = intent.getIntExtra("locationId", 0);

        loadQuestion();
    }

    private void loadQuestion() {
        Random random = new Random();
        int questionNo = random.nextInt(10); // 仮定する質問の数に応じて調整

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
                }

                @Override
                public void onFailure(Call<List<Question>> call, Throwable t) {
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
                }

                @Override
                public void onFailure(Call<List<Question>> call, Throwable t) {
                    Log.e("LocationFetch", "APIリクエスト失敗: ", t);
                }
            });
        }
    }

    private void DisplayQuestion(Question question) {
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
        loadLocationData(currentQuestionLoc_id);

        //出題済みidを格納
        displayedQuestionIds.add(question.getId());

        button_right.setVisibility(View.VISIBLE);
        button_left.setVisibility(View.VISIBLE);

        if (T_Q) {
            TrueQuestionIds.add(question.getId());
            T_Q = false;
        }
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

                    if (touka_loading.getVisibility() == View.GONE) {
                        setupButtonListeners(buttonLeft, buttonRight, currentQuestionIsKansai, locId);
                    }
                }
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                Log.e("LocationFetch", "locationデータ取得失敗: ", t);
            }
        });
    }


    //buttomと正誤判定
    private void setupButtonListeners(Button buttonLeft, Button buttonRight, Boolean currentQuestionIsKansai, int locId) {
        if (locationId == 0) {
            buttonLeft.setOnClickListener(view -> {
                if (currentQuestionIsKansai) {
                    // 正解の処理
                    Log.d("LocationFetch", "left true");
                    seigoText.setImageResource(R.drawable.wahhahhahha);
                    updateUserScore(10);
                    T_Q = true;
                } else {
                    // 不正解の処理
                    Log.d("LocationFetch", "left false");
                    seigoText.setImageResource(R.drawable.gaann);
                }
                seigoText.setVisibility(View.VISIBLE);
                seigo = true;
            });

            buttonRight.setOnClickListener(view -> {
                if (!currentQuestionIsKansai) {
                    Log.d("LocationFetch", "right true");
                    // 正解の処理
                    seigoText.setImageResource(R.drawable.wahhahhahha);
                    updateUserScore(10); // スコアを10加算する
                    T_Q = true;
                } else {
                    Log.d("LocationFetch", "right false");
                    // 不正解の処理
                    seigoText.setImageResource(R.drawable.gaann);
                }
                seigoText.setVisibility(View.VISIBLE);
                seigo = true;
                button_caver = false;
            });
        } else {
            Log.d("LocationFetch", "locationId" + locationId);
            Log.d("LocationFetch", "locid" + locId);
            buttonLeft.setOnClickListener(view -> {

                if (locationId == locId) {
                    Log.d("LocationFetch", "left true");
                    // 正解の処理
                    seigoText.setImageResource(R.drawable.wahhahhahha);
                    updateUserScore(10);
                    T_Q = true;
                } else {
                    Log.d("LocationFetch", "left false");
                    // 不正解の処理
                    seigoText.setImageResource(R.drawable.gaann);
                }
                seigoText.setVisibility(View.VISIBLE);
                seigo = true;
                button_caver = false;
            });

            buttonRight.setOnClickListener(view -> {
                if (locId != locationId) {
                    // 正解の処理
                    seigoText.setImageResource(R.drawable.wahhahhahha);
                    T_Q = true;
                } else {
                    // 不正解の処理
                    seigoText.setImageResource(R.drawable.gaann);
                    updateUserScore(10); // スコアを10加算する
                }
                seigoText.setVisibility(View.VISIBLE);
                seigo = true;
                button_caver = false;
            });
        }
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
                randomValue = getRandomElement(TrueQuestionIds);
                System.out.println("ランダムに選ばれた値: " + randomValue);
                kekka_money.setText("x" + String.valueOf(collect_money));
                SetUserQuestionData(randomValue, displayedQuestionIds);

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
            }
        }
    }

    public void onTap_next(View view) {
        questionNumber++;
        questionNumber_Str = "第" + questionNumber + "問";
        question_number.setText(questionNumber_Str);
        kaisetu.setVisibility(View.GONE);
        seigoText.setVisibility(View.GONE);
        button_right.setVisibility(View.GONE);
        button_left.setVisibility(View.GONE);

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
                    int newScore = currentScore + 10;
                    SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    String name = prefs.getString("UserName", "test_user");
                    apiService.updateUserData(userId, new ApiService.UserUpdateRequest(name, newScore)).enqueue(new Callback<Void>() {
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
                collect_money = collect_money + 10;
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("UserMoneyFetch", "ユーザーのmoney取得失敗", t);
            }
        });
    }

    private <T> T getRandomElement(Set<T> set) {
        List<T> list = new ArrayList<>(set);
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

    private void SetUserQuestionData(int randomValue, Set<Integer> displayedQuestionIds) {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("UserId", 1);

        UserQuestionData data_true = new UserQuestionData(true, randomValue, userId);
        apiService.insertUserQuestionData(data_true).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // 送信成功時の処理
                    Log.e("SetUserQuestionData", "Data successfully sent to the server. ");

                } else {
                    // サーバーからのレスポンスが成功でない場合の処理
                    Log.e("SetUserQuestionData", "Failed to send data. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // 通信失敗時の処理
                t.printStackTrace();
            }
        });
        for (Integer element : displayedQuestionIds) {
            UserQuestionData data_false = new UserQuestionData(false, element, userId);
            if (element != randomValue) {
                if (SearchQuestionData(userId, element) == 3 || SearchQuestionData(userId, element) == 4) {
                    apiService.insertUserQuestionData(data_false).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                // 送信成功時の処理
                                Log.e("SetUserQuestionData", "Data successfully sent to the server. ");

                            } else {
                                // サーバーからのレスポンスが成功でない場合の処理
                                Log.e("SetUserQuestionData", "Failed to send data. Response code: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            // 通信失敗時の処理
                            t.printStackTrace();
                        }
                    });
                }
            } else if (SearchQuestionData(userId, element) == 2) {
                break;
            } else if (SearchQuestionData(userId, element) == 1) {
                ApiService.UserQuestionDataUpdateRequest request = new ApiService.UserQuestionDataUpdateRequest(true, userId,element);
                apiService.updateUserQuestionData(request).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            // 更新成功時の処理。例えば、UIの更新など
                            Log.d("UpdateUseStatus", "Background use status updated successfully.");
                        } else {
                            // エラー処理
                            Log.e("UpdateUseStatus", "Failed to update the background use status.");
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // 通信失敗時の処理
                        Log.e("UpdateUseStatus", "API call failed.", t);
                    }
                });
            }
        }
    }

    private int SearchQuestionData(int userId,int qes_id) {
        apiService.getUserQuestionData(userId).enqueue(new Callback<List<UserQuestionData>>() {
            @Override
            public void onResponse(Call<List<UserQuestionData>> call, Response<List<UserQuestionData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<UserQuestionData> userQuestionData = response.body();
                    for (UserQuestionData data : userQuestionData) {
                        if(!data.get_cor()&&data.get_qes_id()==qes_id){
                            search = 1;//falseのデータがあったときPUT実行
                            break;
                        } else if (data.get_cor()&&data.get_qes_id()==qes_id) {
                            search = 2;//trueのデータがあった時ボーナス30コインでUserQuestionDataに追加しない
                            collect_money=collect_money+30;
                            kekka_money.setText(String.valueOf(collect_money));
                            break;
                            }
                        search = 3;//trueもfalseもなかった時POST実行
                    }
                }else{
                    search = 4;//何もデータがなかった時POST実行
                }
            }

            @Override
            public void onFailure(Call<List<UserQuestionData>> call, Throwable t) {
                Log.e("API Request Failure", "Error: ", t);
            }
        });
        return  search;
    }
}

