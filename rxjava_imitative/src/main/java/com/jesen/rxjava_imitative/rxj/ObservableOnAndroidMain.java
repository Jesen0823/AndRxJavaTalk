package com.jesen.rxjava_imitative.rxj;

import android.os.Handler;
import android.os.Looper;

/**
 * 下游切换主线程
 * */
public class ObservableOnAndroidMain<T> implements ObservableOnSubscribe<T>{

    private ObservableOnSubscribe<T> source;

    public ObservableOnAndroidMain(ObservableOnSubscribe<T> source){
        this.source = source;
    }

    @Override
    public void subscribe(Observer<? super T> observableEmitter) {
        WrapObserver<T> wrapObserver = new WrapObserver(observableEmitter);
        source.subscribe(wrapObserver);
    }

    // 包裹Observer
    private final class WrapObserver<T> implements Observer<T>{


        private Observer<T> observer;

        public WrapObserver(  Observer<T> observer){
            this.observer = observer;
        }

        @Override
        public void onSubscribe() {

        }

        @Override
        public void onNext(T item) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    // Android主线程
                    observer.onNext(item);
                }
            });
        }

        @Override
        public void onError(Throwable e) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    // Android主线程
                    observer.onError(e);
                }
            });
        }

        @Override
        public void onComplete() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    // Android主线程
                    observer.onComplete();
                }
            });
        }
    }
}
