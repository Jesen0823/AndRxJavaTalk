package com.jesen.retofit_rxjava.retrofit;


import io.reactivex.Observable;
import retrofit2.http.Body;

public interface IRequestNetwork {

    // 请求注册
    public Observable<RegisterResponse> registerAction(@Body RegisterRequest registerRequest);

    // 请求登录
    public Observable<LoginResponse> loginAction(@Body LoginRequest loginRequest);
}
