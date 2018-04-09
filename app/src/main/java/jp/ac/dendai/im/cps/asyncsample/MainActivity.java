package jp.ac.dendai.im.cps.asyncsample;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import jp.ac.dendai.im.cps.asyncsample.entity.WeatherEntity;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// Weather Hacks API(http://weather.livedoor.com/weather_hacks/webservice)から天気のデータを取得、表示する

// Androidではメインスレッドで非同期通信を行う事ができないため、別スレッドで非同期に通信・処理する必要があります
// 今回は非同期処理にAsyncTaskクラス、その中でのHttp通信にOkHttp3というライブラリを使っています
// また、インターネットに接続するには AndroidManifest.xml に権限を追記する必要があります

// 興味がある方は、Androidでの非同期処理について調べてみて下さい
// (AsyncTask、RxJava2やOkHttp3、Retrofit2、KotlinにおけるCoroutineなど)

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final Integer CITY_ID = 130010;    // Weather Hacksでの東京の地域ID
    private TextView textTitle;
    private TextView textDate;
    private TextView textForecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textTitle = findViewById(R.id.textTitle);
        textDate = findViewById(R.id.textDate);
        textForecast = findViewById(R.id.textForecast);

        findViewById(R.id.button).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        try {
//          天気を取得して表示する
            WeatherEntity weather = new FetchWeatherWithAsyncTask().execute(CITY_ID).get();

            if (weather.getForecasts().isEmpty()) {
                Toast.makeText(this, "取得に失敗しました", Toast.LENGTH_SHORT).show();
                return;
            }
            textTitle.setText(weather.getTitle());
            textDate.setText(weather.getForecasts().get(0).getDateLabel());
            textForecast.setText(weather.getForecasts().get(0).getTelop());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

//  AsyncTaskクラスでは、doInBackground()内の処理が別のスレッドで実行されます

    private static class FetchWeatherWithAsyncTask extends AsyncTask<Integer, Void, WeatherEntity> {
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

                Response response = new OkHttpClient()
                        .newCall(request)
                        .execute();

                // 取得結果をJSONとして解析して、それを元にJavaのオブジェクトを構築する
                if (response.body() != null)
                    result = new Gson().fromJson(response.body().string(), WeatherEntity.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}
