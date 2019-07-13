package love.liuhao.mycoolweather.db;

import java.util.ArrayList;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.Base;
import interfaces.heweather.com.interfacesmodule.bean.basic.Basic;
import interfaces.heweather.com.interfacesmodule.bean.search.Search;

public class SearchCityContent {

    public static void setITEMS(Search  search) {
        SearchCityContent.ITEMS = ITEMS;
    }

    /**
     * An array of sample (dummy) items.
     */

    public static  List<Basic> ITEMS = new ArrayList<Basic>();
}
