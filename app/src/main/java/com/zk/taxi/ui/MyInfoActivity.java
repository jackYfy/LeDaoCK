package com.zk.taxi.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;
import com.zk.taxi.entity.UserInfo;
import com.zk.taxi.methodDao.UserPost;
import com.zk.taxi.tool.CompressPicUtils;
import com.zk.taxi.tool.UserHelper;
import com.zk.taxi.view.CircleImageView;
import com.zk.taxi.widget.Methods;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import butterknife.Bind;

public class MyInfoActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.chang_phone) RelativeLayout chang_phone;
    @Bind(R.id.my_info_headimg) RelativeLayout my_info_headimg;
    @Bind(R.id.my_info_userimage) CircleImageView my_info_userimage;
    @Bind(R.id.set_realid) RelativeLayout set_realid;
    @Bind(R.id.info_nick) RelativeLayout info_nick;
    @Bind(R.id.isvi) LinearLayout isvi;
    @Bind(R.id.info_namenick) AppCompatTextView info_namenick;
    @Bind(R.id.info_sextx) AppCompatTextView info_sextx;
    @Bind(R.id.info_phone) AppCompatTextView info_phone;
    @Bind(R.id.info_cardid) AppCompatTextView info_cardid;
    @Bind(R.id.info_realname) AppCompatTextView info_realname;

    private static final int REQUEST_CODE_CHOOSE = 23;//定义请求码常量
    List<Uri> mSelected;
    CompressPicUtils compressPicUtils;
    private UserInfo user=null;
    @Override
    public int bindLayout() {
        return R.layout.activity_my_info;
    }

    @Override
    public void initView(View view) {
        setBackup();
        setTitle("个人信息");
        compressPicUtils=new CompressPicUtils(getActivity());
        chang_phone.setOnClickListener(this);
        my_info_headimg.setOnClickListener(this);
        set_realid.setOnClickListener(this);
        info_nick.setOnClickListener(this);
        if (UserHelper.getHeadImgURL()!=null){
            Glide.with(MyInfoActivity.this).load(UserHelper.getHeadImgURL()).into(my_info_userimage);
        }
        UserPost.getUserInfo(act,mHandler);//获取个人信息
        UserPost.getInfoCard(act,mHandler);//查询身份证是否验证
    }

    private void setinfo(UserInfo user) {
        info_namenick.setText(user.getNick_name());
        if (user.getSex()==0){
            info_sextx.setText("男");
        }else {
            info_sextx.setText("女");
        }
        info_realname.setText(user.getUser_truename());
        info_cardid.setText(user.getIc_card_no());
        info_phone.setText(user.getMobile());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chang_phone:
            Methods.toBase(this,ChangePhoneActivity.class);
            break;
            case R.id.my_info_headimg:
                Matisse.from(MyInfoActivity.this)
                        .choose(MimeType.ofAll())//照片视频全部显示
                        .countable(true)//有序选择图片
                        .maxSelectable(1)//最大选择数量为9
                        .gridExpectedSize(240)//图片显示表格的大小
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)//图像选择和预览活动所需的方向。
                        .thumbnailScale(0.85f)//缩放比例
                        .theme(R.style.Matisse_Zhihu)//主题  暗色主题 R.style.Matisse_Dracula
                        .imageEngine(new GlideEngine())//加载方式
                        .capture(true)//设置是否可以拍照
                        .captureStrategy(new CaptureStrategy(true, "com.zk.taxi.fileprovider"))//存储到哪里
                        .forResult(REQUEST_CODE_CHOOSE);//
                break;
            case R.id.set_realid:
                Methods.toBase(this,InfoRealNameActivity.class);
                break;
            case R.id.info_nick:
                Methods.toBase(this,ModifyInfoActivity.class);
                break;
        }
    }
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UserPost.HEADIMG:
                        String headImg= (String) msg.obj;
                    Uri uri = Uri.parse(headImg);
                    Glide.with(MyInfoActivity.this).load(uri).into(my_info_userimage);
                    UserHelper.saveHeadImgURL(headImg);
                    break;
                case UserPost.USERINFO:
                    List<UserInfo> list= (List<UserInfo>) msg.obj;
                    user=list.get(0);
                    setinfo(user);
                    break;
                case UserPost.INFOCRAD:
                    String id=(String) msg.obj;
                    if (id=="0"){
                        isvi.setVisibility(View.GONE);
                    }else if(id=="1"){
                        isvi.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    };
    String compicPath=null;
    @Override      //接收返回的地址
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            if(mSelected.get(0).toString().contains("my_images")){ //这个是照相机返回的图片路径 ，android 6.0后相机拍照储存需要在清单文件创建FileProvider
                String url=getFilePathFromURI(getActivity(),mSelected.get(0)); //my_images就是创建的FileProvider 文件@xml/filepaths的地址
                compicPath=compressPicUtils.getimage(url);
                Log.d("TAG1:",url+",compicPath:"+compicPath);
            }else{ //这个是相册返回的图片路径
                String url1=getRealPathFromUri(getActivity(),mSelected.get(0));
                compicPath=compressPicUtils.getimage(url1);//压缩处理后的图片路径
            }
            UserPost.SendHeadImg(getActivity(),compicPath,mHandler);
        }
    }
//content://media/external/images/media/**
//    转成：/storage/emulated/0/DCIM/Camera/**.jpg路径；  这是从相册选取
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    //"content://com.zk.taxi.fileprovider/my_images/JPEG_20180314_142923.jpg"
    //    转成：/storage/emulated/0/Android/com.zk.taxi/cache/**.jpg路径；  这是拍照后进行的转化
    public String getFilePathFromURI(Context context, Uri contentUri) {
        //copy file and send new file path
        String fileUrl=context.getExternalCacheDir().getAbsolutePath();
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(fileUrl+File.separator + fileName);
            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            IOUtils.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }
