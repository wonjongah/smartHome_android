package com.example.handol

import android.content.Intent
import android.os.AsyncTask
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
import kotlinx.android.synthetic.main.fragment_a.view.*
import kotlinx.android.synthetic.main.fragment_b.*
import kotlinx.android.synthetic.main.fragment_b.view.*
import kotlinx.android.synthetic.main.fragment_c.*
import kotlinx.android.synthetic.main.fragment_c.view.*
import kotlinx.android.synthetic.main.rv_fragment_a.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.Socket
import java.net.UnknownHostException

private const val DATAC= "datac"

class CFragment : Fragment() {


    private var datac: String?=null


    var items: MutableList<MainData> = mutableListOf(
            MainData(R.drawable.light_out2,"전 등", R.drawable.imagebtn_states, "(꺼짐)"),
            MainData(R.drawable.window,"침실 창문", R.drawable.imagebtn_states, "(닫힘)"), 
            MainData(R.drawable.rainy, "강 우", 0, "(강수 없음)")
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
    fun setText(led:String, innerwin:String, rain:String){
        items[0].state = led
        items[1].state = innerwin
        items[2].state = rain

        rv_fragment_a?.adapter?.notifyDataSetChanged()

    }

    fun onWindowClick(){
        val nextIntent = Intent(context, WindowActivity::class.java)
        startActivity(nextIntent)
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


    fun btn_setting_innerwin(state: Boolean){
        if(state) {
            items[1].imagebtn = R.drawable.imagebtn_states
        }else{
            items[1].imagebtn = R.drawable.imagebtn_state_on
        }
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

    fun controlOn(state:Boolean): Boolean{

        if (state) {
            for (i in 0..items.size - 1) {
                Log.d("D", items[i].imagebtn.toString())
                items[i].imagebtn = R.drawable.imagebtn_state_on
                val myClient = MyClientTask("living_led_ON")
                val myClient2 = MyClientTask("living_window_ON")
                val myClient3 = MyClientTask("inner_window_ON")
                val myClient4 = MyClientTask("door_door_0")
                myClient.execute()
                myClient2.execute()
                myClient3.execute()
                myClient4.execute()
            }
            // items를 여기서 갱신, 버튼 이미지를 바꾼다든가
            items[0].state = "(꺼짐)"
            items[1].state = "(닫힘)"
        }else{
            for (i in 0..items.size - 1) {
                Log.d("D", items[i].imagebtn.toString())
                items[i].imagebtn = R.drawable.imagebtn_states
                val myClient = MyClientTask("living_led_OFF")
                val myClient2 = MyClientTask("living_window_OFF")
                val myClient3 = MyClientTask("inner_window_OFF")
                val myClient4 = MyClientTask("door_door_1")
                myClient.execute()
                myClient2.execute()
                myClient3.execute()
                myClient4.execute()
                items[0].state = "(켜짐)"
                items[1].state = "(열림)"
            }
        }
        rv_fragment_a?.adapter?.notifyDataSetChanged()
        return state
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_fragment_a.adapter = RecyclerAdapterC(items, ::onLightClick, ::onWindowClick)
        rv_fragment_a.setHasFixedSize(true)
        rv_fragment_a.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_fragment_a.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
    }


}
