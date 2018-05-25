### Android 微信支付宝支付封装
>使用流程

####1. 在WeixinShare里面填写自己的appid
####2. 从后台请求支付信息
####3. 微信需要复制wxapi包下的WXPayEntryActivity(带包复制)
####4. 掉起支付宝或微信支付(微信同理)



	a.创建支付对象
		mAliPayEntry = AliPayEntry.getInstance();
	
	b.    // 后台返回的支付信息 String info;
        mAliPayEntry.setModel("info");
        mAliPayEntry.registerListener(this);
        mAliPayEntry.setActivity(this);
        mAliPayEntry.pay();

	c.  //alipay支付回调处理

        if (errCode == AliPayEntry.CODE_9000) {
            Toast.makeText(this, "支付宝支付成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "支付宝支付失败", Toast.LENGTH_SHORT).show();
        }
