package com.example.eemon551;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface ApiService {
    @GET("/questions/")
    Call<List<Question>> getAllQuestions();

    @POST("/userquestiondatas/")
    Call<Void> insertUserQuestionData(@Body UserQuestionData data);
}
