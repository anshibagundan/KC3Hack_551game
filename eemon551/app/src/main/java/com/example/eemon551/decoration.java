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
    private TextView pretitle;
    private int usertitleid,selectID;
    private List<TextView> titleList = new ArrayList<>();


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
//        pretitle = findViewById(R.id.pretitle);


        //userid
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = prefs.getInt("UserId", 1);
        //名前
        setName();
        //使っているtitleを書く
        writeTitle(user_title,userId);
        //持っているtitleを並べる
        titles();
        //背景
        fetchbackground();
    }

    //setName
    private void setName(){
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
    }
    //back
    private void fetchbackground() {
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
                    usertitleid = 0;
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
    private void titles() {
        apiService.getUserTitles(userId).enqueue(new Callback<List<UserTitles>>() {
            @Override
            public void onResponse(Call<List<UserTitles>> call, Response<List<UserTitles>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for(UserTitles title: response.body()) {
                        if (title.getisOwn()) {
                            apiService.getTitle(title.getTitle_id()).enqueue(new Callback<Titles>() {
                                @Override
                                public void onResponse(Call<Titles> call, Response<Titles> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        String txt =response.body().getName();
                                        create_title(title.getTitle_id(),txt);
                                    }
                                }
                                @Override
                                public void onFailure(Call<Titles> call, Throwable t) {
                                }
                            });
                        }

                    }
                }
            }
            @Override
            public void onFailure(Call<List<UserTitles>> call, Throwable t) {

            }
        });
    }

    private void create_title(final int titleid,String txt){
        // TextViewを作成
        TextView preTitleTextView = new TextView(this);
        preTitleTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                100)); // 高さを100dpに設定
        preTitleTextView.setGravity(Gravity.CENTER);
        preTitleTextView.setText(txt);
        preTitleTextView.setTextSize(30);
        preTitleTextView.setTypeface(null, Typeface.BOLD);
        preTitleTextView.setTextColor(Color.WHITE);
        preTitleTextView.setBackgroundColor(getResources().getColor(R.color.place)); // R.color.placeはcolors.xmlで定義された色のリソース
        preTitleTextView.setPadding(0, 20, 0, 0); // marginTopを設定
        preTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // タップされたときの処理
                preChangeTitle(preTitleTextView,titleid);
                selectID = titleid;
            }
        });
        // LinearLayoutにTextViewを追加
        titles.addView(preTitleTextView);
        titleList.add(preTitleTextView);
    }

    //title変更
    private void preChangeTitle(TextView title, int selectedIndex){
        user_title.setText(title.getText());
        // 全てのTextViewの背景色をもとに戻す
        for (int i = 0; i < titleList.size(); i++) {
            TextView textView = titleList.get(i);
            textView.setBackgroundColor(getResources().getColor(R.color.place));
        }

        // タップされたTextViewの背景色を変更
        TextView selectedTextView = titleList.get(selectedIndex);
        selectedTextView.setBackgroundColor(getResources().getColor(R.color.selected));

    }

    //変更ボタン
    public void changeTitle(View v){
        //DB格納(前のをオフ）
        UserTitleUpdateRequest request = new UserTitleUpdateRequest(true, true, false, selectID, userId);
        apiService.updateUserTitleData(request).enqueue(new Callback<Void>() {
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

        //DB格納（今のをおん）
        UserTitleUpdateRequest request1 = new UserTitleUpdateRequest(false, true, false, usertitleid, userId);
        apiService.updateUserTitleData(request1).enqueue(new Callback<Void>() {
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

    public void changeName(View view) {
        String name = user_name.getText().toString();
        if(name.isEmpty()) {
            return;
        }

        UserDataNameUpdateRequest data = new UserDataNameUpdateRequest(name);
        apiService.updateUserDataName(userId,data).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    //何もなくていい　　
                    // 変更成功ログを書くといいかも
                }else{
                    //変更できなかった時用のログ
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //DB接続失敗用ログ
            }
        });
    }

//    public void title_tap(View view) {
//        pretitle.setBackgroundColor(Color.rgb(0,100,200));
//        user_title.setText(pretitle.getText());
//    }
}