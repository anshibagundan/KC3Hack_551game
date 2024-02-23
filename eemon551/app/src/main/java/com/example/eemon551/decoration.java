package com.example.eemon551;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private TextView user_name,user_title;
    private int userId;
    private LinearLayout titles;


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
        //称号
        user_name = findViewById(R.id.user_name);
        user_title = findViewById(R.id.user_title);
        titles = findViewById(R.id.titles);

        fetchbackground();

        //名前の表示
        apiService.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    user_name.setText(response.body().getName());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("API_CALL", "API call failed: " + t.getMessage());
            }
        });
        //使っているtitleを書く
        writeTitle(user_title,userId);
        //持っているtitleを
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
        UserBackgroundUpdateRequest request = new UserBackgroundUpdateRequest(userId, backgroundId, isOwn, buyOK,use);
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

    //usertitle表示
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
                    if(response.body().get(i).getUser_data_id()==userId) {
                        usertitleid = response.body().get(i).getTitle_id();
                        // ここでtitlesからtitle_idを取得
                        Log.e("UserTitleId", "" + usertitleid);
                        setTitleName(usertitleid, user_title);
                    }else{
                        user_title.setText("");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserTitles>> call, Throwable t) {
                Log.e("UserTitleId", "API call failed: " + t.getMessage());
            }
        });


    }
    private void setTitleName(int UserTitleId, TextView user_title){
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

    //titleの図鑑
    private void titles(String title){
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                100 // 高さ 100dp
        ));
        linearLayout.setBackgroundColor(Color.parseColor("#FF5722")); // @color/place の色

        // TextViewを作成
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                50 // 高さ 50dp
        ));
        textView.setGravity(Gravity.CENTER);
        textView.setText(title);
        textView.setTextSize(30);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(Color.WHITE);

        // LinearLayoutにTextViewを追加
        linearLayout.addView(textView);

        // 余白を追加
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        layoutParams.setMargins(0, 20, 0, 0); // 上部に20dpの余白を追加
        linearLayout.setLayoutParams(layoutParams);

        titles.addView(linearLayout);
    }

    //title変更
    private void preChangeTitle(TextView title){
        user_title.setText(title.getText());
    }

    //変更ボタン
    public void cangeTitle(View v){
        //DB格納

        //戻る
        overlay_title_back(v);
    }


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