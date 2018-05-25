package pay.com.wxalipaydemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import pay.com.wxalipaydemo.pay.AliPayEntry;
import pay.com.wxalipaydemo.pay.PayEntry;
import pay.com.wxalipaydemo.pay.WeixinPayEntry;

/**
 *
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, PayEntry.OnPayListener {

    private AliPayEntry mAliPayEntry;
    private WeixinPayEntry mWeixinPayEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tvWxPay = (TextView) findViewById(R.id.tv_pay_wx);
        TextView tvAliPay = (TextView) findViewById(R.id.tv_pay_ali);

        tvWxPay.setOnClickListener(this);
        tvAliPay.setOnClickListener(this);

        mAliPayEntry = AliPayEntry.getInstance();
        mWeixinPayEntry = WeixinPayEntry.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pay_ali:
                aliPay();
                break;
            case R.id.tv_pay_wx:
                wxPay();
                break;
        }
    }

    /**
     * 调用微信支付
     */
    private void wxPay() {
        //后台返回的支付信息 组装成WeixinPayModel对象
        mWeixinPayEntry.setModel("MODEL");
        mWeixinPayEntry.registerListener(this);
        mWeixinPayEntry.pay();
    }

    /**
     * 调用支付宝支付
     */
    private void aliPay() {
        // 后台返回的支付信息 String info;
        mAliPayEntry.setModel("info");
        mAliPayEntry.registerListener(this);
        mAliPayEntry.setActivity(this);
        mAliPayEntry.pay();
    }

    @Override
    public void onPayResult(int type, int errCode, String result) {
        if (type == PayEntry.ENTRY_WEIXIN) {
            //wxpay支付回调  根据错误码做相应处理
            if (errCode == WeixinPayEntry.RET_SUCC) {
                Toast.makeText(this, "微信支付成功", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "微信支付失败", Toast.LENGTH_SHORT).show();
            }

        } else if (type == PayEntry.ENTRY_ALI) {
            //alipay支付回调
            if (errCode == AliPayEntry.CODE_9000) {
                Toast.makeText(this, "支付宝支付成功", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "支付宝支付失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
