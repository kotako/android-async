package jp.ac.dendai.im.cps.asyncsample;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;

import jp.ac.dendai.im.cps.asyncsample.entity.WeatherEntity;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// 非同期処理を実現するためにAsyncTaskを用いた例です。
// doInBackground()内での処理が別のスレッドで実行されます。

// また、Http通信を行うために、HttpURLConnection、OkHttp3、Retrofit2を用いた例を載せてあります。
// doInBackground内のコメントを切り替える事で変更できます(結果は同じです)。

public class FetchWeatherWithAsyncTask extends AsyncTask<Integer, Integer, WeatherEntity> {
    @Override
    protected WeatherEntity doInBackground(Integer... integers) {
        // ここに記述した内容は別スレッドで実行されます

        try {
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
            return new Gson().fromJson(response.body().string(), WeatherEntity.class);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
