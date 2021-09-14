# AndRxJavaTalk
RxJava 复习

### 基础知识

1. 创建操作符

* **用来创建Observable的创建操作符**
create 需要使用者手动发送事件

    | 操作符        | 谁来发送事件    |  不需要使用者手动发送事件  |
    | --------     | ------  | ------ |
    | create       | 使用者手动发送         |                  |
    | just         | 内部会自己发送事件      |   可发送多个事件    |
    | formArray    | 内部自己会发送事件      |   发送的事件是个数组    |
    | empty        | 内部自己会发送事件      |   上游自动发送空事件Object类型，下游无法确定事件类型,onComplete一定会被触发    |
    | range        | 内部自己会发送事件      |   发送一串连续指定个数的事件序列    |


* **用来创建Observer的创建操作符**

new Observer(...) 普通版
new Consumer(...) 简化版，不需要覆写所有的方法如 onSubscribe， onComplete, onError()...