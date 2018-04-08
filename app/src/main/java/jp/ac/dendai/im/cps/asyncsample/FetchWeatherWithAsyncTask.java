package jp.ac.dendai.im.cps.asyncsample;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import jp.ac.dendai.im.cps.asyncsample.api.WeatherService;
import jp.ac.dendai.im.cps.asyncsample.entity.WeatherEntity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// 非同期処理を実現するためにAsyncTaskを用いた例です。
// doInBackground()内での処理が別のスレッドで実行されます。

// また、Http通信を行うために、HttpURLConnection、OkHttp3、Retrofit2を用いた例を載せてあります。
// doInBackground内のコメントを切り替える事で変更できます(結果は同じです)。

public class FetchWeatherWithAsyncTask extends AsyncTask<Integer, Integer, WeatherEntity> {
    @Override
    protected WeatherEntity doInBackground(Integer... integers) {
        // ここに記述した内容は別スレッドで実行されます

        try {
            // HttpURLConnection
            return fetchWeatherWithHttpURLConnection(integers[0]);

            // OkHttp3
            // return fetchWeatherWithOkHttp(integers[0]);

            // Retrofit2
            // return fetchWeatherWithRetrofit(integers[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private WeatherEntity fetchWeatherWithHttpURLConnection(int cityId) throws IOException {
        String url = "http://weather.livedoor.com/forecast/webservice/json/v1?city=" + String.valueOf(cityId);

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        connection.disconnect();

        return new Gson().fromJson(body.toString(), WeatherEntity.class);
    }

    private WeatherEntity fetchWeatherWithOkHttp(int cityId) throws IOException {
        String url = "http://weather.livedoor.com/forecast/webservice/json/v1?city=" + String.valueOf(cityId);

        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = new OkHttpClient().newCall(request).execute();

        return new Gson().fromJson(response.body().string(), WeatherEntity.class);
    }

    private WeatherEntity fetchWeatherWithRetrofit(int cityId) throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://weather.livedoor.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherService service = retrofit.create(WeatherService.class);

        return service.weather(cityId).execute().body();
    }
}
