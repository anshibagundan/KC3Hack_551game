package com.example.eemon551;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    private Button zukann;
    private Button introduce;
    private Button store;
    private Boolean setting = false;
    private  ImageView zukan_img;
    private  ImageView setsumei_img;
    private ImageView store_img;
    private Button setting_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        apiService = ApiClient.getApiService();
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String username = prefs.getString("UserName", "test_user");

        initializeViews();
        name.setText(username);
        loadFirstQuestionGenre();
    }

    //ここにIDの呼び出しかいてね
    private void initializeViews() {
        startButton = findViewById(R.id.start);
        textGenre = findViewById(R.id.genre);
        textLocation = findViewById(R.id.prefecture);
        img1 = findViewById(R.id.right_genre);
        touka = findViewById(R.id.touka);
        zukann = findViewById(R.id.zukann);
        introduce = findViewById(R.id.introduce);
        zukan_img=findViewById(R.id.zukan_img);
        setsumei_img=findViewById(R.id.setsumei_img);
        setting_button = findViewById(R.id.setting_button);
        store = findViewById(R.id.store);
        store_img=findViewById(R.id.store_img);
        money_num = findViewById(R.id.money_num);
        name = findViewById(R.id.name);
    }

    public void setButtonClickListener(View view) {
        // Intentを作成してGameActivityを起動
        //ここでgama.javaにgenreIdとlocationIdを渡す
        Intent intent = new Intent(activity_home.this, game.class);
        intent.putExtra("genreId", genreId);
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
                return "全て";
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
    public void setting(View view){
        setting = !setting;
        if (setting){
            touka.setVisibility(View.VISIBLE);
            zukann.setVisibility(View.VISIBLE);
            introduce.setVisibility(View.VISIBLE);
            zukan_img.setVisibility(View.VISIBLE);
            setsumei_img.setVisibility(View.VISIBLE);
            store.setVisibility(View.VISIBLE);
            store_img.setVisibility(View.VISIBLE);
            setting_button.setText("X");
        }else{
            touka.setVisibility(View.GONE);
            zukann.setVisibility(View.GONE);
            introduce.setVisibility(View.GONE);
            zukan_img.setVisibility(View.GONE);
            setsumei_img.setVisibility(View.GONE);
            store.setVisibility(View.GONE);
            store_img.setVisibility(View.GONE);
            setting_button.setText("≡");
        }
    }
    private void GetMoney(){
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("UserId", 1);
        apiService.getUserMoney(userId).enqueue(new Callback<User>() {
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
}
