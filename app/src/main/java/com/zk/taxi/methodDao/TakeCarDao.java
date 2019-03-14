package com.zk.taxi.methodDao;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zk.taxi.Config;
import com.zk.taxi.Interface;
import com.zk.taxi.entity.ComplaintEntity;
import com.zk.taxi.entity.ConfirmOrder;
import com.zk.taxi.entity.CreateTaxiOrder;
import com.zk.taxi.entity.DriverGPS;
import com.zk.taxi.entity.Guid;
import com.zk.taxi.entity.OrderCarStatusEntity;
import com.zk.taxi.entity.OrderInfo;
import com.zk.taxi.entity.TaxiInfoEntity;
import com.zk.taxi.entity.TripHistoryEntity;
import com.zk.taxi.entity.UserCallDriver;
import com.zk.taxi.entity.UserInfo;
import com.zk.taxi.http.HttpResponse;
import com.zk.taxi.http.HttpUtils;
import com.zk.taxi.http.Response;
import com.zk.taxi.tool.DateUtils;
import com.zk.taxi.tool.GaodeToGpsUtil;
import com.zk.taxi.tool.ToastUtils;
import com.zk.taxi.tool.UserHelper;
import com.zk.taxi.ui.CallCar.CancelSucceedActivity;
import com.zk.taxi.widget.LoadingDialog;
import com.zk.taxi.widget.Methods;

import org.apache.http.Header;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/1/2.
 */

public class TakeCarDao {
    private static LoadingDialog loadingDialog;
    public static final int NEARTAXI=1001;
    public static final int NEARCAR=1002;
    public static final int CREATETAXI=1003;
    public static final int ORDERSTATUS=1004;
    public static final int DRIVERLOCATION=1005;
    public static final int ORDERCARSTATUS=1006;
    public static final int TRIPHISTORY=1007;
    public static final int ORDERSTATE=1008;
    public static final int COMPLAINT=1009;

