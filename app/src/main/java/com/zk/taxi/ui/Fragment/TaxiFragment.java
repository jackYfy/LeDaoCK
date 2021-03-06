package com.zk.taxi.ui.Fragment;


import android.os.Bundle;
import android.view.View;

import com.zk.taxi.BaseFragment;
import com.zk.taxi.R;

/**
 */
public class TaxiFragment extends BaseFragment {
    public static final String ARGS_PAGE = "args_page";
    private int mPage;

    public static TaxiFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARGS_PAGE, page);
        TaxiFragment fragment = new TaxiFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARGS_PAGE);
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_taxi;
    }

    @Override
    public void initView(View view) {

    }
}
