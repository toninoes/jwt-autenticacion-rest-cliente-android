package uca.ruiz.antonio.jwtapp;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by toni on 04/06/2018.
 */

public interface MyApiService {

    @Headers("Content-Type: application/json")
    @POST("auth")
    Call<TokenResponse> login(@Body Login login);

    @GET("api/cosas")
    Call<ResponseBody> getCosas(@Header("Authorization") String token);
}