    //附近出租车
    public static void getNearTaxiInfo(final Activity act, int taxiType, LatLng latlng, final Handler handler){
        UserInfo user = UserHelper.getUserInfo();
        UserCallDriver userCallDriver = new UserCallDriver();
        GaodeToGpsUtil wg = new GaodeToGpsUtil();
        HashMap<String, Double> hm = wg.GaodeToGps(latlng.latitude,latlng.longitude);
        latlng = new LatLng(hm.get("lat"),hm.get("lon"));
        userCallDriver.setLat(latlng.latitude);
        userCallDriver.setLon(latlng.longitude);
        userCallDriver.setRequestTime(DateUtils.getTime());
        userCallDriver.setCustomerName(user == null ? "" : user.getUser_name());
        userCallDriver.setCustomerTel(user == null ? "" : user.getMobile());
        userCallDriver.setTaxiType(taxiType);
        HttpUtils.getTaxi(Config.URL_CALL_CAR, Interface.NS_QUERYDETAILTAXISTATUS,userCallDriver.toString(),new HttpResponse(){
            @Override
            public void result(int i, String content) {
                Message msg=new Message();
                msg.obj= Response.analysis(act,content,TaxiInfoEntity.class);
                msg.what=NEARTAXI;
                handler.sendMessage(msg);
            }
        });
    }
//附近网约车
    public static void getNearCarInfo(final Activity act, int taxiType, LatLng latlng, final Handler handler){
        GaodeToGpsUtil wg = new GaodeToGpsUtil();
        HashMap<String, Double> hm = wg.GaodeToGps(latlng.latitude,latlng.longitude);
        latlng = new LatLng(hm.get("lat"),hm.get("lon"));
        JSONObject json=new JSONObject();
        json.put("lat", latlng.latitude);
        json.put("lon", latlng.longitude);
        HttpUtils.getTaxi(Config.URL_CALL_CAR, Interface.NS_TAXI_NEARBYSPECIALCAR,json.toString(),new HttpResponse(){
            @Override
            public void result(int i, String content) {
                Message msg=new Message();
                msg.obj= Response.analysis(act,content,TaxiInfoEntity.class);
                msg.what=NEARCAR;
                handler.sendMessage(msg);
            }
        });
    }
    //创建出租车订单
    public static void getCreateTaxi(final Activity act,LatLng stLat,LatLng edLat,String address,String destination,int type,final Handler handler){
        loadingDialog=new LoadingDialog(act);
        loadingDialog.loading();
        UserInfo user = UserHelper.getUserInfo();
        CreateTaxiOrder createOrder = new CreateTaxiOrder();
        GaodeToGpsUtil wg = new GaodeToGpsUtil();
        createOrder.setCreateTime(DateUtils.getTime());
        createOrder.setCustomerName(user.getLast_name() == null ? "" : user.getLast_name());
        createOrder.setCustomerTel(user.getMobile() == null ? "" : user.getMobile());
        createOrder.setSex(user.getSex());
        createOrder.setDestination(destination);
        //上车点经纬度
        HashMap<String, Double> sthm = wg.GaodeToGps(stLat.latitude,stLat.longitude);
        LatLng startlatlng = new LatLng(sthm.get("lat"),sthm.get("lon"));
        //下车点经纬度
        HashMap<String, Double> edhm = wg.GaodeToGps(edLat.latitude,edLat.longitude);
        LatLng endlatlng = new LatLng(edhm.get("lat"),edhm.get("lon"));

        createOrder.setLat(startlatlng.latitude);
        createOrder.setLon(startlatlng.longitude);
        createOrder.setDLat(endlatlng.latitude);
        createOrder.setDLon(endlatlng.longitude);
        createOrder.setDirection("");
        createOrder.setFlowType(1);
        createOrder.setRequestTime(DateUtils.getTime());
        createOrder.setTaxiAddress(address);
        createOrder.setStatus(0);
        createOrder.setTaxiType(type);
        HttpUtils.getTaxi(Config.URL_CALL_CAR,Interface.NS_EXTERNALTAXIRECORD,createOrder.toString(),new HttpResponse(){
            @Override
            public void result(int i, String content) {
                Message msg=new Message();
                msg.obj= Response.analysis(act,content,Guid.class);
                msg.what=CREATETAXI;
                handler.sendMessage(msg);
                loadingDialog.dismiss();
            }
        });
    }
    //取消出租车订单
    public static void getCancelOrder(final Activity act, int taxiType, String address, final String guid){
        UserInfo user = UserHelper.getUserInfo();
        ConfirmOrder confirmOrder=new ConfirmOrder();
        confirmOrder.setGuid(guid);
        confirmOrder.setRequestTime(DateUtils.getTime());
        confirmOrder.setTaxiAddress(address);
        confirmOrder.setTaxiType(1);
        confirmOrder.setCustomerName(user.getUser_name());
        confirmOrder.setCustomerTel(user.getMobile());
        HttpUtils.getTaxi(Config.URL_CALL_CAR,Interface.NS_CANCELTAXIORDER,confirmOrder.toString(),new HttpResponse(){
            @Override
            public void result(int i, String content) {
                if (Response.CodeSuccess(act,content)) {
                    ToastUtils.show("订单取消成功");
                    Intent intent = new Intent(act, CancelSucceedActivity.class);
                    intent.putExtra("GUID", guid);
                    act.startActivity(intent);
                    act.finish();
                }else {
                    ToastUtils.show("取消失败");
                }
            }
        });
    }
    //取消订单成功后选择取消理由提交
    public static void getCancelOrderReason(final Activity act, String guid,String content){
        JSONObject json=new JSONObject();
        json.put("orderguid", guid);
        json.put("canceltype",content);
        HttpUtils.getTaxi(Config.URL_CALL_CAR, Interface.NS_CANCELTAXIORDERTYPE, json.toJSONString(), new HttpResponse() {
            @Override
            public void result(int i, String content) {
                if (Response.CodeSuccess(act,content)) {
                    ToastUtils.show("提交成功");
                    Methods.toMain(act);
                    act.finish();
                }else {
                    ToastUtils.show("提交失败");
                }
            }
        });
    }

