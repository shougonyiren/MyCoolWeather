package love.liuhao.mycoolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by hasee on 2019-05-19.
 */
public class City extends DataSupport {
    private  int id;
    private String cityName;
    private  int citycode;
    private  int provinceId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCitycode() {
        return citycode;
    }

    public void setCitycode(int citycode) {
        this.citycode = citycode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
