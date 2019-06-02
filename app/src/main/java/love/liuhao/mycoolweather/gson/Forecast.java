package love.liuhao.mycoolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hasee on 2019-05-26.
 */
public class Forecast {
    public  String date;//预报日期
    public String Tmp_max;//最高温度
    public String Tmp_min;//最低温度
    public String getCond_txt_d;//白天天气状况
    public int  Cond_code_d;//白天天气代码
    public String getCond_txt_n;//晚上天气状况
    public int  Cond_code_n;//晚上天气代码
}
