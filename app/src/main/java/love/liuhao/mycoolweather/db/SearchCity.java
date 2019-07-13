package love.liuhao.mycoolweather.db;

import java.util.ArrayList;
import java.util.List;

public class SearchCity {
    private  String cid;

    private  String location;
    private  String admin_area;
    private  String cnty;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAdmin_area() {
        return admin_area;
    }

    public void setAdmin_area(String admin_area) {
        this.admin_area = admin_area;
    }

    public String getCnty() {
        return cnty;
    }

    public void setCnty(String cnty) {
        this.cnty = cnty;
    }

    @Override
    public String toString() {
        return  location + "," + admin_area;
    }
}
