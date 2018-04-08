package jp.ac.dendai.im.cps.asyncsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import jp.ac.dendai.im.cps.asyncsample.entity.WeatherEntity;

// Weather Hacks API(http://weather.livedoor.com/weather_hacks/webservice)から天気のデータを取得、表示します
// Androidではメインスレッドで非同期通信を行う事ができないため、非同期処理のライブラリを活用する必要があります

// 今回は非同期処理にAsyncTaskクラス、その中でのHttp通信にOkHttp3というライブラリを使っています

// 興味がある方は、Androidでの非同期処理について調べてみて下さい
// (ThreadやAsyncTask、RxJava2やOkHttp3、Retrofit2、KotlinにおけるCoroutineなど)

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
            // 天気情報を取得するAsyncTaskの実行
            WeatherEntity weather = new FetchWeatherWithAsyncTask().execute(CITY_ID).get();
            if (weather.getForecasts().isEmpty()) throw new IllegalStateException();

            // 取得した天気情報をTextViewにセット
            textTitle.setText(weather.getTitle());
            textDate.setText(weather.getForecasts().get(0).getDateLabel());
            textForecast.setText(weather.getForecasts().get(0).getTelop());
        } catch (InterruptedException | ExecutionException | IllegalStateException e) {
            e.printStackTrace();
            textTitle.setText("取得に失敗しました");
            textDate.setText("");
            textForecast.setText("");
        }
    }
}