    //查询订单是否被接单状态信息
    public static void getOrderStatus(final Activity act, String guid,final Handler handler){
        JSONObject json=new JSONObject();
        json.put("Guid", guid);
        HttpUtils.getTaxi(Config.URL_CALL_CAR,Interface.NS_QUERYCALLORDERSTATUS,json.toJSONString(),new HttpResponse(){
            @Override
            public void result(int i, String content) {
                Message msg=new Message();
                msg.obj= Response.analysis(act,content,OrderInfo.class);
                msg.what=ORDERSTATUS;
                handler.sendMessage(msg);
            }
        });

    }
    //查询订单状态信息（乘客是否已上车，是否到达目的地）
    public static void getCarStatus(final Activity act, String guid,final Handler handler){
        JSONObject json=new JSONObject();
        json.put("Guid", guid);
        HttpUtils.getTaxi(Config.URL_CALL_CAR,Interface.NS_QUERYORDERSTATUS,json.toJSONString(),new HttpResponse(){
            @Override
            public void result(int i, String content) {
                Message msg=new Message();
                msg.obj= Response.analysis(act,content,OrderCarStatusEntity.class);
                msg.what=ORDERCARSTATUS;
                handler.sendMessage(msg);
            }
        });

    }
    //查询司机实时位置
    public static void getDriverLocation(final Activity act, String taxiguid,final Handler handler){
        JSONObject json=new JSONObject();
        json.put("taxicarid", taxiguid);
        HttpUtils.getTaxi(Config.URL_CALL_CAR, Interface.NS_TAXI_REALTIMEGPSINFO, json.toJSONString(), new HttpResponse() {
            @Override
            public void result(int i, String content) {
                Message msg=new Message();
                msg.obj = Response.analysis(act, content, DriverGPS.class);
                msg.what=DRIVERLOCATION;
                handler.sendMessage(msg);
            }
        });
    }

    //评价订单
  public static void  getEvaluation(final Activity act,String guid,int star,String content,String textid,String textcontent){
      JSONObject json=new JSONObject();
      json.put("orderid",guid);
      json.put("satisfaction",String.valueOf(star));
      json.put("evaluation",content);
      json.put("tabid",textid);
      json.put("tabname",textcontent);
      HttpUtils.getTaxi(Config.URL_CALL_CAR, Interface.SETSATISFACTION, json.toJSONString(), new HttpResponse() {
          @Override
          public void result(int i, String content) {
              if (Response.CodeSuccess(act,content)) {
                  ToastUtils.show("提交成功,谢谢你的评价");
                  Methods.toMain(act);
                  act.finish();
              }else {
                  ToastUtils.show("提交失败");
              }
          }
      });
  }

  //获取行程列表
    public static void getTripHistory(final Activity act,int PageIndex,final Handler handler){
        loadingDialog=new LoadingDialog(act);
        loadingDialog.loading();
        JSONObject json=new JSONObject();
        json.put("CustomerTel",UserHelper.getUserInfo().getMobile());
        json.put("pageIndex",PageIndex);
        json.put("pagesize",Config.PAGE_SIZE);
        HttpUtils.getTaxi(Config.URL_CALL_CAR, Interface.NS_QUERYCALLORDERHISTORYEXFORLEDAO, json.toJSONString(), new HttpResponse() {
            @Override
            public void result(int i, String content) {
                Message msg=new Message();
                msg.obj= Response.analysis(act,content,TripHistoryEntity.class);
                msg.what=TRIPHISTORY;
                handler.sendMessage(msg);
                loadingDialog.dismiss();
            }
        });
    }

