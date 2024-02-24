package com.example.eemon551;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // APIサービスのインスタンスを取得
    ApiService apiService = ApiClient.getApiService();
    private ImageView back_img;
    private ImageView image_2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);

        // SharedPreferencesのインスタンスを取得し、アプリが初回起動かどうかをチェック
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("isFirstRun", true);

        // UIコンポーネントの取得
        TextView textView = findViewById(R.id.app_name);
        EditText user_name = findViewById(R.id.user_name);
        FrameLayout main = findViewById(R.id.main);
        FrameLayout set_user = findViewById(R.id.set_user);
        Button kettei = findViewById(R.id.kettei);
        TextView user_title = findViewById(R.id.user_title);
        back_img = findViewById(R.id.back_img);
        image_2 = findViewById(R.id.image_2);

        // 初回起動時の処理
        if (isFirstRun) {
            // 初回起動時のUI設定
            main.setVisibility(View.GONE);
            set_user.setVisibility(View.VISIBLE);
            textView.setText("ようこそ551ゲームへ");

            // 決定ボタンのClickListener設定
            kettei.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ユーザー名の取得とバリデーション
                    String name = user_name.getText().toString();
                    if(name.isEmpty()) {
                        textView.setText("名前を入力してください！");
                        return;
                    }

                    // ユーザー名をSharedPreferencesに保存



                    // ユーザーデータをサーバーに送信
                    User user = new User(name, 0, 0);
                    apiService.insertUserData(user).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Log.d("first_db", "Data successfully sent to the server.");
                                // ユーザーIDの取得とSharedPreferencesへの保存
                                fetchAndSaveUserId(apiService, name, prefs);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putBoolean("isFirstRun", false);
                            } else {
                                Log.e("first_db", "Failed to send data. Response code: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("first_db", "Failed to send data. Error: " + t.getMessage());
                        }
                    });
                }
            });
        } else {
            // 2回目以降の起動時のUI設定
            int userId = prefs.getInt("UserId", 1);

            configureUIForReturningUser(prefs, textView, main, set_user,userId);
            //称号
            writeTitle(user_title,userId);
        }
    }

    // ユーザーIDの取得とSharedPreferencesへの保存を行うメソッド
    private void fetchAndSaveUserId(ApiService apiService, String name, SharedPreferences prefs) {
        apiService.getUserId(name, 0, 0).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int userId = response.body().getId();
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("UserId", userId);
                    editor.putBoolean("isFirstRun", false);
                    editor.apply();
                    UserTitles data = new UserTitles(true, true, false, userId, 1);
                    apiService.insertUserTitle(data).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Log.d("first_db", "Data successfully sent to the server.");
                            } else {
                                Log.e("first_db", "Failed to send data. Response code: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("first_db", "Failed to send data. Error: " + t.getMessage());
                        }
                    });

                    for (int i = 2; i<52; i++) {
                        UserTitles data2 = new UserTitles(false, false, false, userId, i);
                        apiService.insertUserTitle(data2).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Log.d("first_db", "Data successfully sent to the server.");
                                } else {
                                    Log.e("first_db", "Failed to send data. Response code: " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("first_db", "Failed to send data. Error: " + t.getMessage());
                            }
                        });
                    }

                    UserBackground data_back = new UserBackground(true, true, false, 1, userId);
                    apiService.insertUserbackground(data_back).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Log.d("first_db", "Data successfully sent to the server.");
                            } else {
                                Log.e("first_db", "Failed to send data. Response code: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("first_db", "Failed to send data. Error: " + t.getMessage());
                        }
                    });

                    for (int i = 2; i<16; i++) {
                        UserBackground data2_back = new UserBackground(false, false, false, i, userId);
                        apiService.insertUserbackground(data2_back).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Log.d("first_db", "Data successfully sent to the server.");
                                } else {
                                    Log.e("first_db", "Failed to send data. Response code: " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("first_db", "Failed to send data. Error: " + t.getMessage());
                            }
                        });
                    }

                    navigateToHome(); // この行をコールバック内に移動
                } else {
                    Log.e("API_CALL", "User not found or error in API");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("API_CALL", "API call failed: " + t.getMessage());
            }
        });


    }


    // 2回目以降の起動時のUI設定を行うメソッド
    private void configureUIForReturningUser(SharedPreferences prefs, TextView textView, FrameLayout main, FrameLayout set_user, int userId) {
        main.setVisibility(View.VISIBLE);
        set_user.setVisibility(View.GONE);
        apiService.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    textView.setText(response.body().getName());
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("API_CALL", "API call failed: " + t.getMessage());
            }
        });

        findViewById(R.id.tap).setOnClickListener(v -> navigateToHome());
    }

    // 別のActivityへ遷移するメソッド


    //称号
    private void writeTitle(TextView user_title, int userId){
        // DBから称号を取ってくる titleに格納
        apiService.getUserTitles(userId).enqueue(new Callback<List<UserTitles>>() {
            @Override
            public void onResponse(Call<List<UserTitles>> call, Response<List<UserTitles>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("UserTitleId", "userId: " + userId);
                    int j = 0;
                    while (j < response.body().size() && response.body().get(j).getUser_data_id() != userId) {
                        j++;
                    }
                    if (j < response.body().size()) { // ユーザーIDが見つかった場合
                        int i = j;
                        while (i < response.body().size() && !response.body().get(i).getUse()) {
                            i++;
                        }
                        if (i < response.body().size()) { // 使用中のタイトルが見つかった場合
                            int usertitleid = response.body().get(i).getTitle_id();

                            Log.e("UserTitleId", "usertitleid: " + usertitleid);
                            Log.e("UserTitleId", "user_data_id: " + response.body().get(i).getUser_data_id());
                            setTitleName(usertitleid, user_title);
                        } else {
                            // 使用中のタイトルが見つからない場合の処理
                            Log.e("UserTitleId", "No active title found");
                        }
                    } else {
                        // ユーザーIDが見つからない場合の処理
                        Log.e("UserTitleId", "User ID not found");
                        user_title.setText("称号がないよ");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserTitles>> call, Throwable t) {
                Log.e("UserTitleId", "API call failed: " + t.getMessage());
            }
        });
    }

    private void setTitleName(int UserTitleId,TextView user_title) {


        apiService.getTitle(UserTitleId).enqueue(new Callback<Titles>() {
            @Override
            public void onResponse(Call<Titles> call, Response<Titles> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String title = response.body().getName();
                    user_title.setText(title);
                    Log.e("UserTitleId", "title: " + title);
                }
            }

            @Override
            public void onFailure(Call<Titles> call, Throwable t) {
                Log.e("UserTitleId", "API call failed: " + t.getMessage());
            }
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(MainActivity.this, activity_home.class);
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("UserId", 1);
        intent.putExtra("userId",userId);
        startActivity(intent);
        Log.e("userId2", "Main userId: "+userId );
    }
}
