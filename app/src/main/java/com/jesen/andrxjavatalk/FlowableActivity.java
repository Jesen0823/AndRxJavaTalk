package com.jesen.andrxjavatalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.jesen.andrxjavatalk.utils.GLog;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FlowableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowable);
    }

    private Subscription subscription;

    public void flowableClick(View view) {

        Flowable.create(new FlowableOnSubscribe<Integer>() {
                            @Override
                            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                                    e.onNext(i);
                                }
                                e.onComplete();
                            }
                        },
                //BackpressureStrategy.BUFFER
                BackpressureStrategy.ERROR
                //BackpressureStrategy.DROP
                //BackpressureStrategy.LATEST
                //BackpressureStrategy.MISSING
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        GLog.d("flowable onSubscribe：");
                        subscription = s;
                        // 只给下游100个事件
                        s.request(100);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        try {
                            Thread.sleep(3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        GLog.d("flowable onNext 收到：" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        GLog.d("flowable onError：" + t.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        GLog.d("flowable onComplete：");
                    }
                });
    }
}