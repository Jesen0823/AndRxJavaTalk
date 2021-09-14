package com.jesen.andrxjavatalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.jesen.andrxjavatalk.utils.GLog;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
    }

    public void rangeClick(View view) {
        Observable.range(100,10)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        GLog.d("range相关 accept，"+integer);
                    }
                });
    }

    public void formArrayClick(View view)  {
        String[] events = {"fromArray01","fromArray02","fromArray03","fromArray04"};
        Observable.fromArray(events)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        GLog.d("fromArray相关 onSubscribe");
                    }

                    @Override
                    public void onNext(String s) {
                        GLog.d("fromArray相关 onNext， "+s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        GLog.d("fromArray相关 onError");
                    }

                    @Override
                    public void onComplete() {
                        GLog.d("fromArray相关 onComplete");
                    }
                });
    }

    public void justClick(View view) {
        // 内部会自己发誓事件，不需要使用者手动发送事件
         Observable.just("just相关事件A","just相关事件B")
         .subscribe(new Observer<String>() {
             @Override
             public void onSubscribe(Disposable d) {
                 GLog.d("just相关 onSubscribe");
             }

             @Override
             public void onNext(String s) {
                 GLog.d("just相关 onNext: "+s);
             }

             @Override
             public void onError(Throwable e) {
                 GLog.d("just相关 onError");
             }

             @Override
             public void onComplete() {
                 GLog.d("just相关 onComplete");
             }
         });
    }

    public void emptyClick(View view) {
        Observable.empty()
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        GLog.d("empty相关 onSubscribe");
                    }

                    @Override
                    public void onNext(Object o) {
                        GLog.d("empty相关 onNext，"+o.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        GLog.d(" empty相关 onError");
                    }

                    @Override
                    public void onComplete() {
                        GLog.d("empty相关 onComplete");
                    }
                });
    }

    public void createClick(View view) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("create相关 事件A");
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                GLog.d("create相关 onSubscribe");
            }

            @Override
            public void onNext(String s) {
                GLog.d("create相关 onNext: "+s);
            }

            @Override
            public void onError(Throwable e) {
                GLog.d("create相关 onError");
            }

            @Override
            public void onComplete() {
                GLog.d("create相关 onComplete");
            }
        });
    }
}