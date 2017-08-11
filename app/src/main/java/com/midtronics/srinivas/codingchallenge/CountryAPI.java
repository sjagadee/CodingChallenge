package com.midtronics.srinivas.codingchallenge;

import com.midtronics.srinivas.codingchallenge.model.Country;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by srinivas on 6/22/17.
 *
 * This is an Interface method to perform possible API calls
 * each method would contain Base URL end point annotation that represent
 * http methods such as GET, POST etc
 *
 * The return type would be instance of Call class
 */

public interface CountryAPI {

    @Headers("Content-Type: application/json")
    @GET("name/{name}")
    Call<List<Country>> getCountry(@Path("name") String name);
}
