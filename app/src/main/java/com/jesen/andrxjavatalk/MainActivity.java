package com.jesen.andrxjavatalk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jesen.andrxjavatalk.utils.GLog;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 日志顺序：
 *
 * D/RxJaJa---: 观察者， onSubscribe
 * D/RxJaJa---: 发射器发射事件
 * D/RxJaJa---: 观察者， onNext 接收了事件：11
 * D/RxJaJa---: 发射事件 11 完成
 * */

public class MainActivity extends AppCompatActivity {

    private Disposable d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 被观察者
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                GLog.d("发射器发射事件");
                e.onNext(11);
                GLog.d("发射事件 11 完成");

                GLog.d("还想发射，可以吗？");
                // 可以补发事件，下面可以继续发射
                // 但如果 主动完成e.onComplete()，下面事件可以继续补发，但是观察者再也接收不到补发事件
                //e.onComplete();

                e.onNext(12);
                e.onNext(13);
                e.onNext(14);
                GLog.d("补发完成");
            }
        });

        // 观察者
        Observer<Integer> observer = new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {
                GLog.d("观察者， onSubscribe");
                MainActivity.this.d = d;
            }

            @Override
            public void onNext(Integer integer) {
                GLog.d("观察者， onNext 接收了事件："+ integer);

                if (integer == 13){
                    // 下游切断事件，将不会接收后面的事件
                    d.dispose();
                }
            }

            @Override
            public void onError(Throwable e) {
                GLog.d("观察者， onError");
            }

            @Override
            public void onComplete() {
                GLog.d("观察者， onComplete");
            }
        };

        // 产生订阅事件
        observable.subscribe(observer);
    }
}