package com.kbstar.test_retrofit

import android.app.Application
import com.google.gson.GsonBuilder
import com.kbstar.test_retrofit.apiservice.BoardService
import com.kbstar.test_retrofit.dto.Board
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication : Application() {
    var boardService: BoardService

    val retrofit: Retrofit
        get() {
            //retrofit 을 사용하면서 필수는 아니지만.. 이용하면..
            //request, response 로그를 자동으로 뿌려줘서.. 디버깅 하기 용이하다..
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient().newBuilder().addInterceptor(interceptor).build()

            //날짜 데이터가 있다..
            //날짜를 문자열 타입으로 취급하면 문제가 없겠지만...
            //java 의 Date 타입으로 취급한다면..
            //android 에서 Date 타입의 포멧과 spring 에서 해석하는 Date 타입의 포멧이 맞지 않아서
            //에러발생..
            val gson = GsonBuilder()
                .setLenient()//gson 으로 json 데이터를 파싱할 건데.. 서버에서 단순 문자열을 리턴하는 경우도 있어서
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")//서버와  date 포멧을 맞추기 위해서..
                .create()

            return Retrofit.Builder()
                .baseUrl("http://10.10.220.79:7777/app/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        }

    init {
        boardService = retrofit.create(BoardService::class.java)
    }
}










