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
        MainData(R.drawable.light_out2,"전 등", R.drawable.imagebtn_states, "(꺼짐)"),
        MainData(R.drawable.valve,"가 스", 0, "(30 ppm)"),
        MainData(R.drawable.window,"침실 창문", R.drawable.imagebtn_states, "(닫힘)"),
        MainData(R.drawable.washer3, "세 탁 기", 0, "(중지)"),
        MainData(R.drawable.fire,"화 재",0, "(30 °C)"),
        MainData(R.drawable.window2,"거실 창문", R.drawable.imagebtn_states, "(닫힘)"),
        MainData(R.drawable.faucet,"누 수", 0,""),
            MainData(R.drawable.doorlock, "도 어 락", R.drawable.imagebtn_states, "(잠김)"),
            MainData(R.drawable.sunny, "날 씨", 0, "(맑음)")
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


    fun setText(led:String, gas:String, innerwin:String, washer:String, fire:String, livingwin:String, leak:String, door:String, weather:String){
        items[0].state = led
        items[1].state = gas
        items[2].state = innerwin
        items[3].state = washer
        items[4].state = fire
        items[5].state = livingwin
        items[6].state = leak
        items[7].state = door
        items[8].state = weather
        rv_fragment_a?.adapter?.notifyDataSetChanged()

    }

    fun btn_setting_led(state: Boolean){
        if(state) {
            items[0].imagebtn = R.drawable.imagebtn_states
        }else{
            items[0].imagebtn = R.drawable.imagebtn_state_on
        }
        rv_fragment_a?.adapter?.notifyDataSetChanged()
    }
    fun btn_setting_innerwin(state: Boolean){
        if(state) {
            items[2].imagebtn = R.drawable.imagebtn_states
        }else{
            items[2].imagebtn = R.drawable.imagebtn_state_on
        }
        rv_fragment_a?.adapter?.notifyDataSetChanged()
    }
    fun btn_setting_livingwin(state: Boolean){
        if(state) {
            items[5].imagebtn = R.drawable.imagebtn_states
        }else{
            items[5].imagebtn = R.drawable.imagebtn_state_on
        }
        rv_fragment_a?.adapter?.notifyDataSetChanged()
    }
    fun btn_setting_door(state: Boolean){
        if(state) {
            items[7].imagebtn = R.drawable.imagebtn_states
        }else{
            items[7].imagebtn = R.drawable.imagebtn_state_on
        }
        rv_fragment_a?.adapter?.notifyDataSetChanged()
    }


    fun controlOn(state:Boolean): Boolean{
        if (state) {
            items[0].imagebtn = R.drawable.imagebtn_state_on
            items[2].imagebtn = R.drawable.imagebtn_state_on
            items[5].imagebtn = R.drawable.imagebtn_state_on
            items[7].imagebtn = R.drawable.imagebtn_state_on

            val myClient = MyClientTask("living_LED_ON")
            val myClient2 = MyClientTask("living_window_ON")
            val myClient3 = MyClientTask("inner_window_ON")
            val MyClient4 = MyClientTask("door_door_ON")
            myClient.execute()
            myClient2.execute()
            myClient3.execute()
            MyClient4.execute()
            items[0].state = "(꺼짐)"
            items[2].state = "(닫힘)"
            items[5].state = "(닫힘)"
            items[7].state = "(잠김)"

            // items를 여기서 갱신, 버튼 이미지를 바꾼다든가
        }else{

            items[0].imagebtn = R.drawable.imagebtn_states
            items[2].imagebtn = R.drawable.imagebtn_states
            items[5].imagebtn = R.drawable.imagebtn_states
            items[7].imagebtn = R.drawable.imagebtn_states

            val myClient = MyClientTask("living_LED_OFF")
            val myClient2 = MyClientTask("living_window_OFF")
            val myClient3 = MyClientTask("inner_window_OFF")
            val myClient4 = MyClientTask("door_door_OFF")
            myClient.execute()
            myClient2.execute()
            myClient3.execute()
            myClient4.execute()
            items[0].state = "(켜짐)"
            items[2].state = "(열림)"
            items[5].state = "(열림)"
            items[7].state = "(열림)"

        }
        rv_fragment_a?.adapter?.notifyDataSetChanged()
        return state
    }

    fun onWindowClick(){
        val nextIntent = Intent(context, WindowActivity::class.java)
        startActivity(nextIntent)
    }

    fun onWindowClick2(){
        val nextIntent = Intent(context, WindowActivity2::class.java)
        startActivity(nextIntent)
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

        rv_fragment_a.adapter = RecyclerAdapter(items, ::onLightClick, ::onGasClick, ::onFireClick, ::onLeakClick, ::onCctvClick, ::onWindowClick, ::onWindowClick2)
        rv_fragment_a.setHasFixedSize(true)
        rv_fragment_a.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_fragment_a.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))


        }



}
