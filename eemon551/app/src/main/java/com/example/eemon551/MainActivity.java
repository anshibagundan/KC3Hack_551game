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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // SharedPreferencesを取得
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("isFirstRun", true);
        // TextViewを取得
        TextView textView = findViewById(R.id.app_name);
        EditText user_name = findViewById(R.id.user_name);
        FrameLayout main = findViewById(R.id.main);
        FrameLayout set_user = findViewById(R.id.set_user);
        Button kettei = findViewById(R.id.kettei);

        if (isFirstRun) {
            main.setVisibility(View.GONE);
            set_user.setVisibility(View.VISIBLE);
            textView.setText("ようこそ551ゲーム");


            ApiService apiService = ApiClient.getApiService();
            String name = user_name.getText().toString();

            kettei.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(name==null) {
                        textView.setText("名前入力せい！");
                    }else {
                        User user = new User(name, 0, 0);
                        Call<Void> call = apiService.insertUserData(user);

                        call.enqueue(new Callback<Void>() {
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

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("isFirstRun", false);

                        Call<User> call_user = apiService.getUserId(name, 0, 0);

                        call_user.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    int User_id = prefs.getInt("isFirstRun", response.body().getId());
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putInt("UserId", User_id); // "UserId"はキー名です
                                    editor.apply();
                                    Log.d("API_CALL", "User ID: " + User_id);
                                } else {
                                    Log.e("API_CALL", "User not found or error in API");
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Log.e("API_CALL", "API call failed: " + t.getMessage());
                            }
                        });
                        editor.apply();
                        // Intentを作成してGameActivityを起動
                        Intent intent = new Intent(MainActivity.this, activity_home.class);
                        startActivity(intent);
                    }
                }
            });


        }else{
            main.setVisibility(View.VISIBLE);
            set_user.setVisibility(View.GONE);
            textView.setText("551ゲーム");
            Button tapButton = findViewById(R.id.tap);

            tapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intentを作成してGameActivityを起動
                    Intent intent = new Intent(MainActivity.this, activity_home.class);
                    startActivity(intent);
                }
            });
        }
    }
}
