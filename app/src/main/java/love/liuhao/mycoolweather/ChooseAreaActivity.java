package love.liuhao.mycoolweather;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import love.liuhao.mycoolweather.View.MyGridView;
import love.liuhao.mycoolweather.db.County;
import love.liuhao.mycoolweather.db.TopCity;
import love.liuhao.mycoolweather.util.HttpUtil;
import love.liuhao.mycoolweather.util.MyGridAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static love.liuhao.mycoolweather.ChooseAreaFragment.LEVEL_COUNTY;
import static love.liuhao.mycoolweather.util.Utility.handleTopCityResponse;

public class ChooseAreaActivity extends AppCompatActivity {
    MyGridView gridView;
    LayoutInflater inflater;
    List<TopCity> listInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_area);

        gridView = findViewById(R.id.TopGridView);
        inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listInfo = new ArrayList<TopCity>();
        queryTopCity();

    }
    /**
     * 查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryTopCity() {
/*      titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);*/
        listInfo = DataSupport.findAll(TopCity.class);
        Log.d(String.valueOf(listInfo.size()), "queryTopCity: listInfo.size() ");
        if (listInfo.size() > 0) {
            gridView.setAdapter(new MyGridAdapter(this, listInfo));
        } else {
            queryTopCityFromServer();
        }
    }
    private void queryTopCityFromServer(){
        String address="https://search.heweather.net/top?group=cn&key=9d18bcaf984c406691491f4d41e6a1cd&number=39";
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "热门城市加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                if( handleTopCityResponse(responseText)){
                    queryTopCity();
                }
            }
        });

    }
}
