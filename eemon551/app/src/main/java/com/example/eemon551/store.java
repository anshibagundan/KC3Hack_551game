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
import android.widget.ProgressBar;
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
    private List<Integer> all_TitlesList = new ArrayList<>();
    private List<Integer> all_BackgroundsList = new ArrayList<>();
    private List<Integer> randomvalueList = new ArrayList<>();
    private List<Integer> cardList = new ArrayList<>();
    private List<Integer> titleList = new ArrayList<>();
    private List<Integer> backgroundList = new ArrayList<>();

    private int TitleNum = 0;
    private int questionId = 0;
    private int backgroundId = 0;


    private FrameLayout store_screen;
    private FrameLayout card_screen;
    private FrameLayout title_screen;
    private FrameLayout back_screen;
    private GridLayout card_layout;
    private TextView back_cost, back_cost2;
    private ImageView buy_back, buy_back2;
    private TextView title, title2, title3, title_cost, title_cost2, title_cost3, buy_title, buy_title_cost;
    private LinearLayout card_ano, title_ano, back_ano;

    private String back_img = "card_backside".replace("\"", "").trim();


    private int collect_money;
    private Integer randomValue;
    private ImageView card_1, card_2, card_3, card_4, buy_card;
    private TextView card_cost1, card_cost2, card_cost3, card_cost4, buy_card_cost;

    private TextView cardname1, cardname2, cardname3, cardname4;

    private int back_res, card_res1,card_res2,card_res3,card_res4;
    private LinearLayout card_table_1;
    private LinearLayout card_table_2;
    private LinearLayout card_table_3;
    private LinearLayout card_table_4;
    private LinearLayout title_table_1;
    private LinearLayout title_table_2;
    private LinearLayout title_table_3;
    private int userbackgroundid;
    private ImageView image_3;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // アクティビティの方向を縦向きに設定する
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_store);
        apiService = ApiClient.getApiService();
        gridLayout_1 = findViewById(R.id.card_layout);
        money = findViewById(R.id.money);
        store_screen = findViewById(R.id.store);
        image_3 = findViewById(R.id.image_3);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100); // 進行状況の最大値を設定（例: 非同期タスクの総数に基づく）
        progressBar.setProgress(0); // 初期進行状況を0に設定

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
        cardname1 = findViewById(R.id.cardname1);
        cardname2 = findViewById(R.id.cardname2);
        cardname3 = findViewById(R.id.cardname3);
        cardname4 = findViewById(R.id.cardname4);
        card_table_1 = findViewById(R.id.card_table_1);
        card_table_2 = findViewById(R.id.card_table_2);
        card_table_3 = findViewById(R.id.card_table_3);
        card_table_4 = findViewById(R.id.card_table_4);

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
        title_table_1 = findViewById(R.id.title1);
        title_table_2 = findViewById(R.id.title2);
        title_table_3 = findViewById(R.id.title3);

        GetMoney();
        fetchQuestions();
        fetchTitles();
        fetchBackgrounds();
        setBackgroundid(image_3);


    }

    private void GetMoney() {
        incrementPendingAsyncTasks();
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("UserId", 1);
        apiService.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int currentScore = response.body().getMoney();
                    money.setText(String.valueOf(currentScore));
                    decrementPendingAsyncTasks();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                decrementPendingAsyncTasks();
                Log.e("API Request Failure", "Error: ", t);
            }
        });
    }

    private void fetchQuestions() {
        incrementPendingAsyncTasks();
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("UserId", 1);

        apiService.getUserQuestionData(userId).enqueue(new Callback<List<UserQuestionData>>() {
            @Override
            public void onResponse(Call<List<UserQuestionData>> call, Response<List<UserQuestionData>> response) {
                Log.e("API Request Failure2", "userId " + userId);
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    all_QuestionList.clear();
                    randomvalueList.clear();
                    cardList.clear();
                    for (UserQuestionData u_q : response.body()) {
                        if (!u_q.get_cor() && u_q.getUser_data_id() == userId) {
                            if(all_QuestionList.size()<5) {
                                all_QuestionList.add(u_q.get_qes_id());
                            }
                            Log.e("API Request Failure2", "allQuestionList " + all_QuestionList);
                        }

                    }
                    decrementPendingAsyncTasks();
                    Log.e("kansai", "all_QuestionList "+all_QuestionList);
                    Res_cardId();
                }

            }

            @Override
            public void onFailure(Call<List<UserQuestionData>> call, Throwable t) {
                decrementPendingAsyncTasks();
                Log.e("API Request Failure", "Error: ", t);
            }
        });
    }


    private void Res_cardId() {
        incrementPendingAsyncTasks();
        Log.d("API Request Failure", "random_be");
        for (int i = 0; i < 4; i++) {
            randomValue = getRandomIntElement(all_QuestionList);
            randomvalueList.add(randomValue);
        }

        if(randomvalueList.get(0) == -1) {
            card_res1 = getResources().getIdentifier(back_img, "drawable", getPackageName());
            card_1.setImageResource(card_res1);
            card_cost1.setText("完売");
            card_table_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Do nothing (none)
                }
            });
        }else {

            apiService.getQuestionById(randomvalueList.get(0)).enqueue(new Callback<Question>() {
                @Override
                public void onResponse(Call<Question> call, Response<Question> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String img = response.body().getCard().replace("\"", "").trim();
                        card_res1 = getResources().getIdentifier(img, "drawable", getPackageName());
                        int cost = response.body().getRare() * 300;
                        String name = response.body().getName();
                        cardname1.setText(name);
                        cardList.add(response.body().getId());
                        card_1.setImageResource(card_res1);
                        card_cost1.setText(String.valueOf(cost));
                    } else {
                        Log.d("API Request Failure", "エラー");
                    }
                }


                @Override
                public void onFailure(Call<Question> call, Throwable t) {
                    Log.e("API Request Failure", "Error: ", t);
                }
            });
        }
        if(randomvalueList.get(1) == -1) {
            card_res2 = getResources().getIdentifier(back_img, "drawable", getPackageName());
            card_2.setImageResource(card_res2);
            card_cost2.setText("完売");
            card_table_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Do nothing (none)
                }
            });
        }else {
            apiService.getQuestionById(randomvalueList.get(1)).enqueue(new Callback<Question>() {
                @Override
                public void onResponse(Call<Question> call, Response<Question> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String img = response.body().getCard().replace("\"", "").trim();
                        int cost = response.body().getRare() * 300;
                        card_res2 = getResources().getIdentifier(img, "drawable", getPackageName());
                        String name = response.body().getName();
                        cardList.add(response.body().getId());
                        cardname2.setText(name);
                        card_2.setImageResource(card_res2);
                        card_cost2.setText(String.valueOf(cost));
                    }
                }


                @Override
                public void onFailure(Call<Question> call, Throwable t) {
                    Log.e("API Request Failure", "Error: ", t);
                }
            });
        }
        if(randomvalueList.get(2) == -1) {
            card_res3 = getResources().getIdentifier(back_img, "drawable", getPackageName());
            card_3.setImageResource(card_res3);
            card_cost3.setText("完売");
            card_table_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Do nothing (none)
                }
            });
        }else {
            apiService.getQuestionById(randomvalueList.get(2)).enqueue(new Callback<Question>() {
                @Override
                public void onResponse(Call<Question> call, Response<Question> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String img = response.body().getCard().replace("\"", "").trim();
                        int cost = response.body().getRare() * 300;
                        card_res3 = getResources().getIdentifier(img, "drawable", getPackageName());
                        card_3.setImageResource(card_res3);
                        String name = response.body().getName();
                        cardList.add(response.body().getId());
                        cardname3.setText(name);
                        card_cost3.setText(String.valueOf(cost));
                    }
                }


                @Override
                public void onFailure(Call<Question> call, Throwable t) {
                    Log.e("API Request Failure", "Error: ", t);
                }
            });
        }
        if(randomvalueList.get(3) == -1) {
            card_res4 = getResources().getIdentifier(back_img, "drawable", getPackageName());
            card_4.setImageResource(card_res4);
            card_cost4.setText("完売");
            card_table_4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Do nothing (none)
                }
            });
        }else {
            apiService.getQuestionById(randomvalueList.get(3)).enqueue(new Callback<Question>() {
                @Override
                public void onResponse(Call<Question> call, Response<Question> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String img = response.body().getCard().replace("\"", "").trim();
                        int cost = response.body().getRare() * 300;
                        card_res4 = getResources().getIdentifier(img, "drawable", getPackageName());
                        card_4.setImageResource(card_res4);
                        String name = response.body().getName();
                        cardList.add(response.body().getId());
                        cardname4.setText(name);
                        card_cost4.setText(String.valueOf(cost));
                    }
                }


                @Override
                public void onFailure(Call<Question> call, Throwable t) {
                    Log.e("API Request Failure", "Error: ", t);
                }
            });
            decrementPendingAsyncTasks();
        }
    }


    private void fetchTitles() {
        incrementPendingAsyncTasks();
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("UserId", 1);

        apiService.getUserTitles(userId).enqueue(new Callback<List<UserTitles>>() {
            @Override
            public void onResponse(Call<List<UserTitles>> call, Response<List<UserTitles>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    all_TitlesList.clear();
                    titleList.clear();
                    randomvalueList.clear();
                    for (UserTitles title : response.body()) {
                        if (!title.getisOwn() && title.getUser_data_id() == userId)
                            all_TitlesList.add(title.getTitle_id());
                    }
                    decrementPendingAsyncTasks();
                    Res_titleId();
                }

            }

            @Override
            public void onFailure(Call<List<UserTitles>> call, Throwable t) {
                Log.e("API Request Failure", "Error: ", t);
                decrementPendingAsyncTasks();
            }
        });
    }


    private void Res_titleId() {
        incrementPendingAsyncTasks();
        for (int i = 0; i < 3; i++) {
            randomValue = getRandomIntElement(all_TitlesList);
            randomvalueList.add(randomValue);
        }
        if(randomvalueList.get(0) == -1) {
            title.setText("称号売り切れ！");
            title_cost.setText("完売");
            title_table_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Do nothing (none)
                }
            });
        }else {
            Log.e("randomvalueList", "randomvalueList: "+ randomvalueList);

            apiService.getTitle(randomvalueList.get(0)).enqueue(new Callback<Titles>() {
                @Override
                public void onResponse(Call<Titles> call, Response<Titles> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        int cost = response.body().getRare() * 1000;
                        String name = response.body().getName();
                        titleList.add(response.body().getId());
                        title.setText(name);
                        title_cost.setText(String.valueOf(cost));
                    }
                }


                @Override
                public void onFailure(Call<Titles> call, Throwable t) {
                    Log.e("API Request Failure", "Error: ", t);
                }
            });
        }
        if(randomvalueList.get(1) == -1) {
            title.setText("称号売り切れ！");
            title_cost2.setText("完売");
            title_table_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Do nothing (none)
                }
            });
        }else {
            apiService.getTitle(randomvalueList.get(1)).enqueue(new Callback<Titles>() {
                @Override
                public void onResponse(Call<Titles> call, Response<Titles> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        int cost = response.body().getRare() * 1000;
                        String name = response.body().getName();
                        titleList.add(response.body().getId());
                        title2.setText(name);
                        title_cost2.setText(String.valueOf(cost));
                    }
                }


                @Override
                public void onFailure(Call<Titles> call, Throwable t) {
                    Log.e("API Request Failure", "Error: ", t);
                }
            });
        }
        if(randomvalueList.get(2) == -1) {
            title.setText("称号売り切れ！");
            title_cost3.setText("完売！");
            title_table_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Do nothing (none)
                }
            });
        }else {

            apiService.getTitle(randomvalueList.get(2)).enqueue(new Callback<Titles>() {
                @Override
                public void onResponse(Call<Titles> call, Response<Titles> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        int cost = response.body().getRare() * 1000;
                        String name = response.body().getName();
                        titleList.add(response.body().getId());
                        title3.setText(name);
                        title_cost3.setText(String.valueOf(cost));
                    }
                }


                @Override
                public void onFailure(Call<Titles> call, Throwable t) {
                    Log.e("API Request Failure", "Error: ", t);
                }
            });

        }
        decrementPendingAsyncTasks();
    }

    private void fetchBackgrounds() {
        incrementPendingAsyncTasks();
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("UserId", 1);

        apiService.getUserBackgrounds(userId).enqueue(new Callback<List<UserBackground>>() {
            @Override
            public void onResponse(Call<List<UserBackground>> call, Response<List<UserBackground>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    all_BackgroundsList.clear();
                    backgroundList.clear();
                    randomvalueList.clear();
                    for (UserBackground background : response.body()) {
                        if (!background.getisOwn() && background.getUser_data_id() == userId)
                            all_BackgroundsList.add(background.getBackground_id());
                    }
                    decrementPendingAsyncTasks();
                    Res_BackgroundId();
                }

            }

            @Override
            public void onFailure(Call<List<UserBackground>> call, Throwable t) {
                decrementPendingAsyncTasks();
                Log.e("API Request Failure", "Error: ", t);
            }
        });
    }



    private void Res_BackgroundId() {
        incrementPendingAsyncTasks();
        for (int i = 0; i < 4; i++) {
            randomValue = getRandomIntElement(all_BackgroundsList);
            randomvalueList.add(randomValue);
        }

        if(randomvalueList.get(0) == -1) {
            back_res = getResources().getIdentifier(back_img, "drawable", getPackageName());
            buy_back.setImageResource(back_res);
            buy_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Do nothing (none)
                }
            });
            //ここで飛ばないようにしたい
            back_cost.setText("完売");
        }else {
            apiService.getBackgrounds(randomvalueList.get(0)).enqueue(new Callback<background>() {
                @Override
                public void onResponse(Call<background> call, Response<background> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        int cost = response.body().getRare() * 5000;
                        backgroundList.add(response.body().getId());
                        String img = response.body().getImg()

                                .replace("\"", "").trim();
                        back_res = getResources().getIdentifier(img, "drawable", getPackageName());
                        backgroundId = response.body().getId();
                        buy_back.setImageResource(back_res);
                        back_cost.setText(String.valueOf(cost));
                    }
                }


                @Override
                public void onFailure(Call<background> call, Throwable t) {
                    Log.e("API Request Failure", "Error: ", t);
                }
            });

        }
        decrementPendingAsyncTasks();
    }


    private int getRandomIntElement(List<Integer> set) {
        incrementPendingAsyncTasks();
        List<Integer> list = new ArrayList<>(set);
        if (list == null || list.isEmpty()) {
            // リストが空の場合、適切な処理を行うか、例外を投げる
            decrementPendingAsyncTasks();
            return -1;
        }else {
            Random random = new Random();
            decrementPendingAsyncTasks();
            return list.get(random.nextInt(list.size()));
        }

    }

    public void buy_card(View view) {
        card_screen.setVisibility(View.GONE);
        title_screen.setVisibility(View.GONE);
        back_screen.setVisibility(View.GONE);
        int cost = Integer.parseInt(buy_card_cost.getText().toString());
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("UserId", 1);

        // 現在のユーザースコアを取得するAPIリクエストを想定
        apiService.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int currentMoney = response.body().getMoney();
                    Log.e("money", String.valueOf(currentMoney));
                    if (currentMoney < cost) {
                        make_ano(title_ano);
                    } else {
                        //お金更新
                        int newMoney = currentMoney - cost;
                        apiService.getUser(userId).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if(response.isSuccessful()&&response.body()!=null){
                                    String name = response.body().getName();
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

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {

                            }
                        });

                        UserQuestionDataUpdateRequest request = new UserQuestionDataUpdateRequest(true, userId, questionId);
                        apiService.updateUserQuestionData(request).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    // 更新成功時の処理。例えば、UIの更新など
                                    Log.d("UpdateUseStatus", "Background use status updated successfully.");
                                    GetMoney();
                                    fetchQuestions();
                                    back_store(view);
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


            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("UserMoneyFetch", "ユーザーのmoney取得失敗", t);
            }
        });
    }

    public void buy_back(View view) {
        int cost = Integer.parseInt(back_cost2.getText().toString());
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("UserId", 1);

        // 現在のユーザースコアを取得するAPIリクエストを想定
        apiService.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int currentMoney = response.body().getMoney();
                    Log.e("money", String.valueOf(currentMoney));
                    if (currentMoney < cost) {
                        make_ano(title_ano);
                    } else {
                        //お金更新
                        int newMoney = currentMoney - cost;
                        apiService.getUser(userId).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if(response.isSuccessful()&&response.body()!=null){
                                    String name = response.body().getName();
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

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {

                            }
                        });

                        UserBackgroundUpdateRequest request = new UserBackgroundUpdateRequest(userId, backgroundId, true,false,false);
                        apiService.updateUserBackgroundData(request).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    // 更新成功時の処理。例えば、UIの更新など
                                    Log.d("UpdateBackStatus", "Background use status updated successfully.");

                                    GetMoney();
                                    fetchBackgrounds();
                                    back_store(view);

                                } else {
                                    // エラー処理
                                    Log.e("UpdateBackStatus", "Failed to update the background use status."+ response.message());
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                // 通信失敗時の処理
                                Log.e("UpdateBackStatus", "API call failed.", t);
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

    public void buy_title(View view) {
        int cost = Integer.parseInt(buy_title_cost.getText().toString());
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("UserId", 1);

        // 現在のユーザースコアを取得するAPIリクエストを想定
        apiService.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int currentMoney = response.body().getMoney();
                    Log.e("money", String.valueOf(currentMoney));
                    if (currentMoney < cost) {
                        make_ano(title_ano);
                    } else {
                        //お金更新
                        int newMoney = currentMoney - cost;
                        apiService.getUser(userId).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if(response.isSuccessful()&&response.body()!=null){
                                    String name = response.body().getName();
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

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {

                            }
                        });

                        UserTitleUpdateRequest request = new UserTitleUpdateRequest(false, true, false, TitleNum, userId);
                        apiService.updateUserTitleData(request).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    // 更新成功時の処理。例えば、UIの更新など
                                    Log.d("UpdateUseStatus", "Background use status updated successfully.");
                                    GetMoney();
                                    fetchTitles();
                                    back_store(view);
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
        card_screen.setVisibility(View.GONE);
        title_screen.setVisibility(View.GONE);
        back_screen.setVisibility(View.GONE);
        card_ano.removeAllViews();
        title_ano.removeAllViews();
        back_ano.removeAllViews();
        store_screen.setVisibility(View.VISIBLE);
    }

    //card購入
    public void go_buy_card(View view){
        buy_card_cost.setText(card_cost1.getText());
        buy_card.setImageResource(card_res1);
        card_screen.setVisibility(View.VISIBLE);
        store_screen.setVisibility(View.GONE);
        questionId = cardList.get(0);
    }

    public void go_buy_card2(View view){
        buy_card_cost.setText(card_cost2.getText());
        buy_card.setImageResource(card_res2);
        card_screen.setVisibility(View.VISIBLE);
        store_screen.setVisibility(View.GONE);
        questionId = cardList.get(1);
    }

    public void go_buy_card3(View view){
        buy_card_cost.setText(card_cost3.getText());
        buy_card.setImageResource(card_res3);
        card_screen.setVisibility(View.VISIBLE);
        store_screen.setVisibility(View.GONE);
        questionId = cardList.get(2);
    }

    public void go_buy_card4(View view){
        buy_card_cost.setText(card_cost4.getText());
        buy_card.setImageResource(card_res4);
        card_screen.setVisibility(View.VISIBLE);
        store_screen.setVisibility(View.GONE);
        questionId = cardList.get(3);
    }

    //title購入
    public void go_buy_title(View view) {
        buy_title_cost.setText(title_cost.getText());
        buy_title.setText(title.getText());
        title_screen.setVisibility(View.VISIBLE);
        store_screen.setVisibility(View.GONE);
        TitleNum = titleList.get(0);
    }

    public void go_buy_title2(View view) {
        buy_title_cost.setText(title_cost2.getText());
        buy_title.setText(title2.getText());
        title_screen.setVisibility(View.VISIBLE);
        store_screen.setVisibility(View.GONE);
        TitleNum = titleList.get(1);
    }

    public void go_buy_title3(View view) {
        buy_title_cost.setText(title_cost3.getText());
        buy_title.setText(title3.getText());
        title_screen.setVisibility(View.VISIBLE);
        store_screen.setVisibility(View.GONE);
        TitleNum = titleList.get(2);
    }

    //背景購入
    public void go_buy_back(View view){
        buy_back2.setImageResource(back_res);
        back_cost2.setText(back_cost.getText());
        back_screen.setVisibility(View.VISIBLE);
        store_screen.setVisibility(View.GONE);
    }

    public void none(View view){

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

    private void setBackgroundid(ImageView background_image){
        incrementPendingAsyncTasks();
        //DBから称号を取ってくる titleに格納
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
                        Log.e("UserBackgroundId", "Resource ID not found for: " + img);
                        decrementPendingAsyncTasks();
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
                runOnUiThread(() -> store_screen.setVisibility(View.VISIBLE));
            }
        }
    }

}