package love.liuhao.mycoolweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import love.liuhao.mycoolweather.db.City;
import love.liuhao.mycoolweather.db.County;
import love.liuhao.mycoolweather.db.Province;
import love.liuhao.mycoolweather.db.TopCity;
import love.liuhao.mycoolweather.gson.HeWeather6;
import love.liuhao.mycoolweather.gson.Weather;

/**
 * Created by hasee on 2019-05-20.
 */
public class Utility {
    /**
     * 解析处理服务器返回的省级数据
     */
    public static  boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allProvinces=new JSONArray(response);
                for(int i=0;i<allProvinces.length();i++){
                    JSONObject provincesObject=allProvinces.getJSONObject(i);
                    Province provinces=new Province();
                    provinces.setProvinceName(provincesObject.getString("name"));
                    provinces.setProvinceCode(provincesObject.getInt("id"));
                    provinces.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return  false;
    }
    /**
     * 解析处理服务器返回的市级数据
     */
    public static  boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCities=new JSONArray(response);
                for(int i=0;i<allCities.length();i++){
                    JSONObject CityObject=allCities.getJSONObject(i);
                    City city=new City();
                    city.setCityName (CityObject.getString("name"));
                    city.setCitycode(CityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return  false;
    }
    /**
     * 解析和处理服务器返回的县级数据
     */
    public static boolean handleCountyResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * 解析和处理服务器返回的热门城市数据
     */
    public static boolean handleTopCityResponse(String response) {
        try{
            JSONObject jsonObject =new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather6");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            Log.d(weatherContent, "handleTopCityResponse: ");
            List<TopCity>A=new Gson().fromJson(weatherContent,HeWeather6.class).topCitiyLisy;
            for (int i = 0; i < A.size(); i++) {
                TopCity  topCity =new TopCity();
                topCity.setLocation (A.get(i).getLocation());
                topCity.setCid(A.get(i).getCid());
                topCity.save();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 将返回的JSON数据解析为Weather实体类
     */
    public static Weather handleWeatherResponse(String response){
        try{
            JSONObject jsonObject =new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            return  new Gson().fromJson(weatherContent,Weather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
