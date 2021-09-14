package com.jesen.andrxjavatalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jesen.andrxjavatalk.utils.GLog;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Observable.just("鸡柳", "水果", "葱油饼")
                .filter(new Predicate<String>() {
                    @Override
                     public boolean test(@NonNull String s) throws Exception {
                        if (s.equals("鸡柳")){
                            return false;
                        }
                        return true;
                        // 返回true不过滤，全部接收
                    }
                })
                // 订阅
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        GLog.d("accept : " + s);
                    }
                });
    }
}