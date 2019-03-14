package com.zk.taxi;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zk.taxi.listenr.IActivity;
import com.zk.taxi.tool.ActivityManager;
import com.zk.taxi.tool.SystemBarTintManager;


import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

/**
 * Created by winky on 2016/11/4.
 */
public abstract class BaseActivity extends AppCompatActivity implements IActivity {

    /**
     * 当前Activity渲染的视图View
     **/
    private View activityView = null;
    /**
     * 当前Activity的弱引用，防止内存泄露
     **/
    public WeakReference<Activity> context = null;

    private SystemBarTintManager tintManager = null;
    private boolean isSetStatus = true;//标记是否设置默认状态
    private Toolbar toolbar = null;
    public Activity act;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act=this;
        // 设置渲染视图View
        activityView = LayoutInflater.from(this).inflate(bindLayout(), null);
        setContentView(activityView);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");//将toolbar标题设置为空，否则将显示两个标题
        setSupportActionBar(toolbar);
        // 将当前Activity压入栈
        context = new WeakReference<Activity>(this);
        ActivityManager.getAppManager().addActivity(this);
        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
        }
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        // 初始化控件
        initView(activityView);
        if (isSetStatus)
            tintManager.setStatusBarTintResource(R.color.gsjtsj_title_color);// 通知栏所需颜色
    }

    /**
     * 设置返回为关闭当前页面
     */
    public void setBackup() {
        setNavigation(R.mipmap.backpage_black, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    /**
     * 设置返回为关闭当前页面
     */
    public void setBackup(View.OnClickListener listener) {
        setNavigation(R.mipmap.backpage_black, listener);
    }

    /**
     * 设置navigation
     */
    public void setNavigation(int resId, View.OnClickListener listener) {
        toolbar.setNavigationIcon(resId);
        toolbar.setNavigationOnClickListener(listener);
    }

    /**
     * 设置标题名称，处理居中
     *
     * @param charSequence
     */
    public void setTitle(CharSequence charSequence) {
        ((TextView) findViewById(R.id.toolbar_title)).setText(charSequence);
    }

    /**
     * 设置标题名称，处理居中
     *
     * @param resId
     */
    public void setTitle(int resId) {
        ((TextView) findViewById(R.id.toolbar_title)).setText(resId);
    }

    /**
     * 设置状态栏颜色
     *
     * @param color
     */
    public void setStatusBarTintColor(int color) {
        if (color != 0) {
            toolbar.setBackgroundColor(ContextCompat.getColor(getActivity(), color));
            tintManager.setStatusBarTintResource(color);// 通知栏所需颜色
            isSetStatus = false;
        }
    }

    /**
     * 设置menu监听，重写onCreateOptionsMenu方法
     *
     * @param listener
     */
    public void setOnMenuClickListener(Toolbar.OnMenuItemClickListener listener) {
        toolbar.setOnMenuItemClickListener(listener);
    }

    public Activity getActivity() {
        return context.get();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void setTranslucentStatus() {
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 将当前Activity移除栈
        ButterKnife.unbind(activityView);
        ActivityManager.getAppManager().finishActivity(this);
    }

    public void startActivity(Class<?> clazz) {
        startActivity(new Intent(getActivity(), clazz));
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }



    @Override
    public void finish() {
        // 关闭窗体动画显示
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.finish();
    }

    public String getText(EditText editView) {
        return editView.getText().toString().trim();
    }

    public String getText(TextView textView) {
        return textView.getText().toString().trim();
    }
    /**
     * 隐藏输入法
     *
     * @param v 控件
     */
    public void hideInputMethod(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * 隐藏输入法 全屏
     */
    public void hideInputMethod() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
        }
    }

    /**
     * 显示输入法
     *
     * @param v 控件
     */
    public void showInputMethod(final View v) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(v, 0);
            }
        }, 300);
    }
}
