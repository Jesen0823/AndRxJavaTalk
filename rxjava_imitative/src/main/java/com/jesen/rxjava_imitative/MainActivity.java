package com.jesen.rxjava_imitative;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.jesen.rxjava_imitative.rxj.Function;
import com.jesen.rxjava_imitative.rxj.Observable;
import com.jesen.rxjava_imitative.rxj.ObservableOnSubscribe;
import com.jesen.rxjava_imitative.rxj.Observer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 上游
        /*Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(Observer<? super Integer> observableEmitter) {
                observableEmitter.onNext(9);
                observableEmitter.onComplete();
            }
        })
                // Observable<Integer>.subscribe
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) {
                        return 9+"s";
                    }
                })
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) {
                        return s+"88刷新世界纪录";
                    }
                })
                .subscribe(new Observer<String>() { // 下游
                    @Override
                    public void onSubscribe() {
                        Log.d("Main--", "onSubscribe");
                    }

                    @Override
                    public void onNext(String item) {
                        Log.d("Main--", "item = " + item);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });*/


        threadTest();
    }

    public void threadTest(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(Observer<? super Integer> observableEmitter) {
                Log.d("MAin---","上游 subscribe "+Thread.currentThread().getName());
                observableEmitter.onNext(133);
                observableEmitter.onComplete();
            }
        })
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) {
                        Log.d("MAin---","map apply "+Thread.currentThread().getName());
                        return integer+"km";
                    }
                })
                .observableOn()
                .observerAndroidMainOn()
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe() {
                        Log.d("MAin---","下游 onSubscribe "+Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(String item) {
                        Log.d("MAin---","下游 onNext "+Thread.currentThread().getName());
                        Log.d("Main---","下游 onNext："+item);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("MAin---","下游 onError "+Thread.currentThread().getName());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("MAin---","下游 onComplete "+Thread.currentThread().getName());
                    }
                });
    }
}