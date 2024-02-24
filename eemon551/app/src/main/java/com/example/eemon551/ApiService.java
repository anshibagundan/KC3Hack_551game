package com.example.eemon551;

import android.service.autofill.UserData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface ApiService {
    @GET("/questions/")
    Call<List<Question>> getAllQuestions();

    @GET("/questions/{id}/")
    Call<Question> getQuestionById(@Path("id") int questionId);


    @GET("/userquestiondatas/")
    Call<List<UserQuestionData>> getUserQuestionData(@Query("user_data_id") int user_data_id);

    @PUT("/userdatas/{id}/")
    Call<Void> updateUserData(@Path("id") int userId, @Body UserUpdateRequest userUpdateRequest);

    class UserUpdateRequest {
        final String name;
        final int money;

        public UserUpdateRequest(String name, int money) {
            this.name = name;
            this.money = money;
        }
    }


    @POST("/userquestiondatas/")
    Call<Void> insertUserQuestionData(@Body UserQuestionData data);

    @GET("locations/{id}/")
    Call<Location> getLocationById(@Path("id") int locationId);

    @GET("/userdatas/{id}/")
    Call<User> getUser(@Path("id") int userId);

    @PUT("/userquestiondatas/update")
    Call<Void> updateUserQuestionData(@Body UserQuestionDataUpdateRequest request);

    @PUT("/userdatas/update/{id}")
    Call<Void> updateUserDataName(@Path("id") int userId,@Body UserDataNameUpdateRequest request);

    @PUT("/usertitles/update")
    Call<Void> updateUserTitleData(@Body UserTitleUpdateRequest request);

    @PUT("/userbackgrounds/update")
    Call<Void> updateUserBackgroundData(@Body UserBackgroundUpdateRequest request);

    @POST("/userdatas/")
    Call<Void> insertUserData(@Body User data);
    @POST("/usertitles/")
    Call<Void> insertUserTitle(@Body UserTitles data);

    @POST("/userbackgrounds/")
    Call<Void> insertUserbackground(@Body UserBackground data);

    @GET("api/user-id/")
    Call<User> getUserId(@Query("name") String name, @Query("level") int level, @Query("money") int money);

    @GET("/titles/{id}/")
    Call<Titles> getTitle(@Path("id") int titleId);
    @GET("/usertitles/")
    Call<List<UserTitles>> getUserTitles(@Query("user_data_id") int user_data_id);

    @GET("/backgrounds/{id}/")
    Call<background> getBackgrounds(@Path("id") int backgroundId);
    @GET("/userbackgrounds/")
    Call<List<UserBackground>> getUserBackgrounds(@Query("user_data_id") int user_data_id);

    @PUT("/userbackgrounds/updateUseStatus")
    Call<Void> updateUserBackgroundUseStatus(@Body UserBackgroundUpdateRequest request);

    @PUT("/usertitles/updateUseStatus")
    Call<Void> updateUserTitleUseStatus(@Body UserTitleUpdateRequest request);

}

