package com.kbstar.kotlintest.test2

fun main(){
    //첫번째 매개변수를 두번째 매개변수를 거쳐서 boolean이 true가 나오는 것만을 필터링해서 리턴하겠다.
    fun myFilter(list: List<Int>, arg: (Int) -> Boolean) : List<Int>
    {
        val resultList = mutableListOf<Int>()

        val iterator = list.iterator()
        while (iterator.hasNext())
        {
            val no = iterator.next();
            //매개변수로 전달된 함수를 호출하고
            val result = arg(no)
            if(result)
            {
                resultList.add(no)
            }
        }
        return resultList
    }

    val testList = listOf<Int>(10,13,3,6,20)
    //람다함수 ... 매개변수 하나, 매개변수 선언 생략하고 .. it 으로 대체 ...
//    val resultList = myFilter(testList, {no -> no > 10}) //아래처럼 축약가능
//    val resultList = myFilter(testList, {it > 10})
    // 매개변수가 함수 타입이다.. 함수의 매개변수는 () 안에 선언해야하지만..
    // 마지막 매개변수만 .. 함수타입이라면 ..() 밖에다 선언 가능..

//    resultList.forEach({no -> println(no)})
    val resultList = myFilter(testList){it > 10}
//    resultList.forEach({println(it)})
    resultList.forEach{println(it)}

}