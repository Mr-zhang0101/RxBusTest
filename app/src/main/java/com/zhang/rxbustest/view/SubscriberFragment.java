package com.zhang.rxbustest.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zhang.rxbustest.R;
import com.zhang.rxbustest.event.Event;
import com.zhang.rxbustest.event.Event_Sticky;
import com.zhang.rxbustest.rxbus.RxDisposables;
import com.zhang.rxbustest.utils.ToastUtil;
import com.zhang.rxbustest.rxbus.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


/**
 * 观察者(订阅者)
 */
public class SubscriberFragment extends Fragment {
    private Disposable mRxSub, mRxSubSticky;

    @BindView(R.id.tv_result)
    TextView mTvResult;
    @BindView(R.id.checkbox)
    CheckBox mCheckBox;
    @BindView(R.id.tv_resultSticky)
    TextView mTvResultSticky;
    @BindView(R.id.btn_subscribeSticky)
    Button mBtnSubscribeSticky;

    public static SubscriberFragment newInstance() {
        return new SubscriberFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscriber, container, false);
        initView(view);
        ButterKnife.bind(this, view);
        return view;
    }
    private void initView(View view) {
        // 订阅普通RxBus事件
        subscribeEvent();
        ToastUtil.showShort(getActivity(), R.string.rxbus);
    }

    @OnClick(R.id.btn_subscribeSticky)
    public void onClick() {
        // 订阅Sticky事件
        subscribeEventSticky();
    }



    private void subscribeEvent() {
        RxDisposables.remove(mRxSub);
        mRxSub = RxBus.getDefault().toObservable(Event.class)
                .map(new Function<Event, Event>() {
                    @Override
                    public Event apply(Event event) throws Exception {
                        // 这里模拟产生 Error
                        if (mCheckBox.isChecked()) {
                            throw new RuntimeException("异常");
                        }
                        return event;
                    }
                })
                .subscribe(new Consumer<Event>() {
                    @Override
                    public void accept(Event event) throws Exception {
                        String str = mTvResult.getText().toString();
                        mTvResult.setText(TextUtils.isEmpty(str) ? String.valueOf(event.event) : str + ", " + event.event);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        subscribeEvent();
                    }
                });
        RxDisposables.add(mRxSub);
        ToastUtil.showShort(getActivity(), R.string.resubscribe);
    }


    private void subscribeEventSticky() {
        if (mRxSubSticky != null && !mRxSubSticky.isDisposed()) {
            mTvResultSticky.setText("");
            RxDisposables.remove(mRxSubSticky);

            mBtnSubscribeSticky.setText(R.string.subscribeSticky);
            ToastUtil.showShort(getActivity(), R.string.unsubscribeSticky);
        } else {
            Event_Sticky s = RxBus.getDefault().getStickyEvent(Event_Sticky.class);

            mRxSubSticky = RxBus.getDefault().toObservableSticky(Event_Sticky.class)
                    .flatMap(new Function<Event_Sticky, ObservableSource<Event_Sticky>>() {
                        @Override
                        public ObservableSource<Event_Sticky> apply(Event_Sticky eventSticky) throws Exception {
                            return Observable.just(eventSticky)
                                    .map(new Function<Event_Sticky, Event_Sticky>() {
                                        @Override
                                        public Event_Sticky apply(Event_Sticky eventSticky) throws Exception {
                                            // 这里模拟产生 Error
                                            if (mCheckBox.isChecked()) {
                                                throw new RuntimeException("异常");
                                            }
                                            return eventSticky;
                                        }
                                    })
                                    .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends Event_Sticky>>() {
                                        @Override
                                        public ObservableSource<? extends Event_Sticky> apply(Throwable throwable) throws Exception {
                                            return Observable.<Event_Sticky>never();
                                        }
                                    });
                        }
                    })
                    .subscribe(new Consumer<Event_Sticky>() {
                        @Override
                        public void accept(Event_Sticky eventSticky) throws Exception {
                            String str = mTvResultSticky.getText().toString();
                            mTvResultSticky.setText(TextUtils.isEmpty(str) ? String.valueOf(eventSticky.event) : str + ", " + eventSticky.event);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Logger.d(throwable.getMessage());
                        }
                    });
            RxDisposables.add(mRxSubSticky);

            mBtnSubscribeSticky.setText(R.string.unsubscribeSticky);
            ToastUtil.showShort(getActivity(), R.string.subscribeSticky);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxDisposables.remove(mRxSub);
        RxDisposables.remove(mRxSubSticky);
    }

}
