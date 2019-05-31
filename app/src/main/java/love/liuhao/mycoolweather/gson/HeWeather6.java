package love.liuhao.mycoolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import love.liuhao.mycoolweather.db.TopCity;

public class HeWeather6 {
    public  String status;
    @SerializedName("basic")
    public  List<TopCity> topCitiyLisy;
}
