package com.example.eemon551;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // APIサービスのインスタンスを取得
    ApiService apiService = ApiClient.getApiService();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    SharedPreferences.Editor editor = prefs.edit();


                    // ユーザーデータをサーバーに送信
                    User user = new User(name, 0, 0);
                    apiService.insertUserData(user).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Log.d("first_db", "Data successfully sent to the server.");
                                // ユーザーIDの取得とSharedPreferencesへの保存
                                fetchAndSaveUserId(apiService, name, prefs);
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
            int userId = prefs.getInt("UserId", 0);

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
                    Log.d("API_CALL", "User ID: " + userId);
                    // 別のActivityへ遷移

                } else {
                    Log.e("API_CALL", "User not found or error in API");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("API_CALL", "API call failed: " + t.getMessage());
            }
        });
        navigateToHome();
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
        //DBから称号を取ってくる titleに格納
        apiService.getUserTitles(userId).enqueue(new Callback<List<UserTitles>>() {
            @Override
            public void onResponse(Call<List<UserTitles>> call, Response<List<UserTitles>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int i = 0;
                    int usertitleid = 0;
                    while (!response.body().get(i).getUse()) {
                        i = i + 1;
                    }
                    usertitleid = response.body().get(i).getTitle_id();
                    // ここでtitlesからtitle_idを取得
                    Log.e("UserTitleId", "" + usertitleid);
                    setTitleName(usertitleid, user_title);
                }
            }

            @Override
            public void onFailure(Call<List<UserTitles>> call, Throwable t) {
                Log.e("UserTitleId", "API call failed: " + t.getMessage());
            }
        });


    }
    private void setTitleName(int UserTitleId,TextView user_title){


        apiService.getTitle(UserTitleId).enqueue(new Callback<Titles>() {
            @Override
            public void onResponse(Call<Titles> call, Response<Titles> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String title = response.body().getName();
                    user_title.setText(title);
                    Log.e("UserTitleId", "" +title);
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
        startActivity(intent);
    }
}
