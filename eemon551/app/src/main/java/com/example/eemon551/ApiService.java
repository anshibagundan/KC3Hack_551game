package com.example.eemon551;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface ApiService {
    @GET("/questions/")
    Call<List<Question>> getAllQuestions();
    @GET("/questions/{id}")
    Call<Question> getQuestionById(@Path("id") int questionId);


    @POST("/userquestiondatas/")
    Call<Void> insertUserQuestionData(@Body UserQuestionData data);

    @GET("/locations/")
    Call<List<Location>> getAllLocations();

    @GET("/genres/")
    Call<List<Genre>> getAllGenres();
}