    //订单投诉
    public static void getStrokeComplaint(final Activity act,String complainttype,String complaint,String taxicard,String taskid){
        UserInfo user = UserHelper.getUserInfo();
        JSONObject json=new JSONObject();
        json.put("userid",user.getUser_id());//user_id(必填)
        json.put("complainant",user.getUser_name());//投诉者（必填）
        json.put("complainantphone",user.getMobile());//投诉者电话（必填）
        json.put("complainttype",complainttype);//投诉类型
        json.put("complaint",complaint);//投诉内容
        json.put("taxicard",taxicard);//车牌
        json.put("taskid",taskid);//订单
       HttpUtils.getTaxi(Config.URL_CALL_CAR, Interface.NS_CT_COMPLAINTREGISTERADD, json.toJSONString(), new HttpResponse() {
           @Override
           public void result(int i, String content) {
               Log.d("POST", "content:"+content);
               if (Response.CodeSuccess(act,content)) {
                   ToastUtils.show("提交成功");
                   act.finish();
               }else {
                   ToastUtils.show("提交失败");
               }
           }
       });

    }
//获取投诉信息
    public static void getComplaintReturn(final Activity act,int pageindex,final Handler handler){
        loadingDialog=new LoadingDialog(act);
        loadingDialog.loading();
        UserInfo user = UserHelper.getUserInfo();
        JSONObject json=new JSONObject();
        json.put("userid",user.getUser_id());
        json.put("status",-1);// -1代表任何状态0、新建登记1、转交企业2、企业回复3、回访客户4、完成
        json.put("pageindex",pageindex);
        json.put("pagesize",Config.PAGE_SIZE);
        HttpUtils.getTaxi(Config.URL_CALL_CAR, Interface.NS_CT_GETCOMPLAINTREGISTERLIST1, json.toJSONString(), new HttpResponse() {
            @Override
            public void result(int i, String content) {
                Message msg=new Message();
                msg.obj= Response.analysis(act,content,ComplaintEntity.class);
                msg.what=COMPLAINT;
                handler.sendMessage(msg);
                loadingDialog.dismiss();
            }
        });
    }
    //提交失物信息
    public static void getLostThing(final Activity act,String date,String lostinput,String taxicard,String taskid){
        UserInfo user = UserHelper.getUserInfo();
        JSONObject json=new JSONObject();
        json.put("userid",user.getUser_id());//user_id(必填)
        json.put("owner",user.getUser_name());//失主
        json.put("ownerphone",user.getMobile());//失主电话
        json.put("taxidate",date);//遗失日期时间
        json.put("lostgoods",lostinput);//失物描述
        json.put("taxicard",taxicard);//车牌
        json.put("taskid",taskid);//任务id
        HttpUtils.getTaxi(Config.URL_CALL_CAR, Interface.NS_CT_LOSTREGISTERADD, json.toJSONString(), new HttpResponse() {
            @Override
            public void result(int i, String content) {
                if (Response.CodeSuccess(act,content)) {
                    ToastUtils.show("提交成功");
                    act.finish();
                }else {
                    ToastUtils.show("提交失败");
                }
            }
        });
    }

   /* //获取订单步骤状态去往相应的界面
    public static void getOrderStates(final Activity act,final Handler handler){
        JSONObject json=new JSONObject();
        json.put("tel",UserHelper.getUserInfo().getMobile());
       HttpUtils.getTaxiOrder(act, Config.URL_CALL_CAR, Interface.QUERYLATESTCALLORDER, json.toJSONString(), new AsyncHttpResponseHandler() {
           @Override
           public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String content=new String(bytes);
               Message msg=new Message();
               msg.obj= Response.analysis(act,content,TripHistoryEntity.class);
               msg.what=ORDERSTATE;
               handler.sendMessage(msg);
           }

           @Override
           public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

           }
       });
    }*/
  //查询订单的状态，去往相应界面继续完成
   public static void getOrderStates(final Activity act,final Handler handler){
       JSONObject json=new JSONObject();
       json.put("tel",UserHelper.getUserInfo().getMobile());
       HttpUtils.getTaxiPay(act, Config.URL_CALL_CAR, Interface.QUERYLATESTCALLORDER, json.toJSONString(), new HttpResponse() {
           @Override
           public void result(int i, String content) {
               Message msg=new Message();
               msg.obj= Response.analysis(act,content,TripHistoryEntity.class);
               msg.what=ORDERSTATE;
               handler.sendMessage(msg);
           }
       });
   }
   }
