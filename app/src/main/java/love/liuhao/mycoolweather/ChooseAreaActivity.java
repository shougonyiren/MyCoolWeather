package love.liuhao.mycoolweather;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
import love.liuhao.mycoolweather.db.TopCity;
import love.liuhao.mycoolweather.Presenter.util.HttpUtil;
import love.liuhao.mycoolweather.Presenter.util.MyGridAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static love.liuhao.mycoolweather.Presenter.util.Utility.handleTopCityResponse;

public class ChooseAreaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
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
        gridView.setOnItemClickListener(this);
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

    /**
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
       TopCity topCity=listInfo.get(position);
       String location =topCity.getLocation();
        Intent intent=new Intent(this,WeatherActivity.class);
        intent.putExtra("location",location);
        ListDataSave listDataSave=new ListDataSave(this,"data");

        List <String> staffsList=listDataSave.getDataList("location");
        Set result = new HashSet(staffsList);
        result.add(location);
        List<String> result1 = new ArrayList<>(result);

        listDataSave.setDataList("location",result1);
        startActivity(intent);
    }
}
