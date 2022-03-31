package com.jesen.andrxjavatalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;

import com.jesen.andrxjavatalk.utils.GLog;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SwitchThreadActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ImageView mIv;
    private static final String TEST_URL = "https://www.fzdm.org/uploads/news/2021/1615097044175844.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_thread);

        mIv = findViewById(R.id.iv);
    }

    public void switchThreadClick(View view) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                GLog.d("上游被观察者 subscribe | " + Thread.currentThread().getName());
                e.onNext("BBBBBBBBBBB");
            }
        })
                .subscribeOn(Schedulers.newThread()) // 新线程
                .observeOn(AndroidSchedulers.mainThread()) // 主线程
                .observeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        GLog.d("下游 观察者 onSubscribe| " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(String s) {
                        GLog.d("下游 观察者 onNext： " + s + "|" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        GLog.d("下游 观察者 onError");
                    }

                    @Override
                    public void onComplete() {
                        GLog.d("下游 观察者 onComplete");
                    }
                });
    }

    public void loadImgClick(View view) {

        Observable.just(TEST_URL)
                .map(new Function<String, Bitmap>() {
                    @NonNull
                    @Override
                    public Bitmap apply(@NonNull String s) throws Exception {
                        try {
                            Thread.sleep(1000);
                            URL url = new URL(TEST_URL);
                            URLConnection urlConnection = url.openConnection();
                            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                            httpURLConnection.setConnectTimeout(5000);
                            int responseCode = httpURLConnection.getResponseCode();
                            if (HttpURLConnection.HTTP_OK == responseCode) {
                                Bitmap bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
                                return bitmap;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .map(new Function<Bitmap, Bitmap>() {
                    @Override
                    public Bitmap apply(Bitmap bitmap) throws Exception {
                        // 给图片加水印
                        Paint paint = new Paint();
                        paint.setColor(Color.GREEN);
                        paint.setTextSize(40);
                        Bitmap bitmapSuccess = drawTextToBitmap(bitmap, "现在是 "+System.currentTimeMillis(), paint, 60, 60);
                        return bitmapSuccess;
                    }
                })

                // 比如：增加一个 日志纪录功能，只需要添加要给 变换操作符
                .map(new Function<Bitmap, Bitmap>() {
                    @Override
                    public Bitmap apply(Bitmap bitmap) throws Exception {
                        GLog.d( "apply: 下载的Bitmap :" + bitmap);
                        return bitmap;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        progressDialog = new ProgressDialog(SwitchThreadActivity.this);
                        progressDialog.setMessage("loading...");
                        progressDialog.show();
                    }

                    @Override
                    public void onNext(Bitmap s) {
                        if (s != null) {
                            mIv.setImageBitmap(s);
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                });
    }


    //图片上绘制文字
    private Bitmap drawTextToBitmap(Bitmap bitmap, String text, Paint paint, int paddingLeft, int paddingTop) {
        Bitmap.Config bitmapConfig = bitmap.getConfig();

        paint.setDither(true); // 获取跟清晰的图像采样
        paint.setFilterBitmap(true);// 过滤一些
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawText(text, paddingLeft, paddingTop, paint);
        return bitmap;
    }

}