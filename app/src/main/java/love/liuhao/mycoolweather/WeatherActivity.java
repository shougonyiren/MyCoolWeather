package love.liuhao.mycoolweather;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.basic.Basic;
import interfaces.heweather.com.interfacesmodule.bean.weather.Weather;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import love.liuhao.mycoolweather.Presenter.ListDataSave;
import love.liuhao.mycoolweather.Presenter.util.HttpUtil;
import love.liuhao.mycoolweather.service.AutoUpdateService;
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

    private int hour=1*60*1000;//1小时的毫秒数


  //  private LocationClient mLocationClient;
 //   private MyLocationListener myListener = new MyLocationListener();
    @TargetApi(Build.VERSION_CODES.P)
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locations=new ArrayList<>();
        ListDataSave listDataSave=new ListDataSave(getApplication(),"data");
        locations= listDataSave.getDataList("location");

        if(locations.isEmpty()){
          locations.add("auto_ip");
          listDataSave.setDataList("location",locations);
        }
        thislocation=locations.get(0);
        Log.d(thislocation, "onCreate:thislocation");
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
       // bingPicImg.setImageURI(Uri.parse("https://cdn.heweather.com/cond_icon/100.png"));
       loadBingPic();
    }
    @Override
    protected void onStart() {
        locations=new ArrayList<>();
        ListDataSave listDataSave=new ListDataSave(getApplication(),"data");
        locations= listDataSave.getDataList("location");
        if(locations.isEmpty()){
            thislocation=null;
            Toast.makeText(this,"未选择城市",Toast.LENGTH_LONG).show();
        }else {
            thislocation=locations.get(0);
        }
        Log.d(thislocation, "onStart: thislocation");
        requestWeather(thislocation);
        super.onStart();
    }
    private void requestWeather(String location){

        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
        String WeatherString =sharedPreferences.getString(location,"");
        Log.d(location, "requestWeather:");
        Gson gson= new Gson();
        if(!WeatherString.isEmpty()){
            Weather weather=gson.fromJson(WeatherString,Weather.class);
            if(!NeedtoUpdate(weather.getUpdate().getLoc())){
                Log.d("", "requestWeather:成功读取已保存信息 ");
                showWeatherInfo(weather);
                swipeRefreshLayout.setRefreshing(false);
                return;
            }
        }
            HeWeather.getWeather(this,location, Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherDataListBeansListener() {
                @Override
                public void onError(Throwable throwable) {
                    Toast.makeText(getApplication(),"天气加载失败",Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
                @Override
                public void onSuccess(interfaces.heweather.com.interfacesmodule.bean.weather.Weather weather) {
                    showWeatherInfo(weather);
                    //Gson gson= new Gson();
                    String a=gson.toJson(weather);
                    SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(getApplication()).edit();
                    Log.d(weather.getBasic().getCid(), "requestWeather:  onSuccess ");
                   // editor.putString("cond_code",weather.getNow().getCond_code());
                    String c= "icon_"+weather.getNow().getCond_code()+"d";
                    editor.putInt("resid",getRes(c));
                    editor.putString(weather.getBasic().getCid(),a);
                    editor.apply();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
    }

    /**
     * 根据参数查找drawable目录下的文件名
     * @param name  文件名
     * @return
     */
    public int  getRes(String name) {
        ApplicationInfo appInfo = getApplicationInfo();
        int resID = getResources().getIdentifier(name, "drawable", appInfo.packageName);
        Log.d(String.valueOf(resID), "getRes: ");
        return  resID;
    }

    boolean NeedtoUpdate(String  time){
//        Log.d(time, "NeedtoUpdate: time");
        //现在每小时更新
        String [] strings=time.split(" ");
        Calendar calendar = Calendar.getInstance();
        String [] stringtime=strings[1].split(":");
        Integer hour=Integer.valueOf(stringtime[0]);
        if(hour==Calendar.HOUR_OF_DAY){
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    //requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
    /**
     * 加载每日一图
     */
    private void loadBingPic() {
       // bingPicImg.setImageURI();
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
        //开启后台更新
        Intent intent=new Intent(this, AutoUpdateService.class);
        startService(intent);
    }
}
