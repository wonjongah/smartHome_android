package com.example.handol

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_test.view.*
import kotlinx.android.synthetic.main.fragment_a.*
import kotlinx.android.synthetic.main.fragment_a.view.*
import kotlinx.android.synthetic.main.rv_fragment_a.*
import org.jetbrains.anko.startActivity
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.Socket
import java.net.UnknownHostException
import kotlinx.android.synthetic.main.fragment_a.view.tv_rec_a as tv_rec_a1

private const val DATA = "data"
private const val STATE = false
class AFragment : Fragment() {


    lateinit var ACTIVITY:MainActivity
    private var data: String?=null
    val ARG_NAME = "name"

    var items: MutableList<MainData> = mutableListOf(
        MainData(R.drawable.light_out2,"전 등", R.drawable.imagebtn_states),
        MainData(R.drawable.valve,"가 스", R.drawable.imagebtn_states),
        MainData(R.drawable.window,"침실 창문", R.drawable.imagebtn_states),
        MainData(R.drawable.fire,"화 재", R.drawable.imagebtn_states),
        MainData(R.drawable.window2,"거실 창문", R.drawable.imagebtn_states),
        MainData(R.drawable.faucet,"누 수", R.drawable.imagebtn_states),
            MainData(R.drawable.doorlock, "도 어 락", R.drawable.imagebtn_states),
            MainData(R.drawable.sunny, "날 씨", R.drawable.imagebtn_states)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            data = it.getString("input_txt")
        }


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.rv_fragment_a, container, false)

        val name = arguments?.getString(ARG_NAME)
        name?.let { Log.d("hahahahaha", it) }


        return rootView
    }

    //    fun controlA(){
//        // 제어 코드
//        // on로 세팅...
//        tv_rec_a.text = "on"
//    }
//
//    fun controlB(){
//        // 제어 코드
//    }

    companion object {

        @JvmStatic
        fun newInstance(name:String) =
            AFragment().apply {
                arguments = Bundle().apply {
                    putString("input_txt", "huhu")
                }
                AFragment().arguments

            }
    }

    fun controlOn(state:Boolean): Boolean{


        if (state) {
            for (i in 0..items.size - 1) {
                Log.d("D", items[i].imagebtn.toString())
                items[i].imagebtn = R.drawable.off_64_3
                val myClient = MyClientTask("living_LED_ON")
                val myClient2 = MyClientTask("living_WINDOW_ON")
                val myClient3 = MyClientTask("inner_WINDOW_ON")
                myClient.execute()
                myClient2.execute()
                myClient3.execute()
            }
            // items를 여기서 갱신, 버튼 이미지를 바꾼다든가
        }else{
            for (i in 0..items.size - 1) {
                Log.d("D", items[i].imagebtn.toString())
                items[i].imagebtn = R.drawable.on_64_3
                val myClient = MyClientTask("living_LED_OFF")
                val myClient2 = MyClientTask("living_WINDOW_OFF")
                val myClient3 = MyClientTask("inner_WINDOW_OFF")
                myClient.execute()
                myClient2.execute()
                myClient3.execute()
            }
        }
        rv_fragment_a?.adapter?.notifyDataSetChanged()
        return state
    }

    fun onLightClick(){
        val nextIntext = Intent(context, LightActivity::class.java)
        startActivity(nextIntext)
    }

    fun onGasClick(){
        val nextIntext = Intent(context, GasActivity::class.java)
        startActivity(nextIntext)
    }

    fun onFireClick(){
        val nextIntext = Intent(context, FireActivity::class.java)
        startActivity(nextIntext)
    }

    fun onLeakClick(){
        val nextIntent = Intent(context, LeakActivity::class.java)
        startActivity(nextIntent)
    }

    fun onCctvClick(){
        val nextIntent = Intent(context, CctvActivity::class.java)
        startActivity(nextIntent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_fragment_a.adapter = RecyclerAdapter(items, ::onLightClick, ::onGasClick, ::onFireClick, ::onLeakClick, ::onCctvClick)
        rv_fragment_a.setHasFixedSize(true)
        rv_fragment_a.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_fragment_a.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))


        }



}
