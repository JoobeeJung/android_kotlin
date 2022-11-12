package com.kbstar.test3_androidx

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.kbstar.test3_androidx.databinding.FragmentOneBinding
import com.kbstar.test3_androidx.databinding.ItemRecyclerviewBinding

// 항목의 뷰를 가지는 역할
class MyViewHolder(val binding: ItemRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)

// 항목 구성.. 역할..
class MyAdapter(val datas: MutableList<String>): RecyclerView.Adapter<MyViewHolder>(){
    //항목 갯수를 판단하기 위해서 호출
    override fun getItemCount(): Int {
        return datas.size
    }

    //항목을 구성하기 위한 viewHolder준비 위해 자동 호출..
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(
            parent.context
        ), parent, false))
    }

    //항목 하나당 한번씩 호출.. 항목구성 .. 데이터 출력.. 이벤트 등록..
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.itemData.text = datas[position]
    }
}

class MyDecoration(val context: Context): RecyclerView.ItemDecoration() {
    //항목이 출력된 후 .. 그 위에 추가 꾸미기 ..
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        //뷰의 크기 획득 ..
        val width = parent.width
        val height = parent.height
        //drawing 하귀 위한 이미지 사이즈 획득..
        val dr = ResourcesCompat.getDrawable(context.resources, R.drawable.kbo, null)
        val drWidth = dr?.intrinsicWidth
        val drHeight = dr?.intrinsicHeight

        //중앙에 이미지를 그리기 위한.. 좌표 계산..
        val left = width/2 - drWidth?.div(2) as Int //view의 중앙점
        val top = height/2 - drHeight?.div(2) as Int

        //이미지 drawing
        c.drawBitmap(
            BitmapFactory.decodeResource(context.resources, R.drawable.kbo),
            left.toFloat(),
            top.toFloat(),
            null
        )

    }

    override fun getItemOffsets(
        outRect: Rect, //항목이 출력되는 사각형 정보..
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        //항목의 index 값 획득 ..
        val index = parent.getChildAdapterPosition(view)+1 //계산 편의성을 위해 1부터 시작하려고..
        if(index % 3 == 0)
        {
            outRect.set(10,10,10,60)
        }else
        {
            outRect.set(10,10,10,0)
        }

        view.setBackgroundColor(Color.parseColor("#28ABFF"))
        ViewCompat.setElevation(view, 20.0f)
    }
}


class OneFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentOneBinding.inflate(inflater, container, false)

        //리사이클러뷰 데이터 준비..
        val datas = mutableListOf<String>()

        for(i in 1..10)
        {
            datas.add("item $i")
        }

        //리사이클러뷰에 구성요소 등록..
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = MyAdapter(datas)
        binding.recyclerView.addItemDecoration(MyDecoration(activity as Context))

        return binding.root
    }

}