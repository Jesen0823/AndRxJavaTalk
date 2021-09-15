package com.jesen.rxjava_imitative.rxj;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 上游线程切换异步线程
public class ObservableOnIO <T> implements ObservableOnSubscribe<T>{
    private final static ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    private ObservableOnSubscribe<T> source;

    public ObservableOnIO(ObservableOnSubscribe<T> source){
        this.source = source;
    }


    @Override
    public void subscribe(Observer<? super T> observableEmitter) {
        EXECUTOR_SERVICE.execute(new Runnable() {
            @Override
            public void run() {
                source.subscribe(observableEmitter);
            }
        });
    }
}
