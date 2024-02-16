package com.example.eemon551;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.List;

public interface ApiService {
    @GET("/questions/")
    Call<List<Question>> getAllQuestions();

    @GET("/questions/{id}")
    Call<Question> getQuestionById(@Path("id") int questionId);

    @POST("/userquestiondatas/")
    Call<Void> insertUserQuestionData(@Body UserQuestionData data);

    @GET("locations/{id}/")
    Call<Location> getLocationById(@Path("id") int locationId);

    @GET("/userdatas/{id}/")
    Call<User> getUserMoney(@Path("id") int money);

    @PUT("/userdatas/{id}/")
    Call<Void> updateUserScore(@Path("id") int userId, @Body ScoreUpdateRequest scoreUpdateRequest);

    // スコア更新リクエストのボディを定義するクラス
    class ScoreUpdateRequest {
        final int score;

        public ScoreUpdateRequest(int score) {
            this.score = score;
        }
    }
}
