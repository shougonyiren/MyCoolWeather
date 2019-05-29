package love.liuhao.mycoolweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.lang.reflect.Method;

import love.liuhao.mycoolweather.gson.Forecast;
import love.liuhao.mycoolweather.gson.Weather;
import love.liuhao.mycoolweather.util.HttpUtil;
import love.liuhao.mycoolweather.util.Utility;
import love.liuhao.mycoolweather.util.setFullScreen;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by hasee on 2019-05-27.
 */
public class WeatherActivity extends AppCompatActivity {
    private ScrollView weatherLayout;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sprotText;


    private ImageView bingPicImg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*        //背景和状态栏融合
        View decorview =getWindow().getDecorView();
        decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);*/

        setContentView(R.layout.activity_weather);

        bingPicImg=findViewById(R.id.bing_pic_img);

        weatherLayout=(ScrollView)findViewById(R.id.weather_layout);
        titleCity=(TextView)findViewById(R.id.title_city);
        titleUpdateTime=(TextView)findViewById(R.id.title_update_time);
        degreeText=(TextView)findViewById(R.id.degree_text);
        weatherInfoText=(TextView)findViewById(R.id.weather_info_text);
        forecastLayout=findViewById(R.id.forecast_layout);
        aqiText=(TextView)findViewById(R.id.api_text);
        pm25Text=(TextView)findViewById(R.id.pm25_text);
        comfortText=(TextView)findViewById(R.id.comfor_text);
        carWashText=(TextView)findViewById(R.id.car_wash_text);
        sprotText=(TextView)findViewById(R.id.sport_text);
        SharedPreferences preference= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString =preference.getString("weather",null);
        String bingPic=preference.getString("bing_pic",null);
        if ((Build.VERSION.SDK_INT >= 28)) {
            // 使用官方api判断
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = 1;
            getWindow().setAttributes(lp);
            //不显示状态栏
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        }
        if (bingPic!=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else {
            loadBingPic();
        }
        if(weatherString!=null){
            //有缓存时直接解析天气数据
            Weather weather= Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        }else {
            //无缓存时去服务器查询天气
            String weatherId=getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }

    }

    /**
     * 加载每日一图
     */
    private void loadBingPic() {
        String requestBingPic="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic=response.body().string();
                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

    /**
     * 根据天气id请求城市天气信息
     * @param weatherId
     */
    private void requestWeather(final  String weatherId) {
        Log.d("weatherId",weatherId);
            String weatherUrl="http://guolin.tech/api/weather?cityid="+weatherId+"&key=bc0418b57b2d4918819d3974ac1285d9";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                     }
                 });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
              final String responseText=response.body().string();
              final Weather weather=Utility.handleWeatherResponse(responseText);
              runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      if(weather!=null&&"ok".equals(weather.status)){
                          SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                          editor.putString("weather",responseText);
                          editor.apply();
                          showWeatherInfo(weather);
                      }else {
                          Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                      }
                  }
              });
            }
        });
    }

    private void showWeatherInfo(Weather weather) {
        String cityName=weather.basic.cityName;
        String updateTime=weather.basic.update.updateTime.split(" ")[1];
        String degree=weather.now.temperature+"°C";
        String weatherInfo=weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        forecastLayout.removeAllViews();
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        for(Forecast forecast:weather.forecastList){
            View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText=(TextView)view.findViewById(R.id.date_text);
            TextView infoText=(TextView)view.findViewById(R.id.info_text);
            TextView maxText=(TextView)view.findViewById(R.id.max__text);
            TextView minText=(TextView)view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if(weather.aqi!=null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort="舒适度"+weather.suggestion.comfort.info;
        String carWash="洗车指数"+weather.suggestion.carWash.info;
        String sport="运动建议"+weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sprotText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }



}
