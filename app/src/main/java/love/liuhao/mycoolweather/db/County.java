package love.liuhao.mycoolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by hasee on 2019-05-19.
 */
public class County extends DataSupport {
    private  int id;
    private String countName;
    private  String  weatherId;
    private  int CityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountName() {
        return countName;
    }

    public void setCountName(String countName) {
        this.countName = countName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId(int cityId) {
        return CityId;
    }

    public void setCityId(int cityId) {
        CityId = cityId;
    }
}
