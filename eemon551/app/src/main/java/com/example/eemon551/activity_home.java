package com.example.eemon551;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_home extends AppCompatActivity {

    private ApiService apiService;
    private Button startButton;
    private TextView textGenre;
    private TextView textLocation;
    private ImageView img1;
    public int genreId = 0;
    public int locationId = 0;

    private TextView touka;
    private Boolean setting = false;
    private TextView name;
    private TextView money_num;
    private TextView shogo;
    private ImageView image_3;
    private LinearLayout home_layout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_home);
        apiService = ApiClient.getApiService();
        home_layout = findViewById(R.id.homeLayout);
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("UserId", 1);
        Log.e("userId2", "userId: "+userId );

        initializeViews();

        apiService.getUser(userId).enqueue(new Callback<User>() {
           @Override
           public void onResponse(Call<User> call, Response<User> response) {
               if (response.isSuccessful() && response.body() != null) {
                   name.setText(response.body().getName());
               }
           }

           @Override
           public void onFailure(Call<User> call, Throwable t) {
               Log.e("API_CALL", "API call failed: " + t.getMessage());
           }
       });
        writeTitle(shogo);
        setBackgroundid(image_3);
        GetMoney();
        loadFirstQuestionGenre();

    }

    //ここにIDの呼び出しかいてね
    private void initializeViews() {
        startButton = findViewById(R.id.start);
        textGenre = findViewById(R.id.genre);
        textLocation = findViewById(R.id.prefecture);
        img1 = findViewById(R.id.right_genre);
        money_num = findViewById(R.id.money_num);
        name = findViewById(R.id.name);
        shogo = findViewById(R.id.shogo);
        image_3 = findViewById(R.id.image_3);
    }

    //start button
    public void setButtonClickListener(View view) {
        // Intentを作成してGameActivityを起動
        //ここでgama.javaにgenreIdとlocationIdを渡す
        Intent intent = new Intent(activity_home.this, game.class);
        intent.putExtra("genreId", genreId);
        intent.putExtra("locationId", locationId);
        startActivity(intent);
    }

    private void loadFirstQuestionGenre() {


        // APIリクエストを実行
        apiService.getAllQuestions().enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // 最初の質問のgenre_idを取得してTextViewにセット
//                    int genreId = response.body().get(0).getGenre_id();
                    String genre = getGenreName(genreId);
                    textGenre.setText(genre);

                    String location = getLocationName(locationId);
                    textLocation.setText(location);
                    home_layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                // エラーハンドリング
                Log.e("activity_home", "APIリクエスト失敗: ", t);
            }
        });
    }

    private String getGenreName(int genreId) {
        switch (genreId) {
            case 1:
                return "食べ物";
            case 2:
                return "建物";
            case 3:
                return "人";
            case 4:
                return "土地";
            case 5:
                return "文化";
            default:
                return "全て";
        }
    }

    private String getLocationName(int locationId) {
        switch (locationId) {
            case 1:
                return "大阪";
            case 2:
                return "京都";
            case 3:
                return "滋賀";
            case 4:
                return "奈良";
            case 5:
                return "兵庫";
            case 6:
                return "和歌山";
            default:
                return "関西全域";
        }
    }

    public void genre_right(View view){
        genreId++;
        genreId = genreId % 6;
        loadFirstQuestionGenre();
    }
    public void genre_left(View view){
        genreId--;
        if(genreId<0){
            genreId = 5;
        }
        genreId = genreId % 6;
        loadFirstQuestionGenre();
    }

    public void location_right(View view){
        locationId++;
        locationId = locationId % 6;
        loadFirstQuestionGenre();
    }
    public void location_left(View view){
        locationId--;
        if(locationId<0){
            locationId = 5;
        }
        locationId = locationId % 6;
        loadFirstQuestionGenre();
    }

    private void GetMoney(){
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("UserId", 1);
        apiService.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int currentScore = response.body().getMoney();
                    money_num.setText(String.valueOf(currentScore));
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("API Request Failure", "Error: ", t);
            }
        });
    }
    private void writeTitle(TextView user_title){
        //DBから称号を取ってくる titleに格納
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("UserId", 1);
        apiService.getUserTitles(userId).enqueue(new Callback<List<UserTitles>>() {
            @Override
            public void onResponse(Call<List<UserTitles>> call, Response<List<UserTitles>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int i = 0;
                    while(response.body().get(i).getUser_data_id()!=userId){
                        i = i +1;
                    }
                    while (!response.body().get(i).getUse()) {
                        i = i + 1;
                    }
                    Log.e("UserTitleId2", "" + i);
                    int usertitleid = response.body().get(i).getTitle_id();
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
    private void setBackgroundid(ImageView background_image){
        //DBから称号を取ってくる titleに格納
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("UserId", 1);
        apiService.getUserBackgrounds(userId).enqueue(new Callback<List<UserBackground>>() {
            @Override
            public void onResponse(Call<List<UserBackground>> call, Response<List<UserBackground>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("UserBackgroundId", "userId: " + userId);
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
                            int userbackgroundid = response.body().get(i).getBackground_id();

                            Log.e("UserBackgroundId", "userbackgroundid: " + userbackgroundid);
                            Log.e("UserBackgroundId", "user_data_id: " + response.body().get(i).getUser_data_id());

                            setBackground(userbackgroundid, background_image);

                        } else {
                            // 使用中のタイトルが見つからない場合の処理
                            Log.e("UserBackgroundId", "No active background found");
                        }
                    } else {
                        // ユーザーIDが見つからない場合の処理
                        Log.e("UserBackgroundId", "User ID not found");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserBackground>> call, Throwable t) {
                Log.e("UserBackgroundId", "API call failed: " + t.getMessage());
            }
        });


    }
    private void setBackground(int Userbackgroundid, ImageView background_image){


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
                            }
                        });
                    } else {
                        Log.e("UserBackgroundId", "Resource ID not found for: " + img);
                    }

                    Log.e("UserBackgroundId", "Resource ID: " + resourceId);

                }
            }
            @Override
            public void onFailure(Call<background> call, Throwable t) {
                Log.e("UserBackgroundId", "API call failed: " + t.getMessage());
            }
        });
    }

    public void zukann(View view){
        Intent intent = new Intent(activity_home.this, garally.class);
        startActivity(intent);
    }
    public void introduce(View view){
        Intent intent = new Intent(activity_home.this, introduce.class);
        startActivity(intent);
    }
    public void store(View view){
        Intent intent = new Intent(activity_home.this, store.class);
        startActivity(intent);
    }
    public void decoration(View view){
        Intent intent = new Intent(activity_home.this, decoration.class);
        startActivity(intent);
    }

}
