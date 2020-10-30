package com.example.handol

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_a.view.*

class RecyclerAdapter(var items: MutableList<MainData>) : RecyclerView.Adapter<RecyclerAdapter.MainViewHolder>() {
// var -> 멤버변수 선언, var 안 쓰면 생성자에서만 쓰이는 지역변수
    // 생성자의 매개변수 앞에 var, val 키워드가 붙으면 멤버변수로 운영하겠다란 뜻

    // 3번째 호출
    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val icon = itemView.iv_a
        val tvfurState = itemView.tv_furState_a
        val imageBtn = itemView.ib_a

        // 카드뷰 자식으로 메인 컨텐트가 뭐냐를 참조로 넣음
        // 뷰홀더와 연관된 데이터를 나중에 설정할 것, 그곳이 저기다
    }



    // 2번째 호출
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        // ViewHolder 만들어내는 역할
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_a, parent, false)
        // 레이아웃아이디, parent, 인플레이트한 걸 페어런트에게 즉시 추가할 거냐(false는 아직은 넣지 마라)
        return MainViewHolder(view) // 메인뷰홀더 생성시키는데 매개변수 view(inflate 결과로 리턴되는 루트엘리먼트(카드뷰))
        // 메인뷰홀더 -> item_main 하나당 홀더 하나가 만들어진다
        // 홀더 안에 자신의 컴포넌트 정보를 연결할 수 있다
    }

    // 4번째 호출
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        // bindViewHolder -> 각각의 컴포넌트를 홀더에 넣는 역할
        items[position].let { // 실제데이터와 뷰 연결
            // items[position] -> item 선택
            // item말고 it으로 생량 가능
            // with -> apply와 유사, holder를 this로 삼겠다 (holder.apply)

            // holder.tvTitle.text = it.title과 밑의 코드는 같음

            with(holder) {
                icon.setImageResource(it.icon)
                tvfurState.text = it.content
                imageBtn.setImageResource(it.imagebtn)
            } // 실제 데이터 넣는 작업, 그 연결작업을 뷰홀더가 해주는 것
        }
    }

    // 1번째 호출
    override fun getItemCount(): Int = items.size
    // 데이터 몇 개 있는가


}