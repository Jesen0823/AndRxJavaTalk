package com.jesen.andrxjavatalk.bean;

public class Children {
    private String name = "Wang";
    private String age;

    public Children(String age) {
        this.age = age;
    }

    public Children(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Children{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
