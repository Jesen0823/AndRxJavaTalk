package com.jesen.rxjava_apply;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jakewharton.rxbinding4.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import kotlin.Unit;

public class MainActivity extends AppCompatActivity {

    private long lastTime = 0;
    private Button clickBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clickBtn = findViewById(R.id.clickBtn);
    }

    public void onClickIt(View view) {

    }

    // 普通的防暴力点击
    private void clickPro1(View view){
        long curTime = System.currentTimeMillis();
        // 间隔大于500毫秒认为是有效的
        if (lastTime - curTime > 500){

        }
        lastTime = curTime;
    }

    // Rxjava防暴力点击
    private void clickPro2(View view){
        RxView.clicks(clickBtn).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Observer<Unit>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Unit unit) {
                        Log.d("Main---"," onNext, clicked it");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}