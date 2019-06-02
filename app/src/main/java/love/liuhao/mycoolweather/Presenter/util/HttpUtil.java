package love.liuhao.mycoolweather.Presenter.util;

import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by hasee on 2019-05-20.
 */
public class HttpUtil {
    public  static  void  sendOkHttpRequest(String address,okhttp3.Callback callback){
            OkHttpClient client =new OkHttpClient();
            Request request=new Request.Builder().url(address).build();
            client.newCall(request).enqueue(callback);
    }
    public  static  void  sendOkHttpIMGRequest(String  code,okhttp3.Callback callback){
        String Path="https://cdn.heweather.com/cond_icon/"+code+".png";//+100.png
        //1.创建一个okhttpclient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.设置缓存
        CacheControl cacheControl = new CacheControl.Builder()
                .maxAge(30, TimeUnit.DAYS)
                .build();
        //3.创建Request.Builder对象，设置参数，请求方式如果是Get，就不用设置，默认就是Get
        Request request = new Request.Builder() .url(Path) .build();
        okHttpClient.newCall(request).enqueue(callback);
    }
    public  static  void  sendOkHttpBackGroundRequest(String address,okhttp3.Callback callback){
        //1.创建一个okhttpclient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.设置缓存
        CacheControl cacheControl = new CacheControl.Builder()
                .maxAge(1, TimeUnit.DAYS)
                .build();
        //3.创建Request.Builder对象，设置参数，请求方式如果是Get，就不用设置，默认就是Get
        Request request = new Request.Builder() .url(address) .build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
