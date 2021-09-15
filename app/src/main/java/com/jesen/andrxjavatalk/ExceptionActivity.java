package com.jesen.andrxjavatalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.jesen.andrxjavatalk.utils.GLog;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Rxjava的异常处理
 * */
public class ExceptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exception);
    }

    // 能在发送异常时，捕获异常，可以发送异常标记，比较常用
    public void onExceptionResumeNextClick(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 100; i++) {
                    if (i == 10){
                        //throw new IllegalAccessException("exeception");
                        e.onError(new IllegalAccessException("迷你错误"));
                    }
                    e.onNext(i);
                }
                // 要保证onComplete最后执行
                e.onComplete();
            }
        })
                .onExceptionResumeNext(new ObservableSource<Integer>() {
                    @Override
                    public void subscribe(Observer<? super Integer> observer) {
                        observer.onNext(404);
                        observer.onNext(300);
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        GLog.d("异常操作符 onSubscribe");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        GLog.d("异常操作符 onNext,： "+integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        GLog.d("异常操作符 onError");
                    }

                    @Override
                    public void onComplete() {
                        GLog.d("异常操作符 onComplete");
                    }
                });
    }

    // onErrorResumeNext 能接收onError()， 返回一个被观察者，可以再次发送多个事件给下游
    public void onErrorResumeNextClick(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 50; i++) {
                    if (i == 5){
                        e.onError(new Error("错了"));
                    }
                    e.onNext(i);
                }
                e.onComplete();
            }
        })
                .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends Integer>>() {
                    @NonNull
                    @Override
                    public ObservableSource<? extends Integer> apply(@NonNull Throwable throwable) throws Exception {
                        // onErrorResumeNext 返回一个被观察者，可以再次发送给下游
                        return Observable.create(new ObservableOnSubscribe<Integer>() {
                            @Override
                            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                                e.onNext(404);
                                e.onNext(800);
                                e.onNext(404);
                                e.onNext(405);
                                e.onComplete();
                            }
                        });
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        GLog.d("异常处理 onSubscribe");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        GLog.d("异常处理 onNext： "+ integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        GLog.d("异常处理 onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        GLog.d("异常处理 onComplete");
                    }
                });
    }

    // 能接收onError()，如果发生异常,返回异常标识，，会中断后面所有事件，观察者能收到异常，不会再接收到后面的事件
    public void onErrorReturnClick(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 100; i++) {

                    if (i == 10){
                        e.onError(new IllegalAccessError("模拟异常"));
                    }
                    e.onNext(i);
                }
                e.onComplete();
            }
        })
                // 添加异常操作符
                .onErrorReturn(new Function<Throwable, Integer>() {
                    @NonNull
                    @Override
                    public Integer apply(@NonNull Throwable throwable) throws Exception {
                        GLog.d(" 异常处理 onErrorReturn, apply: "+ throwable.getMessage());
                        return 102;
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        GLog.d(" 异常处理 onSubscribe");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        GLog.d(" 异常处理 onNext："+integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        GLog.d(" 异常处理 onError");
                    }

                    @Override
                    public void onComplete() {
                        GLog.d(" 异常处理 onComplete");
                    }
                });
    }

    public void retryClick(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 100; i++) {

                    if (i == 10){
                        e.onError(new Exception("模拟异常"));
                    }
                    e.onNext(i);
                }
                e.onComplete();
            }
        })
                // 一直重试下去
                /*.retry(new BiPredicate<Integer, Throwable>() {
                    @Override
                    public boolean test(@NonNull Integer integer, @NonNull Throwable throwable) throws Exception {
                        GLog.d("retry test throwable: "+throwable.getMessage());
                        // false 表示不重试, true表示一直重试
                        return true;
                    }
                })*/

                // 重试次数可定制
                /*.retry(3, new Predicate<Throwable>() {
                    @Override
                    public boolean test(@NonNull Throwable throwable) throws Exception {
                        GLog.d("retry test throwable: "+throwable.getMessage());
                        return true; // false不会重试
                    }
                })*/
                .retry(new BiPredicate<Integer, Throwable>() {
                    @Override
                    public boolean test(@NonNull Integer integer, @NonNull Throwable throwable) throws Exception {
                        GLog.d("retry test, 重试次数"+integer+", throwable: "+throwable.getMessage());
                        return false;
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        GLog.d(" 异常处理 onSubscribe");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        GLog.d(" 异常处理 onNext："+integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        GLog.d(" 异常处理 onError");
                    }

                    @Override
                    public void onComplete() {
                        GLog.d(" 异常处理 onComplete");
                    }
                });
    }
}