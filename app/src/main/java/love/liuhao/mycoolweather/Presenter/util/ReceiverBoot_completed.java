package love.liuhao.mycoolweather.Presenter.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import love.liuhao.mycoolweather.WeatherActivity;
import love.liuhao.mycoolweather.service.AutoUpdateService;

public class ReceiverBoot_completed extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.d("开机启动", "onReceive: ");
        Intent i=new Intent(context, AutoUpdateService.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(i);
        Toast.makeText(context,"开机启动",Toast.LENGTH_LONG).show();
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
