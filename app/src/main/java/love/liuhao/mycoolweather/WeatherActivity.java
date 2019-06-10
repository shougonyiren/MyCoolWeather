package love.liuhao.mycoolweather;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.basic.Basic;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import love.liuhao.mycoolweather.Presenter.ListDataSave;
import love.liuhao.mycoolweather.Presenter.util.HttpUtil;
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

 /*    private RecyclerView recyclerView;

   private RecyclerView.LayoutManager mLayoutManager;
    private HourlyBaseRecylerAdapter mAdapter;*/

    //空气污染扩散条件指数
    private TextView aqiTxtText;

   private  TextView aqiBrfText;

    //着装指数
    private TextView drsgTxtText;


    private ImageView bingPicImg;

    public SwipeRefreshLayout swipeRefreshLayout;

    private String thislocation;
    private  List<String> locations;

    private Toolbar toolbar;

    private TextView SunscreenBrf_text;
    private TextView SunscreenTxt_text;


    @TargetApi(Build.VERSION_CODES.P)
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locations=new ArrayList<>();
        ListDataSave listDataSave=new ListDataSave(getApplication(),"data");
        locations= listDataSave.getDataList("location");
        if(locations.isEmpty()){
            Intent intent=new Intent(this,WeatherActivity.class);
            startActivity(intent);
            finish();
        }
        thislocation=locations.get(0);

        setContentView(R.layout.activity_weather);

        bingPicImg=findViewById(R.id.bing_pic_img);

        weatherLayout=(ScrollView)findViewById(R.id.weather_layout);
        titleCity=(TextView)findViewById(R.id.title_city);
        titleUpdateTime=(TextView)findViewById(R.id.title_update_time);
        degreeText=(TextView)findViewById(R.id.degree_text);
        weatherInfoText=(TextView)findViewById(R.id.weather_info_text);
        forecastLayout=findViewById(R.id.forecast_layout);

        aqiBrfText=findViewById(R.id.apiBrf_text);
        aqiTxtText=findViewById(R.id.apiTxt_text);
        drsgTxtText=findViewById(R.id.drsgTxt_text);

        SunscreenBrf_text=findViewById(R.id.SunscreenBrf_text);
        SunscreenTxt_text=findViewById(R.id.SunscreenTxt_text);
        setSupportActionBar(toolbar);//设置toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar11);
        toolbar.inflateMenu(R.menu.menu_choose_addarea);
// Title
        toolbar.setTitle(" ");
// Sub Title
        toolbar.setSubtitle(" ");


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int i=menuItem.getItemId();
                switch (i){
                    case R.id.action_settings: {
                        Intent intent= new Intent(getBaseContext(),ChooseAreaActivity.class);
                        startActivity(intent);
                    }
                }
                return false;
            }
        });


        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(thislocation);
            }
        });

        if ((Build.VERSION.SDK_INT >= 28)) {
            // 使用官方api判断
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = 1;
            getWindow().setAttributes(lp);
/*
            //不显示状态栏
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
*/
        }
        loadBingPic();

      //  requestWeather(thislocation);
    }
    @Override
    protected void onStart() {
        Log.d("here", "onPostResume: ");
        locations=new ArrayList<>();
        ListDataSave listDataSave=new ListDataSave(getApplication(),"data");
        locations= listDataSave.getDataList("location");
        if(locations.isEmpty()){
            Intent intent=new Intent(this,WeatherActivity.class);
            startActivity(intent);
            finish();
        }
        thislocation=locations.get(0);
        requestWeather(thislocation);
        super.onStart();
    }
    private void requestWeather(String location){
        HeWeather.getWeather(this, location, Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherDataListBeansListener() {
            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(getApplication(),"天气加载失败",Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onSuccess(interfaces.heweather.com.interfacesmodule.bean.weather.Weather weather) {
                showWeatherInfo(weather);
                Gson gson= new Gson();
                String a=gson.toJson(weather);
                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString(weather.getBasic().getCid(),a);
                editor.apply();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
 /*    private void InitRecyclerView(interfaces.heweather.com.interfacesmodule.bean.weather.Weather weather) {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mAdapter = new HourlyBaseRecylerAdapter(this,weather.getHourly());

        recyclerView = (RecyclerView) findViewById(R.id.HourlyBase);
        // 设置布局管理器
        recyclerView .setLayoutManager(mLayoutManager);
        // 设置adapter
        recyclerView .setAdapter(mAdapter);
    }*/

    /**
     * 加载每日一图
     */
    private void loadBingPic() {
        String requestBingPic="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpBackGroundRequest(requestBingPic,this, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("背景加载失败",  "onFailure: ");
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

    private void showWeatherInfo(interfaces.heweather.com.interfacesmodule.bean.weather.Weather weather) {
        Basic  basic=weather.getBasic();
        NowBase nowBase=weather.getNow();
        String cityName=basic.getLocation();
        String updateTime=weather.getUpdate().getLoc();
        String degree=nowBase.getTmp()+"°C";
        String weatherInfo=nowBase.getCond_txt();
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        forecastLayout.removeAllViews();
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
/*        if(weather.getHourly()!=null){
            InitRecyclerView(weather);
        }*/
            forecastLayout.removeAllViews();
      for(ForecastBase forecast:weather.getDaily_forecast()){
            View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText=(TextView)view.findViewById(R.id.date_text);
            TextView infoText=(TextView)view.findViewById(R.id.info_text);
            TextView max_and_minText=(TextView)view.findViewById(R.id.max_and_min__text);
            String[]  strs=forecast.getDate().split("-");
            dateText.setText(strs[1]+"月"+strs[2]+"日");
            infoText.setText(forecast.getCond_txt_d());
            max_and_minText.setText(forecast.getTmp_max()+"°C/"+forecast.getTmp_min()+"°C");
            forecastLayout.addView(view);
        }
        //生活建议
        String  comfort1=weather.getLifestyle().get(1).getTxt();
        //空气质量
        String  aqi1=weather.getLifestyle().get(7).getBrf();
        String  aqi=weather.getLifestyle().get(7).getTxt();


        //紫外线强度
        String  Sunscreen=weather.getLifestyle().get(5).getBrf();
        String  Sunscreen1=weather.getLifestyle().get(5).getTxt();

        SunscreenBrf_text.setText(Sunscreen);
        SunscreenTxt_text.setText(Sunscreen1);

        aqiBrfText.setText(aqi1);
        aqiTxtText.setText(aqi);
        drsgTxtText.setText(comfort1);
        weatherLayout.setVisibility(View.VISIBLE);
    }
}
