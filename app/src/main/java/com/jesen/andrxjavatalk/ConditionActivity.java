package com.jesen.andrxjavatalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.jesen.andrxjavatalk.utils.GLog;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * 条件操作符
 */
public class ConditionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition);
    }

    /**
     * all 条件判断：所有条件都为true才返回true,否则返回false
     * 用途举例： 登录账号页面的账号密码判断，全部符合规范才能登录
     */
    public void allClick(View view) {
        String v1 = "1";
        String v2 = "2";
        String v3 = "3";
        String v4 = "t";
        // 只要有一个是t,就是false

        Observable.just(v1, v2, v3, v4)
                .all(new Predicate<String>() {
                    @Override
                    public boolean test(@NonNull String s) throws Exception {
                        return !s.equals("t");
                    }
                })
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean s) throws Exception {
                        GLog.d("条件操作符 all ， accept：" + s);
                    }
                });
    }

    public void containsClick(View view) {
        Observable.just("java", "c++", "flutter", "Rxjava", "apt", "hook")
                .contains("java")
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean s) throws Exception {
                        GLog.d("条件操作符 contains ， accept：" + s);
                    }
                });
    }

    public void isEmptyClick(View view) {
        Observable.just("java", "c++", "flutter")
                .isEmpty()
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean s) throws Exception {
                        GLog.d("条件操作符 isEmpty ， accept：" + s);
                    }
                });
    }

    // 全部false,才是false,只要有一个是true，就是true
    public void anyClick(View view) {
        Observable.just("红豆", "绿豆", "黄豆","黑豆")
                .any(new Predicate<String>() {
                    @Override
                    public boolean test(@NonNull String s) throws Exception {
                        return s.equals("绿豆");
                    }
                })
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean s) throws Exception {
                        GLog.d("条件操作符 any ， accept：" + s);
                    }
                });
    }
}