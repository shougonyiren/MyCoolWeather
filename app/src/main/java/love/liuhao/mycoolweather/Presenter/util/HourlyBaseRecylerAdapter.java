package love.liuhao.mycoolweather.Presenter.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.HourlyBase;
import love.liuhao.mycoolweather.R;
import love.liuhao.mycoolweather.WeatherActivity;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HourlyBaseRecylerAdapter extends RecyclerView.Adapter<HourlyBaseRecylerAdapter.ViewHolder> {
    List<HourlyBase> hourlyBaseList;
    Context context;
    static  class ViewHolder extends RecyclerView.ViewHolder{
        TextView Time_text;
        TextView Pop_text;
        ImageView Cond_img_view;
        TextView Tmp_text;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Time_text=itemView.findViewById(R.id.Time_text);
            Pop_text=itemView.findViewById(R.id.Pop_text);
            Cond_img_view=itemView.findViewById(R.id.Cond_img_view);
            Tmp_text=itemView.findViewById(R.id.Tmp_text);
        }
    }
    public HourlyBaseRecylerAdapter(Context context,List<HourlyBase> hourlyBaseList) {
        this.context=context;
        this.hourlyBaseList = hourlyBaseList;
    }

/*    android.os.Handler handler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap)msg.obj;
            C.setImageBitmap(bitmap);//将图片的流转换成图片
        }
    };*/
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.hourly_item,viewGroup,false);
        ViewHolder holder =new ViewHolder(view);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        HourlyBase hourlyBase =hourlyBaseList.get(i);
        viewHolder.Cond_img_view.setImageResource(R.drawable.ic_launcher_background);
        HttpUtil.sendOkHttpIMGRequest(hourlyBase.getCond_code(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String img=response.body().string();
                Glide.with(context).load(img).into(viewHolder.Cond_img_view);
            }
        });
    }



    @Override
    public int getItemCount() {
        return hourlyBaseList.size();
    }
}
