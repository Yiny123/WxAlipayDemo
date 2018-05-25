package pay.com.wxalipaydemo.pay;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liny 2017年3月8日
 */
public abstract class PayEntry {
    public interface OnPayListener {
        public void onPayResult(int type, int errCode, String result);
    }

    // 微信支付
    public static final int ENTRY_WEIXIN = 0;
    // 支付宝
    public static final int ENTRY_ALI = 1;

    protected List<WeakReference<OnPayListener>> mListeners;

    private boolean mPaying;

    public PayEntry() {
        mListeners = new ArrayList<WeakReference<OnPayListener>>();
    }

    public void registerListener(OnPayListener listener) {
        if (listener != null) {
            for (WeakReference<OnPayListener> wr : mListeners) {
                if (wr.get() == listener) {
                    return;
                }
            }
        }

        WeakReference<OnPayListener> weakReference = new WeakReference<OnPayListener>(listener);
        mListeners.add(weakReference);
    }

    public void unregisterListener(OnPayListener listener) {
        if (listener != null) {
            for (WeakReference<OnPayListener> wr : mListeners) {
                if (wr.get() == null || wr.get() == listener) {
                    mListeners.remove(wr);
                    break;
                }
            }
        }
    }

    protected boolean isPaying() {
        return mPaying;
    }

    protected void payStart() {
        mPaying = true;
    }

    protected void payEnd() {
        mPaying = false;
    }

    public abstract void setModel(Object obj);
    public abstract void pay();
    public abstract void notifyResult(int errCode,String result);

    public void pay(String tokenId, Context context) {

    }
}
