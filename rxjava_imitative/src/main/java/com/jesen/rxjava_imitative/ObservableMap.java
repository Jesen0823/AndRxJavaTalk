package com.jesen.rxjava_imitative;

import com.jesen.rxjava_imitative.rxj.Function;
import com.jesen.rxjava_imitative.rxj.ObservableOnSubscribe;
import com.jesen.rxjava_imitative.rxj.Observer;

/**
 * map方法独有的
 * 要拥有控制下一层的能力，控制上一层的能力
 */


// ObservableMap<T> 的泛型传递给了 ObservableOnSubscribe<T>
public class ObservableMap<T, R> implements ObservableOnSubscribe<R> {

    private ObservableOnSubscribe<T> source; // 上一层的能力
    private Function<? super T, ? extends R> function;
    private Observer<? super R> observableEmitter; // 下一层的能力


    public ObservableMap(ObservableOnSubscribe source, Function<? super T, ? extends R> function) {
        this.source = source;
        this.function = function;
    }

    @Override
    public void subscribe(Observer<? super R> observableEmitter) {
        this.observableEmitter = observableEmitter;

        // source.subscribe(observableEmitter); // 不应该把下一层Observer交出去 ---》 上一层， 如果交出去了，map没有控制权

        MapObserver<T> mapObserver = new MapObserver(observableEmitter, source, function);

        source.subscribe(mapObserver); // 把我们自己 map MapObserver 交出去了
    }

    // 真正拥有控制下一层的能力  让map拥有控制权力  observer,source,function
    class MapObserver<T> implements Observer<T> {

        private Observer</*? super */R> observableEmitter; // 给下一层的类型，意思是 变换后的类型 也就是给下一层的类型 R
        private ObservableOnSubscribe<T> source;
        private Function<? super T, ? extends R> function;

        public MapObserver(Observer</*? super */R> observableEmitter,
                           ObservableOnSubscribe<T> source,
                           Function<? super T, ? extends R> function) {
            this.observableEmitter = observableEmitter;
            this.source = source;
            this.function = function;

        }

        @Override
        public void onSubscribe() {
            // observableEmitter.onSubscribe();
        }

        @Override
        public void onNext(T item) { // 真正做变换的操作

            /**
             * T Integer    变换     R String
             */

            R nextMapResultSuccesType = function.apply(item);

            // 调用下一层 onNext 方法
            observableEmitter.onNext(nextMapResultSuccesType);
        }

        @Override
        public void onError(Throwable e) {
            observableEmitter.onError(e);
        }

        @Override
        public void onComplete() {
            observableEmitter.onComplete();
        }
    }
}
