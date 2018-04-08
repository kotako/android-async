package jp.ac.dendai.im.cps.asyncsample.api;

import jp.ac.dendai.im.cps.asyncsample.entity.WeatherEntity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// Retrofit2 を使用する場合に必要な定義ファイル

public interface WeatherService {
    @GET("/forecast/webservice/json/v1")
    Call<WeatherEntity> weather(@Query("city") int cityId);
}