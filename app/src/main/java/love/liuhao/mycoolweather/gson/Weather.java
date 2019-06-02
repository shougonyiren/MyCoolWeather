package love.liuhao.mycoolweather.gson;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.basic.Update;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;

/**
 * Created by hasee on 2019-05-26.
 */
public class Weather  extends DataSupport {
    public String status;
    public Basic basic;
    public Update update;
    public List<Forecast> forecastList;
    public NowBase nowBase;
    public List<HourlyBase> HourlyBaseList;
}

