package com.jesen.rxjava_imitative.rxj;

import com.jesen.rxjava_imitative.ObservableMap;

//  被观察者 上游
public class Observable<T> { // 类声明的泛型T  Int

    ObservableOnSubscribe source;

    private Observable(ObservableOnSubscribe source) {
        this.source = source;
    }

    // 静态方法声明的<T>泛型        ObservableOnSubscribe<T>==静态方法声明的<T>泛型
    // 参数中：ObservableOnSubscribe<? extends T> 和可读可写模式没有任何关系，还是我们之前的那一套思想（上限和下限）
    public static <T> Observable<T> create(ObservableOnSubscribe<? extends T> source) { // int
        return new Observable<T>(source); // 静态方法声明的<T>泛型 int
    }

    // new Observable<T>(source).subscribe(Observer<Int>)
    // 参数中：Observer<? extends T> 和可读可写模式没有任何关系，还是我们之前的那一套思想（上限和下限）
    public void subscribe(Observer<? extends T> observer) {

        observer.onSubscribe();

        source.subscribe(observer);

    }

    /////////////////////////////////////////////////////////////////////////////

    // 可变参数
    public static <T> Observable<T> just(final T...t){
        // 给source赋值
        return new Observable<T>(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(Observer<? super T> observableEmitter) {
                for (T t1 : t) {
                    // 发送传递过来的事件
                    observableEmitter.onNext(t1);
                }
                observableEmitter.onComplete();
            }
        });
    }

    // 单个参数
    public static <T> Observable<T> just(final T t){
        return new Observable<T>(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(Observer<? super T> observableEmitter) {

                observableEmitter.onNext(t);
                observableEmitter.onComplete();
            }
        });
    }

    // 3个参数，4个参数....10个参数的方法省略

    //////////////////////////////////////////////////////////

    // T可写  R可读
    public <R> Observable<R> map(Function<? super T, ? extends R> function){
        ObservableMap observableMap = new ObservableMap(source, function);
        return new Observable<R>(observableMap);
    }

    /////////////////////////////////////////////////////////////

    // 线程切换

    public Observable<T> observableOn(){
        return create(new ObservableOnIO(source));
    }

    public Observable<T> observerAndroidMainOn() {
        ObservableOnAndroidMain<T> observableOnAndroidMain = new ObservableOnAndroidMain(source);
        return create(observableOnAndroidMain);
    }
}
