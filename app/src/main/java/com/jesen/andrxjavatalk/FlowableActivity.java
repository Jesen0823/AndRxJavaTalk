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

/**
 * 背压模式
 */
public class FlowableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowable);
    }

    private Subscription subscription;

    public void flowableClick(View view) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Flowable.create(new FlowableOnSubscribe<Integer>() {
                                    @Override
                                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                                        for (int i = 0; i < Integer.MAX_VALUE; i++) {
                                            e.onNext(i);
                                        }
                                        e.onComplete();
                                    }
                                },
                        BackpressureStrategy.BUFFER
                        //BackpressureStrategy.ERROR
                        //BackpressureStrategy.DROP
                        //BackpressureStrategy.LATEST
                        //BackpressureStrategy.MISSING
                )
                        .subscribe(new Subscriber<Integer>() {
                            @Override
                            public void onSubscribe(Subscription s) {
                                GLog.d("flowable onSubscribe：");
                                subscription = s;
                                // 只给下游100个事件
                                //s.request(100);
                                //s.request(Integer.MAX_VALUE); // 如果策略是buffer 是会正常运行的因为超出的量会被缓存起来
                                s.request(180); // 指定最大容量180，如果用latest策略就只能收到180个
                                //
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
        }).start();
    }
}