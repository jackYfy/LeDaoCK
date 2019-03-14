package com.zk.taxi.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zk.taxi.R;


public class LoadingDialog {
    private OneDialog dialog;
    private TextView textMsg;

    public LoadingDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        textMsg = (TextView) view.findViewById(R.id.loading_msg);
        dialog = new OneDialog(context, view);
    }

    public LoadingDialog setMessage(String msg) {
        textMsg.setText(msg);
        return this;
    }

    public LoadingDialog setCanceledOnTouchOutside(boolean value) {
        dialog.setCanceledOnTouchOutside(value);
        return this;
    }

    public LoadingDialog setCancelable(boolean value) {
        dialog.setCancelable(value);
        return this;
    }

    public LoadingDialog setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        dialog.setOnCancelListener(onCancelListener);
        return this;
    }

    public void show() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
       // params.format = PixelFormat.TRANSPARENT;
//        params.dimAmount = 0.5f;
//        dialog.getWindow().setAttributes(params);
//        dialog.getWindow().setBackgroundDrawable(null);
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }

    public void loading() {
        textMsg.setText("加载中...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
