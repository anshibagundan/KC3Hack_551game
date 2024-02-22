package com.example.eemon551;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class background_change extends AppCompatActivity {

    private ApiService apiService;
    private GridLayout background_layout;
    private List<Integer> background_list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_change);

        apiService = ApiClient.getApiService();
        background_layout = findViewById(R.id.background_layout);
        background_layout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        background_layout.setColumnCount(3);
        fetchbackground();
    }

    private void fetchbackground() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("UserId", 1);

        apiService.getUserBackgrounds(userId).enqueue(new Callback<List<UserBackground>>() {
            @Override
            public void onResponse(Call<List<UserBackground>> call, Response<List<UserBackground>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    background_list.clear(); // 既存のリストをクリアして新しいデータで更新

                    // ループを使用して、isOwnがtrueの全ての背景をリストに追加
                    for (UserBackground background : response.body()) {
                        if (background.getisOwn()) { // isOwnがtrueの場合に追加
                            background_list.add(background.getBackground_id());
                        }
                    }

                    if (background_list.isEmpty()) {
                        // 使用中のタイトルが1つも見つからない場合の処理
                        Log.e("UserBackground", "No active backgrounds found");
                    } else {
                        // UIスレッドでビューを更新
                        runOnUiThread(() -> {
                            // ここでbackground_listを使って背景を設定するロジックを実装
                            background_layout.removeAllViews();
                            SetImage(background_list);
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserBackground>> call, Throwable t) {
                Log.e("API Request Failure", "Error: ", t);
            }
        });
    }


    private void SetImage(List<Integer> userbackgroundid) {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("UserId", 1);
        for (int i = 0; i < userbackgroundid.size(); i++) {
            int backgroundId = userbackgroundid.get(i);
            apiService.getBackgrounds(backgroundId).enqueue(new Callback<background>() {
                @Override
                public void onResponse(Call<background> call, Response<background> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // 各背景画像に対して新しいImageViewを生成
                        ImageView imageView = new ImageView(background_change.this);
                        String img = response.body().getImg().replace("\"", "").trim();
                        int imageResId = getResources().getIdentifier(img, "drawable", getPackageName());
                        Glide.with(background_change.this).load(imageResId).into(imageView);

                        imageView.setOnClickListener(v -> {
                            // クリックされた背景のuseをtrueに更新し、他をfalseにする
                            updateUserBackgroundStatus(userId, backgroundId);
                            Intent intent = new Intent(background_change.this, activity_home.class);
                            startActivity(intent);
                        });

                        // ImageViewにレイアウトパラメータを設定してから追加
                        GridLayout.LayoutParams params = SetGridLayout();
                        background_layout.addView(imageView, params);
                    }
                }

                @Override
                public void onFailure(Call<background> call, Throwable t) {
                    Log.e("UserBackgroundId", "API call failed: " + t.getMessage());
                }
            });
        }

    }
    private GridLayout.LayoutParams SetGridLayout(){
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 300;  // 画像の幅
        params.height = 300; // 画像の高さ
        params.setGravity(Gravity.CENTER);
        return params;

    }
    private void updateUserBackgroundStatus(int userId, int backgroundId) {
        boolean isOwn = true;
        boolean buyOK = false;
        boolean use = true;
        UserBackgroundUseUpdateRequest request = new UserBackgroundUseUpdateRequest(userId, backgroundId, isOwn, buyOK,use);
        apiService.updateUserBackgroundUseStatus(request).enqueue(new Callback<Void>() {
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