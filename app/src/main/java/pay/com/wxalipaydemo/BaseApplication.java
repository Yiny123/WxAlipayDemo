package pay.com.wxalipaydemo;/**
 * Created by Administrator on 2018/5/25.
 */

import android.app.Application;
import android.content.Context;

/**
 * Created by liny on 2018/5/25. 21:31
 */
public class BaseApplication extends Application {
    private BaseApplication mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }
}
