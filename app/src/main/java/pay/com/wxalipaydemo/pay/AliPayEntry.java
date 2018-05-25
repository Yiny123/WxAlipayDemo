package pay.com.wxalipaydemo.pay;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.alipay.sdk.app.PayTask;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

/**
 * Created by liny 2017年3月8日
 */
public class AliPayEntry extends PayEntry {
    private static final String TAG = "AliPayEntry";

    // 订单支付成功
    public static final int CODE_9000 = 9000;
    // 正在处理中
    public static final int CODE_8000 = 8000;
    // 订单支付失败
    public static final int CODE_4000 = 4000;
    // 用户中途取消
    public static final int CODE_6001 = 6001;
    // 网络出现错误
    public static final int CODE_6002 = 6002;

    private static AliPayEntry instance;
    private Activity mActivity;

    public synchronized static AliPayEntry getInstance() {
        if (instance == null) {
            instance = new AliPayEntry();
        }

        return instance;
    }

    private AliPayEntry() {
        super();
    }

    private String mModel;

    @Override
    public void setModel(Object obj) {
        mModel = (String) obj;
    }
    public void setActivity(Activity act) {
        mActivity = act;
    }

    @Override
    public void pay() {
        if (isPaying())
            return;

        payStart();
        ThreadManager.executeOnNetWorkThread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 此处也可以在baseactivity里面获取栈顶activity
                 * BaseActivity.sTopActivity.  省了每次写setActivity();
                 */
                PayTask payTask = new PayTask(mActivity);
                Map<String, String> result = payTask.payV2(mModel, true);
                PayResult payResult = new PayResult(result);
                payResult(payResult);
            }
        });
       
    }



    @Override
    public void pay(String tokenId, final Context context) {
        super.pay(tokenId, context);
    }


    private void payResult(final PayResult result) {
        int code = CODE_4000;
        try {
            code = Integer.parseInt(result.getResultStatus());
        } catch (Exception e) {
            Log.e(TAG, "pase resultStatus " + " ex." + e.toString());
        }
        final int finalCode = code;
        ThreadManager.getMainThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                notifyResult(Integer.valueOf(finalCode), result.getResult());
            }
        });
        Log.d(TAG, "alipay result : " + result);
    }

    @Override
    public void notifyResult(int errCode, String result) {
        payEnd();
        for (WeakReference<OnPayListener> wr : mListeners) {
            if (wr.get() != null) {
                wr.get().onPayResult(PayEntry.ENTRY_ALI, errCode, result);
            }
        }
    }



    /**
     * 检查支付包是否存在
     *
     * @param context
     * @return
     * @deprecated 此方法已过时, aliapp 已经不是当初那个单纯的app了,你变了
     */
    private boolean isZfbAvilible(Context context) {
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                System.out.println(pinfo.get(i).packageName);
                if (pn.equals("com.alipay.android.app")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 主要采用intent意图匹配间接实现检测支付宝客户端是否安装,代码如下:
     *
     * @param context 检查支付包是否存在
     * @return true 存在  false 不存在
     */

    public static boolean isAliAppInstalled(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

}
