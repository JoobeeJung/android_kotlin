package com.kbstar.kotlintest.ch3.test1_pacakge

//동일 패키지에 있지 않은 다른 패키지의 멤버(클래스, 변수, 함수)를 사용하겠다면..
//import로 선언해서 사용해야
import java.text.SimpleDateFormat
import java.util.*

//코틀린에서는 변수 함수가 클래스로 묶이지 않고 top level에 선언 가능
var data = 10
fun formatDate(date: Date): String{
    val sdformat = SimpleDateFormat("yyyy-MM-dd")
    return sdformat.format(date)
}

class User {
    var name = "홍길동"
    fun sayHello(){
        println("Hello $name")
    }

}