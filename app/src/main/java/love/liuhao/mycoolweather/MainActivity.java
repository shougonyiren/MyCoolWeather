package love.liuhao.mycoolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import love.liuhao.mycoolweather.gson.Weather;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_main);
        //使用 和风SDK 时，需提前进行账户初始化（全局执行一次即可）
        HeConfig.init("HE1905292257381242", "9d18bcaf984c406691491f4d41e6a1cd");
        HeConfig.switchToFreeServerNode();
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        //if(prefs.getString("weather",null)!=null){
/*            Intent intent=new Intent(this, WeatherActivity.class);
            startActivity(intent);*/
       // }
        Intent intent=new Intent(this, ChooseAreaActivity.class);
        startActivity(intent);
        finish();
    }
}
