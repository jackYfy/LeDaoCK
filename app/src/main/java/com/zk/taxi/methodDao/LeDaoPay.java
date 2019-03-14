package com.zk.taxi.methodDao;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.zk.taxi.Config;
import com.zk.taxi.Interface;
import com.zk.taxi.entity.Guid;
import com.zk.taxi.http.HttpResponse;
import com.zk.taxi.http.HttpUtils;
import com.zk.taxi.http.Response;
import com.zk.taxi.tool.ToastUtils;
import com.zk.taxi.widget.LoadingDialog;

import java.util.List;
import java.util.Map;

/**
 * 乐道app支付
 *
 * @author winky
 */
@SuppressLint("HandlerLeak")
public class LeDaoPay {
	public static final String TAG = LeDaoPay.class.getSimpleName();
	private static final String URL_WXPAY = "https://weixin.zkits.cn/pay";// 微信支付url
	private static final String URL_ALIPAY = Config.URL_ALIPAYS;// 支付宝url
	private static final int GET_TRADENO = 1001;// 获取支付流水号成功
	private static final int CLOSE_WEIXIN = 1002;// 关闭微信订单
	private static final int CREATE_WEIXIN = 1003;// 创建微信订单
	private static final int CREATE_ALIPAY = 1004;// 创建支付宝订单

	public static final int ALIPAY_RESULT = 1007;// 支付宝结果
	private Activity act = null;
	private String guid = null;
	private String total = null;
	private int paytype = 0;
	private Callback callback = null;
	private LoadingDialog dialog = null;

	/**
	 * 乐道支付 支付宝、微信
	 *
	 * @param act
	 *            上下文
	 * @param guid
	 *            订单唯一id
	 * @param total
	 *            金额
	 * @param paytype
	 *            支付类型 CREATE_WEIXIN、CREATE_ALIPAY
	 * @param callback
	 *            微信传null，支付宝需要传入回调
	 */
	public LeDaoPay(Activity act, String guid, String total, int paytype, Callback callback) {
		this.act = act;
		this.guid = guid;
		this.total = total;
		this.paytype = paytype;
		this.callback = callback;
//		dialog = new LoadingDialog(act);
//		dialog.loading();
		getTradNo(guid);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case GET_TRADENO:
					String tradeNo = msg.obj.toString();
					if (paytype == Config.WEIXIN_PAY) {
//						createWeiXin(total, tradeNo, guid);
					} else if (paytype == Config.ALI_PAY) {
						createAlipay(Float.parseFloat(total), tradeNo, guid);
					} else {
						ToastUtils.show("支付失败");
					}
					break;
				case CREATE_ALIPAY:
//					dialog.dismiss();
					final String alipayStr = msg.obj.toString();
					final Handler handler = new Handler(callback);
					if (TextUtils.isEmpty(alipayStr) || callback == null) {
						ToastUtils.show("支付参数异常，不能发起支付");
						return;
					}
					new Thread(new Runnable() {
						@Override
						public void run() {
							PayTask alipay = new PayTask(act);
							// 调用支付接口，获取支付结果
							Map<String, String> result = alipay.payV2(alipayStr, true);
							Message msg = handler.obtainMessage();
							msg.what = ALIPAY_RESULT;
							msg.obj = result;
							handler.sendMessage(msg);
						}
					}).start();
					break;
				case CLOSE_WEIXIN:

					break;
			}
		}
	};

	/**
	 * 获取乐道支付流水号
	 *
	 * @param guid
	 */
	public synchronized void getTradNo(String guid) {
		JSONObject json=new JSONObject();
		json.put("orderid", guid);
		HttpUtils.getTaxiPay(act,Config.URL_CALL_CAR, Interface.GETPAYSERIALNO, json.toString(), new HttpResponse() {
			@Override
			public void result(int i, String content) {
				try {
					List<Guid> list = Response.analysis(act,content, Guid.class);
					if (list != null && list.size() > 0) {
						Message msg = handler.obtainMessage();
						msg.what = GET_TRADENO;
						msg.obj = list.get(0).getPaySerialNO();
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
				}
			}
		});
	}

//	*
//	 * 创建支付宝支付订单
	public synchronized void createAlipay(final float  total, final String tradeNo, String guid) {
		JSONObject json=new JSONObject();
		json.put("orderguid", guid);//guid
		json.put("payamount", total);//金额
		json.put("payment", 1);//支付方式，支付方式，0.现金，1.支付宝，2.微信钱包，3.电子钱包，4.百度钱包
		json.put("payserialno", tradeNo);//流水号
		HttpUtils.getTaxiPay(act,Config.URL_CALL_CAR, Interface.UPDATEORDERBYPAY, json.toString(), new HttpResponse() {
			@Override
			public void result(int i, String content) {
				if (Response.CodeSuccess(act,content)) {
					String	alipayStr=Response.analysisResult(content);
					Log.d(TAG, "alipayStr:" + alipayStr);
						Message msg = handler.obtainMessage();
						msg.what = CREATE_ALIPAY;
						msg.obj = alipayStr;
						handler.sendMessage(msg);
				}else {
					ToastUtils.show("创建支付宝订单失败");
				}
			}
		});

	}

}
