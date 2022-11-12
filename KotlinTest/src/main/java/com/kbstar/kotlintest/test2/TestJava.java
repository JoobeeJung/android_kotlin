package com.kbstar.kotlintest.test2;

public class TestJava {
    int a = 10;
    double d = 10.0;

    Integer i = 10;
    Double e = 10.0;

    public static void main(String[] args){
        TestJava obj = new TestJava();
        obj.d = obj.a;

//        obj.a.equals(10); // reference타입(interger 클래스)이고 int는 primitive type
    }
}
