package com.kbstar.kotlintest.test2
// 기초데이터 타입 : 숫자 int, long, short, double, boolean
// String은 클래스
// lateinit은 클래스만 가능 왜냐하면 기초데이터는 int = 0 이렇게 초기화할 수 있기 때문에

var a: Int = 10 //Int클래스의 객체 . int라는 primitive type이 아님
var d: Double = 10.0

fun some(){
    a.plus(10) //코틀린에서는 모든게 객체
    d = a.toDouble()
}

class User

class Test {
    var a: Int = 0
    lateinit var b: String; //lateinit으로 나중에 초기화 할게 라고 선언
    var c: Boolean = false;
    var d: Double = 0.0;
//    lateinit var d: Double; //'lateinit' modifier is not allowed on properties of primitive types
    lateinit var obj: User; //lateinit val은 불가. val는 변경이 안되기 때문에

    val e: Int by lazy { //이용되는 순간에 초기화하는 것
        println("1.......")
        10
    }
}

fun main(){
    println("2......")
    val obj = Test()
    println(obj.e)
    println("3.....")
}