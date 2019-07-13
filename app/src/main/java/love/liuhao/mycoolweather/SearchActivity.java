package love.liuhao.mycoolweather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.basic.Basic;
import interfaces.heweather.com.interfacesmodule.bean.search.Search;
import interfaces.heweather.com.interfacesmodule.bean.weather.Weather;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import love.liuhao.mycoolweather.Presenter.ListDataSave;
import love.liuhao.mycoolweather.Presenter.util.HttpUtil;
import love.liuhao.mycoolweather.Presenter.util.SearchListAdapter;
import love.liuhao.mycoolweather.db.SearchCityContent;
import love.liuhao.mycoolweather.db.TopCity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static love.liuhao.mycoolweather.Presenter.util.Utility.handleTopCityResponse;

public class SearchActivity extends AppCompatActivity {
   // SearchFragment searchFragment;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    SearchView searchView;
    private SearchListAdapter madapter;
    public static List<Basic> basics=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView=findViewById(R.id.search_list);
        progressBar = (ProgressBar) findViewById(R.id.search_progressBar);
        searchView=findViewById(R.id.SearchView);
        searchView.onActionViewExpanded();
        madapter=new SearchListAdapter(basics);
        recyclerView.setAdapter(madapter);
        madapter.setOnItemClickListener(new SearchListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d(String.valueOf(position), "onItemClick: position");
                String location =madapter.getmValues(). get(position).getLocation();
                Log.d(location, "onItemClick: location");
                Intent intent=new Intent(getBaseContext(),WeatherActivity.class);
                ListDataSave listDataSave=new ListDataSave(getApplication(),"data");
                List<String> result1 = new ArrayList<>();
                result1.add(location);
                listDataSave.setDataList("location",result1);
                startActivity(intent);
                finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)); //上下文、方向、是否倒序
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if(TextUtils.isEmpty(s)){
                    recyclerView.setVisibility(View.INVISIBLE);
                }else{
                    Log.d(s, "onQueryTextChange: s");
                    requestCity(s);
                    recyclerView.setAdapter(madapter);
                }
                return false;
            }
        });
    }
    private void requestCity(String location){
        progressBar.setVisibility(View.VISIBLE);
        HeWeather.getSearch(this,location,"CN",5 ,Lang.CHINESE_SIMPLIFIED, new HeWeather.OnResultSearchBeansListener() {
            @Override
            public void onError(Throwable throwable) {
                recyclerView.setVisibility(View.VISIBLE);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getBaseContext(), "热门城市加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onSuccess(Search search) {
                madapter.setmValues(search.getBasic());
              //  Log.d(String.valueOf(search.getBasic().size()), "onSuccess: search.getBasic().size()");
                madapter.notifyDataSetChanged();
               // Log.d(String.valueOf(madapter.getItemCount()), "onSuccess: Count()");
                Gson gson= new Gson();
                String a =gson.toJson(search);
                Log.d(a, "onSuccess: a");
                progressBar.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

}
