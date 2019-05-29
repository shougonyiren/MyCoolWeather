package love.liuhao.mycoolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hasee on 2019-05-26.
 */
public class Basic {
    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String weatherId;

    public Update update;
    public  class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}

