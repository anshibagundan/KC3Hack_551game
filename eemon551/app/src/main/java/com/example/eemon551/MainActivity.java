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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SharedPreferencesを取得
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("isFirstRun", true);

        // ビュー要素を取得
        TextView textView = findViewById(R.id.app_name);
        EditText user_name = findViewById(R.id.user_name);
        FrameLayout main = findViewById(R.id.main);
        FrameLayout set_user = findViewById(R.id.set_user);
        Button kettei = findViewById(R.id.kettei);

        ApiService apiService = ApiClient.getApiService();

        if (isFirstRun) {
            main.setVisibility(View.GONE);
            set_user.setVisibility(View.VISIBLE);
            textView.setText("ようこそ551ゲームへ");

            kettei.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = user_name.getText().toString();
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("UserName", name); // ユーザー名を保存
                    editor.apply();
                    if(name.isEmpty()) {
                        textView.setText("名前を入力してください！");
                        return;
                    }

                    User user = new User(name, 0, 0);
                    apiService.insertUserData(user).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Log.d("first_db", "Data successfully sent to the server.");
                                // ユーザーIDを取得してSharedPreferencesに保存
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
                                            // MainActivityから別のActivityへ遷移
                                            Intent intent = new Intent(MainActivity.this, activity_home.class);
                                            startActivity(intent);
                                        } else {
                                            Log.e("API_CALL", "User not found or error in API");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        Log.e("API_CALL", "API call failed: " + t.getMessage());
                                    }
                                });
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
            main.setVisibility(View.VISIBLE);
            set_user.setVisibility(View.GONE);
            int userId = prefs.getInt("UserId", 0); // デフォルト値はユーザーが見つからない場合のための0
            if(userId==0){
                textView.setText("ユーザーIDが取得できません ");
            }else {
                textView.setText("ユーザーID: " + userId);
            }
            Button tapButton = findViewById(R.id.tap);
            tapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, activity_home.class);
                    startActivity(intent);
                }
            });
        }
    }
}
