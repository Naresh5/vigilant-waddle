package com.example.naresh.demoproject_1.retrofit;

import com.example.naresh.demoproject_1.models.ListResponse;
import com.example.naresh.demoproject_1.models.QuestionDetailItem;
import com.example.naresh.demoproject_1.models.TagItem;
import com.example.naresh.demoproject_1.models.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by naresh on 24/3/17.
 */


public interface ApiInterface {

    /* @GET("questions")
       Call<ListResponse<QuestionDetailItem>> getUserDetail(@Query("page") int page,
                                                            @Query("order") String order,
                                                            @Query("sort") String sort,
                                                            @Query("fromdate") String fromDate,
                                                            @Query("todate") String toDate,
                                                            @Query("site") String site);*/
    @GET("users")
    Call<ListResponse<User>> getUserList(@Query("page") int page,
                                         @Query("order") String order,
                                         @Query("sort") String sort,
                                         @Query("fromdate") String fromdate,
                                         @Query("todate") String todate,
                                         @Query("inname") String inname,
                                         @Query("site") String site);

    @GET("questions")
    Call<ListResponse<QuestionDetailItem>> getQuestionsList(@Query("page") int page,
                                                            @Query("order") String order,
                                                            @Query("sort") String sort,
                                                            @Query("fromdate") String fromdate,
                                                            @Query("todate") String todate,
                                                            @Query("tagged") String tagged,
                                                            @Query("site") String site);

    @GET("search")
    Call<ListResponse<QuestionDetailItem>> getFilterQuestionList(@Query("page") int page,
                                                                 @Query("order") String order,
                                                                 @Query("sort") String sort,
                                                                 @Query("fromdate") String fromdate,
                                                                 @Query("todate") String todate,
                                                                 @Query("intitle") String intitle,
                                                                 @Query("site") String site);

    @GET("tags")
    Call<ListResponse<TagItem>> getTagList(@Query("page") int page,
                                           @Query("order") String order,
                                           @Query("sort") String sort,
                                           @Query("inname") String inname,
                                           @Query("site") String site);

//https://api.stackexchange.com/2.2/users/
//https://api.stackexchange.com/2.2/questions?page=2&order=desc&sort=activity&site=stackoverflow
// Question Search
//https://api.stackexchange.com/2.2/search?order=desc&sort=activity&intitle=ionic&site=stackoverflow
}
