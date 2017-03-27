package com.example.naresh.demoproject_1.retrofit;

import com.example.naresh.demoproject_1.models.ListResponse;
import com.example.naresh.demoproject_1.models.QuestionDetailItem;
import com.example.naresh.demoproject_1.models.User;
import com.example.naresh.demoproject_1.models.UserResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by naresh on 24/3/17.
 */

public interface ApiInterface {
    @GET("questions")
    Call<ListResponse<QuestionDetailItem>> getUserDetail(@Query("page") int page,
                                                         @Query("order") String order,
                                                         @Query("sort") String sort,
                                                         @Query("site") String site);
//https://api.stackexchange.com/2.2/users/
//https://api.stackexchange.com/2.2/questions?page=2&order=desc&sort=activity&site=stackoverflow

}