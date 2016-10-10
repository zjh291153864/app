package com.zhonghong.xqshijie.alipay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * 支付宝支付工具类
 * Created by xiezl on 16/7/15.
 */
public class payUtil {

    // 商户PID
    private static final String PARTNER = "2088021297585566";
    // 商户收款账号
    private static final String SELLER = "admin@xqshijie.com";
    // 商户私钥，pkcs8格式
    private static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMf8Nv7zG5HkDT0CAXmcpZ9OjSzqEapeccnH7eFnQq8ElMCM7Par3K5wIUKQF6vorOXf2Vjbsa/Mo21ei1eTkLFWdtc5ZoQpsiL7PEdjz5DZ/1SzfBxVktxCQ9mxNphDVY6yT4iYj0wt26FLi8R6Vj8jdcL3yPB8C0UjIoAYDk13AgMBAAECgYEApxS/Ve4CoLbpoIjmdpY7gYd9R4Mf0zko1C6nPeSpv9OR+/VBnS0lKNAh2ZzNSiERItUMn8KdB4VvHNcaj8aH4UZmH4x99F1Ld/XlFOGjHEgowsUx6cQpFmuiLyI/WVZ9ENTxcFaDdU9WztSyg2L7RXl6eIcYZS8Qg6Df53JFiuECQQD0geaxmrqQyCwLHrhtnb2RzlU9QXy0fFxc8vhulzInLhGYcz7ZSaRQfR5SqOOp36SAqwPNOolvz5idFEi5nfLJAkEA0WKWsX59I24H80MIhS30VOAu1XI4N5Y9XrdOZvB5JjH7y+j2QMiA/6PF5iZrYOdfuyH2c2xys5jhbxnpq9wePwJBAMe71df/rafX3SK4VbA1y0XwZ3FCrbxCWrETSh4NJMsctwLyIcUegzu8+ahMjq2WI6t4CAL+bocH7oNiroXyoQECQBEGlDxQjbpgyxKzw7CYoQJ5zDUc+YdDly/pdd2W9jLt44ycH9H6u8qNtZzWSOrzRWyBAXo3OkhySPpwv/hyiJ8CQQDJunUrij3ygt9iEvb92oGzzYHKnwAm7vjDQYoWYWFzpu5D7gWzLp1VvlWVwfE5t8qj18B0Lsc0ji9ryMfYYD00";
    // 支付宝公钥
    private static final String RSA_PUBLIC = "";

    public static final int SDK_PAY_FLAG = 1;
    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(final Activity mContext, final Handler mHandler, String goods, String goodsDes, String money,String orderId) {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(mContext).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            dialoginterface.dismiss();
                        }
                    }).show();
            return;
        }
//        String orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01","");
        String orderInfo = getOrderInfo(goods, goodsDes, money,orderId);

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();//支付啊

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mContext);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * create the order info. 创建订单信息
     */

    /**
     * @param subject "测试的商品"
     * @param body    "该测试商品的详细描述"
     * @param price   "0.01"
     * @param orderId    订单ID
     * @return
     */
    private String getOrderInfo(String subject, String body, String price,String orderId) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
//        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";
        orderInfo += "&out_trade_no=" + "\"" + orderId + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
//        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";
//        orderInfo += "&notify_url=" + "\"" + "https://testapp.xqshijie.com/xqsj/app/order/payCallback" + "\"";
        orderInfo += "&notify_url=" + "\"" + "https://103.10.86.28/xqsj/app/order/orderAlipay" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

      //调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
//         orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return sign(content, RSA_PRIVATE);
    }

    private final String ALGORITHM = "RSA";

    private final String SIGN_ALGORITHMS = "SHA1WithRSA";

    private final String DEFAULT_CHARSET = "UTF-8";

    private String sign(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                    Base64.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update(content.getBytes(DEFAULT_CHARSET));

            byte[] signed = signature.sign();

            return Base64.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
