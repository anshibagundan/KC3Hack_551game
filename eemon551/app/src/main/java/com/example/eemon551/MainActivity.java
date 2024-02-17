package com.example.eemon551;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        if (isFirstRun) {
            textView.setText("ようこそ551ゲーム");

            ApiService apiService = ApiClient.getApiService();
            String name = user_name.getText().toString();
            User user = new User(name,0,0);
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
                    // 通信エラー (例: インターネット接続なし)
                    Log.e("first_db", "Failed to send data. Error: " + t.getMessage());
                }
            });

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isFirstRun", false);
            int User_id = prefs.getInt("isFirstRun")
            editor.apply();
        }else{
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
