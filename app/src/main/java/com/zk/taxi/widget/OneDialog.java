package com.zk.taxi.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.zk.taxi.R;


/**
 * Created by winky on 2016/11/4.
 */
public class OneDialog extends Dialog {

    public OneDialog(Context context) {
        super(context);
    }

    public OneDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public OneDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public OneDialog(Context context, View view) {
        super(context, R.style.dialog_full);
        setContentView(view);
    }
}
