package com.jesen.rxjava_imitative.rxj;

public interface ObservableOnSubscribe<T> { // T == String

    // ? super  代表可写的   observableEmitter == 观察者
    public void subscribe(Observer<? super T> observableEmitter); // Observer<String>

}
