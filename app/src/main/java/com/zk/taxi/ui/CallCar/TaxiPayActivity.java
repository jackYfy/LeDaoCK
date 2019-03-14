package com.zk.taxi.ui.CallCar;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zk.taxi.BaseActivity;
import com.zk.taxi.Config;
import com.zk.taxi.R;
import com.zk.taxi.entity.OrderInfo;
import com.zk.taxi.entity.TripHistoryEntity;
import com.zk.taxi.methodDao.LeDaoPay;
import com.zk.taxi.methodDao.PayResult;
import com.zk.taxi.tool.ToastUtils;
import com.zk.taxi.view.CircleImageView;
import com.zk.taxi.widget.ColorDialog;

import java.math.BigDecimal;
import java.util.Map;

import butterknife.Bind;

import static com.zk.taxi.methodDao.LeDaoPay.ALIPAY_RESULT;

public class TaxiPayActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.weixinpay)  LinearLayout weixinpay;
    @Bind(R.id.weixin_img) AppCompatImageView weixin_img;
    @Bind(R.id.weixin_tx) AppCompatTextView weixin_tx;

    @Bind(R.id.alipay)  LinearLayout alipay;
    @Bind(R.id.ali_img) AppCompatImageView ali_img;
    @Bind(R.id.ali_tx) AppCompatTextView ali_tx;

    @Bind(R.id.bankpay)  LinearLayout bankpay;
    @Bind(R.id.bank_img) AppCompatImageView bank_img;
    @Bind(R.id.bank_tx) AppCompatTextView bank_tx;

    @Bind(R.id.cashpay)  LinearLayout cashpay;
    @Bind(R.id.cash_img) AppCompatImageView cash_img;
    @Bind(R.id.cash_tx) AppCompatTextView cash_tx;

    @Bind(R.id.taxi_pay_userimage) CircleImageView  userimage;//司机头像
    @Bind(R.id.taxi_pay_name) AppCompatTextView taxi_pay_name;//司机姓名
    @Bind(R.id.taxi_pay_taxiId) AppCompatTextView taxi_pay_taxiId;//车牌号
    @Bind(R.id.taxi_pay_phone) LinearLayout taxi_pay_phone;//拨打电话
    @Bind(R.id.taxi_pay_total) EditText taxi_pay_total;//输入金额
    @Bind(R.id.tolls) EditText tolls;//过路费
    @Bind(R.id.parking) EditText parking;//停车费
    @Bind(R.id.submit_money) AppCompatTextView submit_money;//总金额


    private OrderInfo orderInfo = null;// 订单
    private TripHistoryEntity stateEntity = null;// 订单状态对应的数据
    private String guid = null;// 订单唯一标识
    private String startAddress = null;// 开始地址
    private String endAddress = null;// 结束地址
    /*
    乘客自己结束行程付款
    * */
    @Override
    public int bindLayout() {
        return R.layout.activity_taxi_pay;
    }

    @Override
    public void initView(View view) {
               setTitle("待支付");
        setOnMenuClickListener(menuClickListener);
        weixinpay.setOnClickListener(this);
        alipay.setOnClickListener(this);
        bankpay.setOnClickListener(this);
        cashpay.setOnClickListener(this);
        taxi_pay_phone.setOnClickListener(this);
        initSt();
    }

    private void initSt() {
        Intent intent = getIntent();
        if (!intent.hasExtra(OrderInfo.TAG)&&!intent.hasExtra("state")) {
            ToastUtils.show("初始化失败");
            finish();
            return;
        }
        if(intent.hasExtra("state")){
            stateEntity=(TripHistoryEntity) intent.getSerializableExtra("state");//订单信息
            taxi_pay_name.setText(stateEntity.getDriverName()+ "师傅");
            taxi_pay_taxiId.setText(stateEntity.getTaxiCard());
            startAddress = stateEntity.getTaxiAddress();//起点地址
            endAddress=stateEntity.getTaxiDestination();//终点地址
            guid = stateEntity.getGuid();//订单GUID
        }else {
            orderInfo = (OrderInfo) intent.getSerializableExtra(OrderInfo.TAG);//订单信息
            guid = orderInfo.getGuid();//订单GUID
            startAddress = intent.getStringExtra(Config.ST_ADDRESS);//起点地址
            endAddress = intent.getStringExtra(Config.ED_ADDRESS);//终点地址
            taxi_pay_name.setText(orderInfo.getDriverName() + "师傅");
            taxi_pay_taxiId.setText(orderInfo.getTaxiCard());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.taxi_pay_phone:
                String phonenum=null;
                if(stateEntity!=null){
                    phonenum=stateEntity.getTaxiSim();
                }else{
                    phonenum=orderInfo.getTaxiSim();
                }
                ColorDialog dialog = new ColorDialog(this);
                dialog.setColor("#ffffff");//#8ECB54
                dialog.setAnimationEnable(true);
                dialog.setTitle("");
                dialog.setContentText(phonenum);
                final String finalPhonenum = phonenum;
                dialog.setPositiveListener("取消", new ColorDialog.OnPositiveListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        ToastUtils.show("取消");
                        dialog.dismiss();
                    }
                }).setNegativeListener("呼叫", new ColorDialog.OnNegativeListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_CALL); // 设置动作
                        Uri data = Uri.parse("tel:" + finalPhonenum); // 设置数据
                        intent.setData(data);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                }).show();
                break;
            case R.id.weixinpay:
                Intent intent = new Intent(act, UserEvaluateActivity.class);
                intent.putExtra(OrderInfo.TAG, orderInfo);
                act.startActivity(intent);
                act.finish();
                break;
            case R.id.alipay:
                String total = null;
                String pay1=getText(taxi_pay_total);
                String pay2=getText(tolls);
                String pay3=getText(parking);
                double total1 ;
                double total2 ;
               if(!TextUtils.isEmpty(pay2)&&!TextUtils.isEmpty(pay3)){
                    total1=add(pay1,pay2);
                    total2=add(Double.toString(total1),pay3);
                    total=Double.toString(total2);
                }else if(!TextUtils.isEmpty(pay2)&&TextUtils.isEmpty(pay3)){
                    total1=add(pay1,pay2);
                    total=Double.toString(total1);
                }else if(TextUtils.isEmpty(pay2)&&!TextUtils.isEmpty(pay3)){
                    total2=add(pay1,pay3);
                    total=Double.toString(total2);
                }else if(TextUtils.isEmpty(pay2)&&TextUtils.isEmpty(pay3)){
                    total=pay1;
                }
                if(total!=null&&total.length() != 0){
                        submit_money.setText("总金额 "+total+" 元");
                    }
                if (checkTotal(total) && !TextUtils.isEmpty(guid)) {
                    new LeDaoPay(act,guid, total, Config.ALI_PAY, callback);
                }
                break;
            case R.id.bankpay:

                break;
            case R.id.cashpay:

                break;
        }

    }


    Handler.Callback callback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Intent intent = null;
            if (msg.what == ALIPAY_RESULT) {
                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                switch (Integer.parseInt(resultStatus)) {
                    case 9000:// 订单支付成功
                        intent = new Intent(act, UserEvaluateActivity.class);
                        if(stateEntity!=null){
                            intent.putExtra(TripHistoryEntity.TAG,stateEntity);
                        }else{
                            intent.putExtra(OrderInfo.TAG, orderInfo);
                        }
                        act.startActivity(intent);
                        finish();
                        break;
                    case 8000:// 正在处理中

                        break;
                    case 4000:// 订单支付失败

                        break;
                    case 6001:// 用户中途取消

                        break;
                    case 6002:// 网络连接出错

                        break;
                }
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.complaint, menu);
        return true;
    }
    Toolbar.OnMenuItemClickListener menuClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_complaint:
                    Intent intent = new Intent(TaxiPayActivity.this, StrokeComplaintActivity.class);
                    if(stateEntity!=null){
                        intent.putExtra(TripHistoryEntity.TAG,stateEntity);
                    }else{
                        intent.putExtra(OrderInfo.TAG, orderInfo);
                    }
                    intent.putExtra(Config.ST_ADDRESS, startAddress);
                    intent.putExtra(Config.ED_ADDRESS, endAddress);
                    startActivity(intent);
                    break;
            }
            return false;
        }
    };

    //金额相加转换方法
    public double add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).doubleValue();
    }
    //金额检查
    private boolean checkTotal(String total) {
        try {
            if (TextUtils.isEmpty(total)) {
                ToastUtils.show("请输入计价器金额");
                return false;
            }
            if (total.indexOf(".") > 0
                        && (total.subSequence(total.lastIndexOf(".") + 1, total.length()).length() > 2)) {
                ToastUtils.show("金额最小单位为分");
                return false;
            }
            // double total_double = Double.parseDouble(total);

            return true;
        } catch (Exception e) {
            ToastUtils.show("金额转换出错");
            return false;
        }
    }
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            return true;
//        }
//        return false;
//    }
}
