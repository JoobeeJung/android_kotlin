package com.kbstar.test_retrofit.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.kbstar.test_retrofit.databinding.ItemMainBinding
import com.kbstar.test_retrofit.dto.Board
import java.text.SimpleDateFormat

class MyViewHolder(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root)

//datas - MainActivity 가 받은 항목을 구성하기 위한 서버데이터..
class MyAdapter(val context: Context, val datas: MutableList<Board>)
    : RecyclerView.Adapter<MyViewHolder>(){

    val updateLiveData = MutableLiveData<Board>()
    val deleteLiveData = MutableLiveData<Board>()

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemMainBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.titleView.text = datas[position].subject
        holder.binding.contentView.text = datas[position].content
        holder.binding.dateView.text =
            SimpleDateFormat("yyyy-MM-dd").format(datas[position].regdate)

        //항목 전체를 클릭 했을때... AddActivity 를 실행.. 항목 데이터를 넘겨서..  update 되게..
        holder.binding.root.setOnClickListener{
            //AddActivity 를 실행하면.. 단순 intent 만 띄우면 된다면 여기에서 작성하겠지만..
            //addActivity 에서 수정했다가 MainActivity 로 되돌아 올때.. 수정했던 데이터를 받아서..
            //Main 의 목록도 갱신해야 한다..
            //즉 activity 가 되돌아 올때 사후처리 작업이 있는 것이다..

            //어떤 상황이 발생했을때.. 그 상황이 발생한 곳에서는... 특정 상황이 발생했다는 데이터만 발생..
            //누군가가.. 그 데이터 발행을 감지해서.. 상황 발생순간.. 처리할 일을 처리할 수 있게..
            //LiveData 이다..
            updateLiveData.postValue(datas[position])

        }

        //X 아이콘 클릭했을때.. 항목 데이터 삭제되게...
        holder.binding.deleteImageView.setOnClickListener {
            AlertDialog.Builder(context).run {
//                andoridx 쓰는게 좋음

                setIcon(android.R.drawable.ic_dialog_alert)
                setTitle("Warning")
                setMessage("정말 삭제하시겠습니까?")
                setPositiveButton("OK", {dialog, whitch ->
                    //삭제.. 서버 연동후. activity 의 목록도 갱신되어야..
                    deleteLiveData.postValue(datas[position])
                })
                setNegativeButton("CANCEL", null)
                show()
            }
        }

    }
}



