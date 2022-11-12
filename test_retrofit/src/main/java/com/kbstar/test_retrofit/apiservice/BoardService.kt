package com.kbstar.test_retrofit.apiservice

import com.kbstar.test_retrofit.dto.Board
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BoardService {

    @POST("addBoard.jbmovie")
    fun addBoard(@Body board:Board): Call<String>

    @GET("listBoard.jbmovie")
    fun listBoard(): Call<MutableList<Board>>

    @POST("updateBoard.do")
    fun updateBoard(@Body board: Board): Call<String>

    @POST("deleteBoard.do")
    fun deleteBoard(@Body board: Board): Call<String>

    @POST("getBoard.do")
    fun getBoard(@Body board: Board): Call<Board>
}