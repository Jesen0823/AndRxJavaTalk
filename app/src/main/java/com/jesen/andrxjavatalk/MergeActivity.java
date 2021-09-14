package com.jesen.andrxjavatalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.jesen.andrxjavatalk.utils.GLog;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

public class MergeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge);
    }

    // 最多能合并4个被观察者,并列执行
    public void mergeClick(View view) {
        Observable<Long> observable1 = Observable.intervalRange(1, 5, 1000, 2000, TimeUnit.MICROSECONDS);
        Observable<Long> observable2 = Observable.intervalRange(6,10,1000,2000, TimeUnit.MICROSECONDS);
        Observable<Long> observable3 = Observable.intervalRange(11,5,1000,2000, TimeUnit.MICROSECONDS);
        Observable.merge(observable1,observable2,observable3)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        GLog.d("merge 合并， accept ："+aLong);
                    }
                });
    }

    public void zipClick(View view) {
        Observable<String> observable1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("数学");
                e.onNext("英语");
                e.onNext("语文");
                e.onComplete();
            }
        });

        Observable<Integer> observable2 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(90);
                e.onNext(65);
                e.onNext(70);
                e.onComplete();
            }
        });

        // 合并最多9个被观察者，多个事件的结果合并成为一个结果
        Observable.zip(observable1, observable2, new BiFunction<String, Integer, StringBuffer>() {
            @NonNull
            @Override
            public StringBuffer apply(@NonNull String s, @NonNull Integer integer) throws Exception {
                return new StringBuffer().append("课程：").append(s).append(",得分：").append(integer);
            }
        })
                .subscribe(new Consumer<StringBuffer>() {
                    @Override
                    public void accept(@NonNull StringBuffer stringBuffer) throws Exception {
                        GLog.d("zip 合并符：accept ："+stringBuffer);
                    }
                });
    }

    // concat 最多能合并4个被观察者,按存入的顺序执行
    public void concatClick(View view) {

        Observable.concat(
                Observable.just("1","2"),
                Observable.just("3"),
                Observable.just("4"),
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        e.onNext("5");
                        e.onComplete();
                    }
                })
        )
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        GLog.d("concat 合并后： "+s);
                    }
                });
    }

    // 观察者1.concatWith(观察者2)  会先收到观察者1再收到观察者2
    public void startWithClick(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        })
                .startWith(Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        e.onNext(10);
                        e.onNext(20);
                        e.onNext(30);
                        e.onComplete();
                    }
                }))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        GLog.d("startWith 合并后： "+ integer);
                    }
                });

    }

    // 与startWith相反，
    public void concatWithClick(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        })
                .concatWith(Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        e.onNext(10);
                        e.onNext(20);
                        e.onNext(30);
                        e.onComplete();
                    }
                }))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        GLog.d("startWith 合并后： "+ integer);
                    }
                });

    }
}