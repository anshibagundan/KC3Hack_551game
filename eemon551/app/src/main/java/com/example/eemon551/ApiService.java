package com.example.eemon551;

import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;

public interface ApiService {
    @GET("/questions")
    Call<List<Question>> getAllQuestions();
}
