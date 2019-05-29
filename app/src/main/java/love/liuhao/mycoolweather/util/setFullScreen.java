package love.liuhao.mycoolweather.util;

import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by hasee on 2019-05-29.
 */
public class setFullScreen {
    //在Activity里面通过getWindow()获取window参数，然后再onCreate()函数里面调用下面的函数

    public static void setFullScreenWindowLayout(Window window) {

        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        //设置页面全屏显示
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        //设置页面延伸到刘海区显示
        window.setAttributes(lp);
    }

}
