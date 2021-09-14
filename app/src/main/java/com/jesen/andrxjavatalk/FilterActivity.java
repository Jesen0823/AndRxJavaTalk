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
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

    }

    public void filterClick(View view) {
        Observable.just("鸡柳", "水果", "葱油饼")
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(@NonNull String s) throws Exception {
                        if (s.equals("鸡柳")) {
                            return false;
                        }
                        return true;
                        // 返回true不过滤，全部接收
                    }
                })
                // 订阅
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        GLog.d("accept : " + s);
                    }
                });
    }

    public void takeClick(View view) {
        // take操作符只有和定时器Observable.interval一起使用才有意义
        Observable.interval(2, TimeUnit.SECONDS)
                // 增加过滤操作符
                .take(8)  // 执行数到达8会停止计时器
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        GLog.d("take过滤符 过滤后  accept： " + aLong);
                    }
                });
    }

    public void distinctClick(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(1);
                e.onNext(3);
                e.onNext(4);
                e.onNext(3);
                e.onNext(5);
                e.onComplete();
            }
        })
                // 过滤重复发送的事件
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        GLog.d("distinct过滤符 过滤后  accept： " + integer);
                    }
                });
    }

    public void elementAtClick(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(100);
                e.onNext(100);
                e.onNext(300);
                e.onNext(500);
                e.onNext(20);
                e.onNext(100);
                e.onComplete();
            }
        })
                .elementAt(3,0)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        GLog.d("elementAt过滤符 过滤后  accept： " + integer);
                    }
                });
    }
}