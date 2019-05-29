package love.liuhao.mycoolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by hasee on 2019-05-19.
 */
public class Province extends DataSupport {
    private int id;
    private  String provinceName;
    private int provinceCode;

    public int getId() {
        return id;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
