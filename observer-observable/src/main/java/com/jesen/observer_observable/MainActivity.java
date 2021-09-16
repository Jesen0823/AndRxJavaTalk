package com.jesen.observer_observable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 众多观察者
        Observer observer_1 = new ObserverImpl();
        Observer observer_2 = new ObserverImpl();
        Observer observer_3 = new ObserverImpl();
        Observer observer_4 = new ObserverImpl();
        Observer observer_5 = new ObserverImpl();

        // 一个被观察者
        Observable observable = new ObservableImpl();

        // 关联/注册
        observable.registerObserver(observer_1);
        observable.registerObserver(observer_2);
        observable.registerObserver(observer_3);
        observable.registerObserver(observer_4);
        observable.registerObserver(observer_5);

        // 被观察者数据发生变化
        observable.notifyObservers();
    }
}