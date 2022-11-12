package com.kbstar.kotlintest.test2

fun main() {
    fun ifTest(arg: Int){
        var result = if(arg>10)
        {
            println("arg > 10")
            arg
        }else if(arg>100)
        {
            println("arg > 100")
            100
        }else
        {
            println("arg = 10")
            0
        }
        println("result : $result")
    }
    ifTest(10)

    fun forTest(){
        for(i in 10 downTo 1 step 2)
        {
            println(i)
        }
        val array = arrayOf("hello","world")
        for ((a,b) in array.withIndex())
        {
            println("$a | $b")
        }

        val map = mapOf<String, String>("one" to "hello", "two" to "world")
        for(item in map)
        {
            println("key: ${item.key}, value:${item.value}")
        }
    }
    forTest()

    fun whenTest(arg: Any){
        when(arg){
            1 -> println("1....")
            10,20 -> println("10, 20....")
            30 -> {
                val result = arg as Int * 10
                println("30 ... $result")
            }
            "hello" -> println("hello...")
            is Int -> print("Int...")
            else -> println("else ...")
        }
    }
    whenTest(3)
}