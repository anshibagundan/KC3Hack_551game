package com.example.eemon551;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class garally extends AppCompatActivity {

    private ApiService apiService;
    private List<Question> Osaka_QuestionList = new ArrayList<>();
    private List<Question> Kyoto_QuestionList = new ArrayList<>();
    private List<Question> Shiga_QuestionList = new ArrayList<>();
    private List<Question> Nara_QuestionList = new ArrayList<>();
    private List<Question> Hyogo_QuestionList = new ArrayList<>();
    private List<Question> Wakayama_QuestionList = new ArrayList<>();

    private GridLayout gridLayout_1;
    private GridLayout gridLayout_2;
    private GridLayout gridLayout_3;
    private GridLayout gridLayout_4;
    private GridLayout gridLayout_5;
    private GridLayout gridLayout_6;

    private FrameLayout card;
    private FrameLayout zukan;
    private ImageView card_image;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_garally);

        apiService = ApiClient.getApiService();
        gridLayout_1 = findViewById(R.id.genre1_layout);
        gridLayout_2 = findViewById(R.id.genre2_layout);
        gridLayout_3 = findViewById(R.id.genre3_layout);
        gridLayout_4 = findViewById(R.id.genre4_layout);
        gridLayout_5 = findViewById(R.id.genre5_layout);
        gridLayout_6 = findViewById(R.id.genre6_layout);
        gridLayout_1.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout_1.setColumnCount(3);
        gridLayout_2.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout_2.setColumnCount(3);
        gridLayout_3.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout_3.setColumnCount(3);
        gridLayout_4.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout_4.setColumnCount(3);
        gridLayout_5.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout_5.setColumnCount(3);
        gridLayout_6.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout_6.setColumnCount(3);
        card = findViewById(R.id.card);
        zukan = findViewById(R.id.zukan);
        card_image = findViewById(R.id.card_image);

        fetchQuestions();
    }

    private void fetchQuestions() {
        apiService.getAllQuestions().enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // 各リストをクリア
                    Osaka_QuestionList.clear();
                    Kyoto_QuestionList.clear();
                    Shiga_QuestionList.clear();
                    Nara_QuestionList.clear();
                    Hyogo_QuestionList.clear();
                    Wakayama_QuestionList.clear();

                    // 取得した質問リストをループ
                    for (Question question : response.body()) {
                        switch (question.getLoc_id()) {
                            case 1:
                                Osaka_QuestionList.add(question);
                                break;
                            case 2:
                                Kyoto_QuestionList.add(question);
                                break;
                            case 3:
                                Shiga_QuestionList.add(question);
                                break;
                            case 4:
                                Nara_QuestionList.add(question);
                                break;
                            case 5:
                                Hyogo_QuestionList.add(question);
                                break;
                            case 6:
                                Wakayama_QuestionList.add(question);
                                break;
                            default:
                                // 関西以外はないと思う
                                break;
                        }
                    }

                    // UIを更新するためのメソッドをメインスレッドで実行
                    runOnUiThread(() -> addImagesToGridLayout());
                }
            }
            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Log.e("API Request Failure", "Error: ", t);
            }
        });
    }


    private void addImagesToGridLayout() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("UserId", 1);
        for (Question question : Osaka_QuestionList) {
            gridLayout_1.addView(SetImage(question,userId), SetGridLayout());
        }
        for (Question question : Kyoto_QuestionList) {
            gridLayout_2.addView(SetImage(question,userId), SetGridLayout());
        }
        for (Question question : Shiga_QuestionList) {
            gridLayout_3.addView(SetImage(question,userId), SetGridLayout());
        }
        for (Question question : Nara_QuestionList) {
            gridLayout_4.addView(SetImage(question,userId), SetGridLayout());
        }
        for (Question question : Hyogo_QuestionList) {
            gridLayout_5.addView(SetImage(question,userId), SetGridLayout());
        }
        for (Question question : Wakayama_QuestionList) {
            gridLayout_6.addView(SetImage(question,userId), SetGridLayout());
        }
    }

    private void applyOverlayBasedOnUserQuestionData(int questionId, int userId, RelativeLayout card_lay, TextView lay_txt, ImageView imageView, Question question) {
        apiService.getUserQuestionData(userId).enqueue(new Callback<List<UserQuestionData>>() {
            @Override
            public void onResponse(Call<List<UserQuestionData>> call, Response<List<UserQuestionData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<UserQuestionData> userQuestionData = response.body();
                    for (UserQuestionData data : userQuestionData) {
                        if (data.get_qes_id() == questionId && data.getUser_data_id() == userId) {
                            if(data.get_cor()){
                                imageView.setOnClickListener(v -> {
                                    card.setVisibility(View.VISIBLE);
                                    zukan.setVisibility(View.GONE);
                                    String card = question.getCard().replace("\"", "").trim();
                                    int cardResId = getResources().getIdentifier(card, "drawable", getPackageName());
                                    Glide.with(garally.this)
                                            .load(cardResId)
                                            .into(card_image);
                            });
                            }
                            else if (!data.get_cor()) {
                                displayTextOverlay(lay_txt, "?", 0xAA000000, card_lay);
                            }
                            return;
                        }
                    }
                    // qes_idがUserQuestionDataに存在しない場合は"?"オーバーレイを適用
                    displayTextOverlay(new TextView(garally.this), "?", 0xFF000000, card_lay);
                }
            }

            @Override
            public void onFailure(Call<List<UserQuestionData>> call, Throwable t) {
                Log.e("API Request Failure", "Error: ", t);
            }
        });
    }

    private void displayTextOverlay(TextView lay_txt, String text, int backgroundColor, ViewGroup parent) {
        lay_txt.setText(text);
        lay_txt.setTextSize(40);
        lay_txt.setGravity(Gravity.CENTER);
        lay_txt.setWidth(300);
        lay_txt.setHeight(300);
        lay_txt.setBackgroundColor(backgroundColor);
        parent.addView(lay_txt);
    }

    private void SetBackgroundColor(Question question, ImageView imageView){
        switch (question.getGenre_id()) {
            case 1:
                imageView.setBackgroundColor(ContextCompat.getColor(garally.this, R.color.food));
                break;
            case 2:
                imageView.setBackgroundColor(ContextCompat.getColor(garally.this, R.color.build));
                break;
            case 3:
                imageView.setBackgroundColor(ContextCompat.getColor(garally.this, R.color.chara));
                break;
            case 4:
                imageView.setBackgroundColor(ContextCompat.getColor(garally.this, R.color.place));
                break;
            case 5:
                imageView.setBackgroundColor(ContextCompat.getColor(garally.this, R.color.culture));
                break;
        }
    }
    private RelativeLayout SetImage (Question question, int userId){
        ImageView imageView = new ImageView(this);
        String img = question.getImg().replace("\"", "").trim();
        int imageResId = getResources().getIdentifier(img, "drawable", getPackageName());
        Glide.with(this).load(imageResId).into(imageView);
        SetBackgroundColor(question, imageView);
        RelativeLayout card_lay = new RelativeLayout(this);
        TextView lay_txt = new TextView(this);
        card_lay.addView(imageView);
        applyOverlayBasedOnUserQuestionData(question.getId(), userId, card_lay,lay_txt,imageView,question);
        return card_lay;
    }

    private GridLayout.LayoutParams SetGridLayout(){
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 300;  // 画像の幅
        params.height = 300; // 画像の高さ
        params.setGravity(Gravity.CENTER);
        return params;

    }

    public void back_zukan(View view) {
        zukan.setVisibility(View.VISIBLE);
        card.setVisibility(View.GONE);
    }


    //    ホームデータ遷移
    public void zukan_store(View view){
        Intent intent = new Intent(garally.this, store.class);
        startActivity(intent);
    }
    public void zukan_introduce(View view){
        Intent intent = new Intent(garally.this, introduce.class);
        startActivity(intent);
    }
    public void zukan_home(View view){
        Intent intent = new Intent(garally.this, activity_home.class);
        startActivity(intent);
    }
    public void zukan_deco(View view){
        Intent intent = new Intent(garally.this, decoration.class);
        startActivity(intent);
    }
}


