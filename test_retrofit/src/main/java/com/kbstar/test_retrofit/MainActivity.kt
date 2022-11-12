package com.kbstar.test_retrofit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kbstar.test_retrofit.AddActivity
import com.kbstar.test_retrofit.MyApplication
import com.kbstar.test_retrofit.R
import com.kbstar.test_retrofit.databinding.ActivityMainBinding
import com.kbstar.test_retrofit.dto.Board
import com.kbstar.test_retrofit.recyclerview.MyAdapter
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {

    //다른 activity 를 실행하기 위해서는 intent 를 시스템에 발생시켜야 한다..
    //단순 다른 activity 가 실행만 되면 되고.. 되돌아 왔을때.. 특별하게 처리할 일이 없다.. - startActivity
    //다른 activity 실행, 다시 되돌아 올때.. 사후 처리가 있다면...
    //startActivityForResult() 로 intent 발생.. - deprecated...
    lateinit var addLuncher: ActivityResultLauncher<Intent>
    var boardList = mutableListOf<Board>()//서버 데이터
    var adapter = MyAdapter(this, boardList)
    var mode = "add"//순수 개발자 알고리즘에서 필요해서.. add 를 실행시키는 경우가 "add" 와 "update" 구분

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //gridlayoutmanager로 변경
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        val dividerDecoration = DividerItemDecoration(
            this, LinearLayoutManager(this).orientation
        )
        dividerDecoration.setDrawable(resources.getDrawable(R.drawable.divider))
        binding.recyclerView.addItemDecoration(dividerDecoration)

        //목록 데이터 서버에서
        val boardService = (applicationContext as MyApplication).boardService
        val call = boardService.listBoard()
        call.enqueue(object: retrofit2.Callback<MutableList<Board>>{
            override fun onResponse(
                call: Call<MutableList<Board>>,
                response: Response<MutableList<Board>>
            ) {
                boardList.addAll(response.body() ?: mutableListOf()) //없으면 빈것
                adapter.notifyDataSetChanged()//갱신
            }

            override fun onFailure(call: Call<MutableList<Board>>, t: Throwable) {
                t.printStackTrace()
                call.cancel()

            }
        })

        //AddActivity에서 되돌아올때
        addLuncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()//요청처리자.. intent 발생자..
        ){
            //되돌아 올때 callback
            if(mode == "add"){
                //addactivity에서 넘긴 데이터를 받아서..
                it.data?.getParcelableExtra<Board>("result")?.let {
                    boardList.add(0, it) //신규글을 맨 위로 넣는다
                    adapter.notifyDataSetChanged()
                }
            }else
            {
                //update..
                //AddActivity에서 넘긴 데이터 받아서..
                it.data?.getParcelableExtra<Board>("result")?.apply {
                    boardList.forEachIndexed{index, board->
                        if(idx==board.idx){
                            boardList.set(index, this)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        }

        binding.floatingActionButton.setOnClickListener{
            mode = "add"
            //AddActivity 실행
            val intent = Intent(this, AddActivity::class.java)
            addLuncher.launch(intent)
        }

        adapter.updateLiveData.observe(this){
            //livedata에 데이터를 postValue() 하는 순간 실행..
            mode = "update"
            //AddActivity 실행
            val intent = Intent(this, AddActivity::class.java)
            intent.putExtra("dto", it)
            addLuncher.launch(intent)

        }
        adapter.deleteLiveData.observe(this){
            //서버 연동..
            val call = boardService.deleteBoard(it)
            call.enqueue(object : retrofit2.Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val result = response.body()!!

                    if(result == "success")
                    {
                      //화면 갱신.. 항목 삭제..
                        boardList.remove(it)
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    t.printStackTrace()
                    call.cancel()
                }
            })
        }
    }
}