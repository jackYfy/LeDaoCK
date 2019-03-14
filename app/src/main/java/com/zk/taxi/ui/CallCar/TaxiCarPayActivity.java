package com.zk.taxi.ui.CallCar;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;
import com.zk.taxi.widget.Methods;

import butterknife.Bind;

public class TaxiCarPayActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.cost_detail)  RelativeLayout cost_detail;
    @Bind(R.id.detail) AppCompatTextView detail;
    @Bind(R.id.go_rightimg) AppCompatImageView go_rightimg;
    @Bind(R.id.left_line)  View left_line;
    @Bind(R.id.right_line) View Right_line;
    @Bind(R.id.moeny_go) LinearLayout moeny_go;

    private Boolean isshow= false;
/*
* 司机结账，乘客端付款
* */
    @Override
    public int bindLayout() {
        return R.layout.activity_taxi_car_pay;
    }

    @Override
    public void initView(View view) {
        setTitle("待支付");
        setBackup();

        setOnMenuClickListener(menuClickListener);
        cost_detail.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cost_detail:
                if(isshow){
                    detail.setTextColor(getResources().getColor(R.color.green1));
                    go_rightimg.setVisibility(View.VISIBLE);
                    left_line.setVisibility(View.GONE);
                    Right_line.setVisibility(View.GONE);
                    moeny_go.setVisibility(View.GONE);
                    isshow=false;
                }else {
                    detail.setTextColor(getResources().getColor(R.color.colorPrimary));
                    go_rightimg.setVisibility(View.GONE);
                    left_line.setVisibility(View.VISIBLE);
                    Right_line.setVisibility(View.VISIBLE);
                    moeny_go.setVisibility(View.VISIBLE);
                    isshow=true;
                }

                break;
        }
    }

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
                    Methods.toBase(getActivity(),StrokeComplaintActivity.class);
                    break;
            }
            return false;
        }
    };
}
