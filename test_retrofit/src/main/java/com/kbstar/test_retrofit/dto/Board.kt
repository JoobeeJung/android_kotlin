package com.kbstar.test_retrofit.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

//서버 연동 데이터 추상화 .. vo, dto ...
//intent 의 extra data에 추가할 것이다.. Parcelable 구현해야함

@Parcelize
data class Board(var idx: Int, var subject: String, var content: String, var writer: String, var regdate: Date, var cnt:Int): Parcelable