package love.liuhao.mycoolweather;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.google.gson.Gson;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.weather.Weather;
import love.liuhao.mycoolweather.Presenter.ListDataSave;

/**
 * Implementation of App Widget functionality.
 */
public class WeatherWidget21 extends AppWidgetProvider {
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId)  {
        List<String> locations;
        ListDataSave listDataSave=new ListDataSave(context,"data");
        locations= listDataSave.getDataList("location");
        String location=locations.get(0);
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        String WeatherString =sharedPreferences.getString(location,"");

        int resid=sharedPreferences.getInt("resid",100);
        Gson gson= new Gson();
        if(WeatherString.isEmpty()){
            return;
        }
        Weather weather=gson.fromJson(WeatherString,Weather.class);
        // Construct the RemoteViews object
        String text1= weather.getDaily_forecast().get(0).getTmp_max()+"°C/"+weather.getDaily_forecast().get(0).getTmp_min()+"°C";
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget21);
        String code= weather.getNow().getCond_code();
        Log.d(code, "updateAppWidget:code ");
        views.setImageViewResource(R.id.imageViewwidget,resid);
        views.setTextViewText(R.id.textViewwidget, weather.getNow().getTmp()+"°C");
        views.setTextViewText(R.id.textViewwidget1, text1);
        Intent skipIntent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 200, skipIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.buttontomain, pi);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

