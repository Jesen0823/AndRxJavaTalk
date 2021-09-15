# Java中的泛型

1.  定义测试类

```java
public class Person{}

public class Student extends Person{}

public class Teacher extends Person{}

```



```java
public class Test<T> {

    private T t;

    public void add(T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }

}
```

2.  测试

```java
public class TestClient<T> {
    public static void main(String[] args) {


        // 上限
        show1(new Test<Person>());
        show1(new Test<Student>());
        show1(new Test<Worker>());

        // 下限
        show2(new Test<Student>());
        show2(new Test<Person>()); // 父类
        show2(new Test<Object>()); // 父类

        Test<? extends Person> test1 = null;
        // 以下全都不可写
        test1.add(new Person());
        test1.add(new Student());
        test1.add(new Teacher());
        test1.add(new Object());
        Person person = test1.get(); // 可读

        Test<? super Person> test2 = null;
        test2.add(new Person()); // 可写
        test2.add(new Student()); // 可写
        test2.add(new Teacher()); // 可写
        Object object = test.get(); // 不完全可读

    }

    // extends 上限  Person 或 Person的所有子类都可以， 最高的类型只能是Person
    public static <T> void show1(Test<? extends Person> test) {

    }

    // super 下限  Student or Student 的所有父类 都可以  最低的类型只能是Student
    public static <T> void show2(Test<? super Student> test) {

    }
}
```

