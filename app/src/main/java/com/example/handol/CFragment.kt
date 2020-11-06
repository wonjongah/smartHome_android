package com.example.handol

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rv_fragment_a.*

private const val DATAC= "datac"

class CFragment : Fragment() {


    private var datac: String?=null


    var items: MutableList<MainData> = mutableListOf(
            MainData(R.drawable.light_out2,"전 등", R.drawable.imagebtn_states),
            MainData(R.drawable.window,"창 문", R.drawable.imagebtn_states)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            datac = it.getString(DATAC)
        }


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.rv_fragment_a, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }


    fun onLightClick(){
        val nextIntext = Intent(context, LightActivity::class.java)
        startActivity(nextIntext)
    }


    companion object {

        @JvmStatic
        fun newInstance(uri: String) =
                AFragment().apply {
                    arguments = Bundle().apply {
                        //putString(DATAB, datab)
                    }
                }
    }

    fun controlOn(state:Boolean): Boolean{


        if (state) {
            for (i in 0..items.size - 1) {
                Log.d("D", items[i].imagebtn.toString())
                items[i].imagebtn = R.drawable.on_64_3
                //items[i][2] = R.drawable.imagebtn_states
            }
            // items를 여기서 갱신, 버튼 이미지를 바꾼다든가
        }else{
            for (i in 0..items.size - 1) {
                Log.d("D", items[i].imagebtn.toString())
                items[i].imagebtn = R.drawable.off_64_3
                //items[i][2] = R.drawable.imagebtn_states
            }
        }
        rv_fragment_a?.adapter?.notifyDataSetChanged()
        return state
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_fragment_a.adapter = RecyclerAdapterC(items, ::onLightClick)
        rv_fragment_a.setHasFixedSize(true)
        rv_fragment_a.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_fragment_a.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
    }


}
