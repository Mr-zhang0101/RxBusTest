package com.zhang.rxbustest.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zhang.rxbustest.R;
import com.zhang.rxbustest.event.Event;
import com.zhang.rxbustest.event.Event_Sticky;
import com.zhang.rxbustest.rxbus.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 被观察者
 */
public class ObservableFragment extends Fragment {

    private int mCountNum, mCountStickyNum;

    @BindView(R.id.tv_post)
    TextView mTvPost;
    @BindView(R.id.btn_post)
    Button mBtnPost;
    @BindView(R.id.tv_postSticky)
    TextView mTvPostSticky;
    @BindView(R.id.btn_postSticky)
    Button mBtnPostSticky;

    public static ObservableFragment newInstance() {
        return new ObservableFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_observable, container, false);
        initView(view);
        ButterKnife.bind(this, view);
        return view;
    }

    private void initView(View view) {

    }

    @OnClick({R.id.btn_post, R.id.btn_postSticky})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_post:
                RxBus.getDefault().post(new Event(++mCountNum));

                String str = mTvPost.getText().toString();
                mTvPost.setText(TextUtils.isEmpty(str) ? String.valueOf(mCountNum) : str + ", " + mCountNum);
                break;
            case R.id.btn_postSticky:
                RxBus.getDefault().postSticky(new Event_Sticky(String.valueOf(--mCountStickyNum)));

                String str2 = mTvPostSticky.getText().toString();
                mTvPostSticky.setText(TextUtils.isEmpty(str2) ? String.valueOf(mCountStickyNum) : str2 + ", " + mCountStickyNum);
                break;
        }
    }
}
