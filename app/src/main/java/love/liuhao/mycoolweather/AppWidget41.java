package love.liuhao.mycoolweather;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import interfaces.heweather.com.interfacesmodule.bean.weather.Weather;
import love.liuhao.mycoolweather.Presenter.ListDataSave;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget41 extends WeatherWidget21 {
    /**
     * Constructor to initialize AppWidgetProvider.
     */
    public AppWidget41() {

        super();
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("KK:mm", Locale.CHINA);//, Locale.CHINA
        SimpleDateFormat   formatter1   =   new   SimpleDateFormat   ("MM月dd日  E",Locale.CHINA);//, Locale.CHINA
        Date curDate =  new Date(System.currentTimeMillis());
        String   time   =   formatter.format(curDate);
        String   date  =   formatter1.format(curDate);
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
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget41);

        String code= weather.getNow().getCond_code();
        Log.d(code, "updateAppWidget:code ");

        views.setImageViewResource(R.id.imageViewwidget,resid);
        views.setTextViewText(R.id.textViewwidget, weather.getNow().getTmp()+"°C");
        views.setTextViewText(R.id.textViewwidget1, text1);
        views.setTextViewText(R.id.dateclocktext,date);
        views.setTextViewText(R.id.timeclocktext,time);
        Intent skipIntent = new Intent(context, MainActivity.class);
        Intent AlarmClockIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
        PendingIntent pi = PendingIntent.getActivity(context, 200, skipIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent p1 = PendingIntent.getActivity(context, 123, AlarmClockIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.buttontomain, pi);
        views.setOnClickPendingIntent(R.id.toAlarmButton,p1);//跳转到闹钟界面
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
/*        String action1 = intent.getAction();
        if(action1.equals("test")){
            String action = intent.getAction();
            if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    int[] appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
                    if (appWidgetIds != null && appWidgetIds.length > 0) {
                        this.onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);
                    }
                }
        }else{*/
            super.onReceive(context, intent);
       // }
    }

/*    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }*/


}

