package jp.ac.dendai.im.cps.asyncsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import jp.ac.dendai.im.cps.asyncsample.entity.WeatherEntity;

// livedoorのWeather Hacks(http://weather.livedoor.com/weather_hacks/webservice)から天気のデータを取得し、表示しています。
// Androidではメインスレッドで非同期通信を行う事ができないため、非同期処理のライブラリを活用する必要があります。

// 非同期処理には、AsyncTask、RxJava2 を用いた例を、
// Http通信には、HttpURLConnection、OkHttp3、Retrofit2 を用いた例を載せました。

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final Integer CITY_ID = 130010;    // Weather Hacksでの東京の地域ID

    private TextView textTitle;
    private TextView textDate;
    private TextView textForecast;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textTitle = findViewById(R.id.textTitle);
        textDate = findViewById(R.id.textDate);
        textForecast = findViewById(R.id.textForecast);
        button = findViewById(R.id.button);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        try {
            FetchWeatherWithAsyncTask fetchWeatherTask = new FetchWeatherWithAsyncTask();
            WeatherEntity weather = fetchWeatherTask.execute(CITY_ID).get();

            if (weather == null) {
                textTitle.setText("取得に失敗しました");
                return;
            }

            // 取得したお天気情報をTextViewにセット
            textTitle.setText(weather.getTitle());
            textDate.setText(weather.getForecasts().get(0).getDateLabel());
            textForecast.setText(weather.getForecasts().get(0).getTelop());
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
        }
    }
}
