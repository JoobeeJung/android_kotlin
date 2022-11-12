package com.kbstar.kotlintest.ch3.test1_pacakge.one
//kotlin에서는 kt파일이 있는 위치와 다른 가상의 패키지명을 줄 수 있다..
//실제 컴파일된 class 파일이 패키지 위치에 들어가게 된다..
import com.kbstar.kotlintest.ch3.test1_pacakge.data
import com.kbstar.kotlintest.ch3.test1_pacakge.formatDate
import java.util.*

fun main() {
    data = 20
    formatDate(Date())
}