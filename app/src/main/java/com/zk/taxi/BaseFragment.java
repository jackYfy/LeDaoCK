package com.zk.taxi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zk.taxi.listenr.IActivity;

import butterknife.ButterKnife;

/**
 *
 */
public abstract class BaseFragment extends Fragment implements IActivity {

    private View mView = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView==null) {
            mView= inflater.inflate(bindLayout(), container, false);
        }else{
            ViewGroup parent = (ViewGroup) mView.getParent();
            if(parent!=null){
                parent.removeView(mView);
            }
        }
        ButterKnife.bind(this, mView);
        initView(mView);
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(mView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
