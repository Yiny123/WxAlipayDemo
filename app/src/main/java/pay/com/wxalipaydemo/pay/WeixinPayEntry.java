package pay.com.wxalipaydemo.pay;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.lang.ref.WeakReference;
import java.util.List;

import pay.com.wxalipaydemo.BaseApplication;

/**
 * Created by liny 2017年3月8日
 */
public class WeixinPayEntry extends PayEntry {
    private static final String TAG = "WeixinPayEntry";
    private static WeixinPayEntry instance;

    // 微信支付返回码，见支付文档 0:成功   -1:失败   -2:取消
    public static final int RET_SUCC = 0;
    public static final int RET_FAIL = -1;
    public static final int RET_CANCEL = -2;

    private IWXAPI mApi;
    private WeixinPayModel mModel;

    public synchronized static WeixinPayEntry getInstance() {
        if (instance == null) {
            instance = new WeixinPayEntry();
        }

        return instance;
    }

    private WeixinPayEntry() {
        super();
        mApi = WXAPIFactory.createWXAPI(BaseApplication.getContext(), WeixinShare.WEIXIN_APP_ID, true);
        boolean result = mApi.registerApp(WeixinShare.WEIXIN_APP_ID);
        if (result) {
            Log.d(TAG, "register app success.");
        } else {
            Log.d(TAG, "register app fail.");
        }
    }

    @Override
    public void setModel(Object obj) {
        mModel = (WeixinPayModel) obj;
    }

    @Override
    public void pay() {
        if (isPaying())
            return;

        payStart();

        PayReq req = new PayReq();
        req.appId = mModel.appid;
        req.partnerId = mModel.partnerid;
        req.prepayId = mModel.prepayid;
        req.packageValue = mModel.packageValue;
        req.nonceStr = mModel.noncestr;
        req.timeStamp = mModel.timestamp;
        req.sign = mModel.sign;

        if (mApi.sendReq(req)) {
            Log.d(TAG, "send pay request success.");
        } else {
            Log.d(TAG, "send pay request fail.");
        }
    }

    /**
     * //宝付支付的微信支付
     *
     * @param tokenId
     * @param context
     */
    @Override
    public void pay(String tokenId, Context context) {


    }


    @Override
    public void notifyResult(int errCode, String resultInfo) {
        payEnd();


        for (WeakReference<OnPayListener> wr : mListeners) {
            if (wr.get() != null) {
                wr.get().onPayResult(ENTRY_WEIXIN, errCode, "");
            }
        }
    }

    public static class WeixinPayModel {
        public String appid;
        public String partnerid;
        public String prepayid;
        public String packageValue;
        public String noncestr;
        public String timestamp;
        public String sign;
    }


    /**
     * 检查微信是否存在
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                System.out.println(pinfo.get(i).packageName);
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }
}
