package com.jesen.andrxjavatalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.jesen.andrxjavatalk.bean.Children;
import com.jesen.andrxjavatalk.utils.GLog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;

public class ConvertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);
    }

    public void mapClick(View view) {

        Observable.just(10)
                // 在上游下游间给事件做变换
                .map(new Function<Integer, String>() {

                    @NonNull
                    @Override
                    public String apply(@NonNull Integer integer) throws Exception {
                        return "[" + integer + "]";
                    }
                })
                // 再次做变换
                .map(new Function<String, Children>() {

                    @NonNull
                    @Override
                    public Children apply(@NonNull String s) throws Exception {
                        return new Children(s);
                    }
                })
                .subscribe(
                        new Observer<Children>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                GLog.d("map 操作符变换后，onSubscribe ");
                            }

                            @Override
                            public void onNext(Children obj) {
                                GLog.d("map 操作符变换后，onNext " + obj.toString());
                            }

                            @Override
                            public void onError(Throwable e) {
                                GLog.d("map 操作符变换后，onError ");
                            }

                            @Override
                            public void onComplete() {
                                GLog.d("map 操作符变换后，onComplete ");
                            }
                        }
                );
    }

    public void flatMapClick(View view) {
        Observable.just(100, 101, 105)
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @NonNull
                    @Override
                    public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
                        // 转换后ObservableSource可以再次发射
                        return Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> e) throws Exception {
                                e.onNext("flatMap变换| " + integer + " s");
                                e.onNext("flatMap变换|| " + integer + " s");
                                e.onNext("flatMap变换||| " + integer + " s");
                            }
                        });
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String integer) throws Exception {
                        GLog.d("flatMap变换后， accept：" + integer);
                    }
                });
    }

    public void flatMapClick2(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(106);
                e.onNext(101);
                e.onNext(105);
            }
        })
                .flatMap(new Function<Integer, ObservableSource<?>>() {
                    @NonNull
                    @Override
                    public ObservableSource<?> apply(@NonNull Integer integer) throws Exception {
                        List<Integer> list = new ArrayList<>();
                        for (int i = 0; i < 3; i++) {
                            list.add(integer + 100);
                        }
                        return Observable.fromIterable(list).delay(1, TimeUnit.SECONDS);
                    }
                })
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object integer) throws Exception {
                        GLog.d("concatMap转换后: " + integer.toString());
                    }
                });
    }

    public void concatMapClick(View view) {
        Observable.just("A", "B", "C", "D")
                .concatMap(new Function<String, ObservableSource<?>>() {
                    @NonNull
                    @Override
                    public ObservableSource<?> apply(@NonNull String s) throws Exception {
                        List<String> list = new ArrayList<>();
                        for (int i = 0; i < 3; i++) {
                            list.add(s + ",index=" + (i + 1));
                        }
                        return Observable.fromIterable(list).delay(3000, TimeUnit.MICROSECONDS);
                    }
                })
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object s) throws Exception {
                        GLog.d("concatMap 变换后，" + s.toString());
                    }
                });
    }

    public void groupByClick(View view) {
        Observable.just(500, 2400, 800, 1200, 10000)
                .groupBy(new Function<Integer, String>() {
                    @NonNull
                    @Override
                    public String apply(@NonNull Integer integer) throws Exception {
                        return integer > 2000 ? "高配版" : "低配版";
                    }
                })
                // 使用 groupBy，下游是有规范的
                .subscribe(new Consumer<GroupedObservable<String, Integer>>() {
                    @Override
                    public void accept(@NonNull GroupedObservable<String, Integer> stringIntegerGroupedObservable) throws Exception {
                        GLog.d("groupBy变换后，accept, key：" + stringIntegerGroupedObservable.getKey());
                        // GroupedObservable 是一个被观察者，需要再次订阅
                        stringIntegerGroupedObservable.subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(@NonNull Integer integer) throws Exception {
                                GLog.d("groupBy变换后，accept, key:" + stringIntegerGroupedObservable.getKey() + ",value：" + integer);
                            }
                        });
                    }
                });
    }

    // 多个事件不想全部发送，可以缓存到buffer分批次发送
    public void bufferClick(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
                              @Override
                              public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                                  for (int i = 0; i < 100; i++) {
                                      e.onNext(i);
                                  }
                                  e.onComplete();
                              }
                          }
        ).buffer(21)
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(@NonNull List<Integer> integer) throws Exception {
                        GLog.d("buffer变换后，accept：" + integer);
                    }
                });
    }
}