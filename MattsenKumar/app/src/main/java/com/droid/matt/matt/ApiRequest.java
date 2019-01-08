package com.droid.matt.matt;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiRequest {

    String Base_URL = "https://pixabay.com/";

    @GET("api/")
    Call<List<Images>> getDataFromUrl(@Query("key") String key);
}
