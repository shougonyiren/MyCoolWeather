package love.liuhao.mycoolweather;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import love.liuhao.mycoolweather.Presenter.ListDataSave;
import love.liuhao.mycoolweather.View.MyGridView;
import love.liuhao.mycoolweather.db.SearchCity;
import love.liuhao.mycoolweather.db.TopCity;
import love.liuhao.mycoolweather.Presenter.util.HttpUtil;
import love.liuhao.mycoolweather.Presenter.util.MyGridAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static love.liuhao.mycoolweather.Presenter.util.Utility.handleTopCityResponse;

public class ChooseAreaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    MyGridView mgridView;
    LayoutInflater minflater;
    List<TopCity> mlistInfo;
    Button back_button;
    Toolbar toolbarchoose;
    Button buttonToSearch;
    MyGridAdapter myGridAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_area);
        toolbarchoose=findViewById(R.id.toolbarchoose);
        gridView = findViewById(R.id.TopGridView1);
        buttonToSearch=findViewById(R.id.buttonToSearch);
        buttonToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
        inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listInfo = new ArrayList<TopCity>();
        myGridAdapter=new MyGridAdapter(this,listInfo);
        gridView.setOnItemClickListener(this);
        gridView.setAdapter(myGridAdapter);
        back_button=findViewById(R.id.weather_back1_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(),WeatherActivity.class);
                startActivity(intent);
            }
        });
        queryTopCity();
    }
    /**
     * 查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryTopCity() {
        mlistInfo = DataSupport.findAll(TopCity.class);
        Log.d(String.valueOf(mlistInfo.size()), "queryTopCity: mlistInfo.size() ");
        if (mlistInfo.size() > 0) {
            mgridView.setAdapter(new MyGridAdapter(this, mlistInfo));
        } else {
            queryTopCityFromServer();
        }
        myGridAdapter.notifyDataSetChanged();
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

    /**传的是城市代码
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       TopCity topCity=mlistInfo.get(position);
       String location =topCity.getCid();
        Intent intent=new Intent(this,WeatherActivity.class);
        ListDataSave listDataSave=new ListDataSave(getApplication(),"data");
        List<String> result1 = new ArrayList<>();
        result1.add(location);
        listDataSave.setDataList("location",result1);
        startActivity(intent);
    }
}
