package jp.ac.dendai.im.cps.asyncsample;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;

import jp.ac.dendai.im.cps.asyncsample.entity.WeatherEntity;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// 非同期処理を実現するためにAsyncTaskを用いた例
// AsyncTaskクラスでは、doInBackground()内の処理は別のスレッドで実行される

public class FetchWeatherWithAsyncTask extends AsyncTask<Integer, Integer, WeatherEntity> {

    @Override
    protected WeatherEntity doInBackground(Integer... integers) {
        WeatherEntity result = new WeatherEntity();

        try {
            // OkHttp3を用い、Weather Hacks APIから天気情報を取得
            HttpUrl url = new HttpUrl.Builder()
                    .scheme("http")
                    .host("weather.livedoor.com")
                    .addPathSegments("forecast/webservice/json/v1")
                    .addQueryParameter("city", String.valueOf(integers[0]))
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = new OkHttpClient().newCall(request).execute();

            // 取得結果をJSONとして解析、それを元にJavaのオブジェクトを構築
            if (response.body() != null)
                result = new Gson().fromJson(response.body().string(), WeatherEntity.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
