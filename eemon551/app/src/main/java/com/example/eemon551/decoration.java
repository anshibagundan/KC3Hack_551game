package com.example.eemon551;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class decoration extends AppCompatActivity {


    private ApiService apiService;
    private GridLayout background_layout;
    private LinearLayout name_change;
    private LinearLayout title_change;
    private  LinearLayout background_change;
    private Button name_chnage_button;
    private Button title_chnage_button;
    private Button background_chnage_button;
    private List<Integer> background_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_decoration);
        apiService = ApiClient.getApiService();
        background_layout = findViewById(R.id.background_layout);
        name_change=findViewById(R.id.name_change);
        title_change=findViewById(R.id.title_change);
        background_change=findViewById(R.id.background_change);
        name_chnage_button=findViewById(R.id.name_change_button);
        title_chnage_button=findViewById(R.id.title_change_button);
        background_chnage_button=findViewById(R.id.background_change_button);
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
                        if (userId == background.getUser_data_id()) {
                            if (background.getisOwn()) { // isOwnがtrueの場合に追加
                                background_list.add(background.getBackground_id());
                            }
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
                        ImageView imageView = new ImageView(decoration.this);
                        String img = response.body().getImg().replace("\"", "").trim();
                        int imageResId = getResources().getIdentifier(img, "drawable", getPackageName());
                        Glide.with(decoration.this).load(imageResId).into(imageView);

                        imageView.setOnClickListener(v -> {
                            // クリックされた背景のuseをtrueに更新し、他をfalseにする
                            updateUserBackgroundStatus(userId, backgroundId);
                            Intent intent = new Intent(decoration.this, activity_home.class);
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

//    name_change.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            // キーボードを非表示にする
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//        }
//    });


//変更画面のオーバーレイ
    public void overlay_name(View view){
            name_change.setVisibility(View.VISIBLE);
            name_chnage_button.setVisibility(View.GONE);
            title_chnage_button.setVisibility(View.GONE);
            background_chnage_button.setVisibility(View.GONE);
    }
    public void overlay_title(View view){
        title_change.setVisibility(View.VISIBLE);
        name_chnage_button.setVisibility(View.GONE);
        title_chnage_button.setVisibility(View.GONE);
        background_chnage_button.setVisibility(View.GONE);
    }
    public void overlay_background(View view){
        background_change.setVisibility(View.VISIBLE);
        name_chnage_button.setVisibility(View.GONE);
        title_chnage_button.setVisibility(View.GONE);
        background_chnage_button.setVisibility(View.GONE);
    }
    public void overlay_name_back(View view){
        name_change.setVisibility(View.GONE);
        name_chnage_button.setVisibility(View.VISIBLE);
        title_chnage_button.setVisibility(View.VISIBLE);
        background_chnage_button.setVisibility(View.VISIBLE);
    }
    public void overlay_title_back(View view){
        title_change.setVisibility(View.GONE);
        name_chnage_button.setVisibility(View.VISIBLE);
        title_chnage_button.setVisibility(View.VISIBLE);
        background_chnage_button.setVisibility(View.VISIBLE);
    }
    public void overlay_background_back(View view){
        background_change.setVisibility(View.GONE);
        name_chnage_button.setVisibility(View.VISIBLE);
        title_chnage_button.setVisibility(View.VISIBLE);
        background_chnage_button.setVisibility(View.VISIBLE);
    }


//ホーム下の画面遷移
    public void decoration_home(View view){
        Intent intent = new Intent(decoration.this, activity_home.class);
        startActivity(intent);
    }
    public void decoration_zukann(View view){
        Intent intent = new Intent(decoration.this, garally.class);
        startActivity(intent);
    }
    public void decoration_store(View view){
        Intent intent = new Intent(decoration.this, store.class);
        startActivity(intent);
    }
    public void decoration_introduce(View view){
        Intent intent = new Intent(decoration.this, introduce.class);
        startActivity(intent);
    }
}