package com.jesen.retofit_rxjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jesen.retofit_rxjava.retrofit.IRequestNetwork;
import com.jesen.retofit_rxjava.retrofit.LoginRequest;
import com.jesen.retofit_rxjava.retrofit.LoginResponse;
import com.jesen.retofit_rxjava.retrofit.MyRetrofit;
import com.jesen.retofit_rxjava.retrofit.RegisterRequest;
import com.jesen.retofit_rxjava.retrofit.RegisterResponse;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 请求注册，注册完成马上登陆
 */
public class MainActivity extends AppCompatActivity {

    private TextView tvRegister, tvLogin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvRegister = findViewById(R.id.tv_register);
        tvLogin = findViewById(R.id.tv_login);
    }

    public void requestClick(View view) {

        MyRetrofit.createRetrofit().create(IRequestNetwork.class)
                // 返回一个注册的被观察者
                .registerAction(new RegisterRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RegisterResponse>() {
                    @Override
                    public void accept(@NonNull RegisterResponse registerResponse) throws Exception {
                        // 更新注册的UI
                        tvRegister.setText("注册成功");
                        tvRegister.setTextColor(Color.BLUE);
                    }
                });

        MyRetrofit.createRetrofit().create(IRequestNetwork.class)
                .loginAction(new LoginRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResponse>() {
                    @Override
                    public void accept(@NonNull LoginResponse loginResponse) throws Exception {
                        // 更新登录UI
                        tvLogin.setText("登录成功");
                        tvLogin.setTextColor(Color.BLUE);
                    }
                });
    }

    public void requestChainClick(View view) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("请稍后...");

        MyRetrofit.createRetrofit().create(IRequestNetwork.class)
                // 返回一个注册的被观察者
                .registerAction(new RegisterRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<RegisterResponse>() {
                    @Override
                    public void accept(@NonNull RegisterResponse registerResponse) throws Exception {
                        // 更新注册相关UI
                        tvRegister.setText("注册成功");
                        tvRegister.setTextColor(Color.BLUE);
                    }
                })
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<RegisterResponse, ObservableSource<LoginResponse>>() {
                    @NonNull
                    @Override
                    public ObservableSource<LoginResponse> apply(@NonNull RegisterResponse registerResponse) throws Exception {
                        Observable<LoginResponse> observable = MyRetrofit.createRetrofit().create(IRequestNetwork.class)
                                .loginAction(new LoginRequest());
                        return observable;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        progressDialog.show();
                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        // 更新登录UI
                        tvLogin.setText("登录成功");
                        tvLogin.setTextColor(Color.BLUE);

                        Log.d("Main---", "loginResponse " + loginResponse.toString());
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                    }
                });


    }
}