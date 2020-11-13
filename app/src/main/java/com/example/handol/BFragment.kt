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
import kotlinx.android.synthetic.main.fragment_b.*
import kotlinx.android.synthetic.main.fragment_b.view.*
import kotlinx.android.synthetic.main.fragment_c.*
import kotlinx.android.synthetic.main.fragment_c.view.*
import kotlinx.android.synthetic.main.rv_fragment_a.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.Socket
import java.net.UnknownHostException

private const val DATAB = "datab"

class BFragment : Fragment() {


    private var datab: String?=null


    var items: MutableList<MainData> = mutableListOf(
            MainData(R.drawable.doorlock, "도 어 락", R.drawable.imagebtn_states, "(잠김)"),
            MainData(R.drawable.window2, "거실 창문", R.drawable.imagebtn_states, "(닫힘)"),
            MainData(R.drawable.sunny, "날 씨", 0, "(맑음)")

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            datab = it.getString(DATAB)
        }


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.rv_fragment_a, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    fun onWindowClick2(){
        val nextIntent = Intent(context, WindowActivity2::class.java)
        startActivity(nextIntent)
    }

    fun onCctvClick(){
        val nextIntent = Intent(context, CctvActivity::class.java)
        startActivity(nextIntent)
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

    fun setText(door:String, livingwin:String, weather:String){
        items[0].state = door
        items[1].state = livingwin
        items[2].state = weather

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

    fun btn_setting_livingwin(state: Boolean){
        if(state) {
            items[5].imagebtn = R.drawable.imagebtn_states
        }else{
            items[5].imagebtn = R.drawable.imagebtn_state_on
        }
        rv_fragment_a?.adapter?.notifyDataSetChanged()
    }

    fun controlOn(state:Boolean): Boolean{


        if (state) {

            items[0].imagebtn = R.drawable.imagebtn_state_on
            items[1].imagebtn = R.drawable.imagebtn_state_on

            val myClient = MyClientTask("living_led_ON")
            val myClient2 = MyClientTask("living_window_ON")
            val myClient3 = MyClientTask("inner_window_ON")
            val MyClient4 = MyClientTask("door_door_0")
            myClient.execute()
            myClient2.execute()
            myClient3.execute()
            MyClient4.execute()
            items[0].state = "(잠김)"
            items[1].state = "(닫힘)"

        }else{

            items[0].imagebtn = R.drawable.imagebtn_states
            items[1].imagebtn = R.drawable.imagebtn_states

            val myClient = MyClientTask("living_led_OFF")
            val myClient2 = MyClientTask("living_window_OFF")
            val myClient3 = MyClientTask("inner_window_OFF")
            val myClient4 = MyClientTask("door_door_1")
            myClient.execute()
            myClient2.execute()
            myClient3.execute()
            myClient4.execute()
            items[0].state = "(열림)"
            items[1].state = "(열림)"

        }
        rv_fragment_a?.adapter?.notifyDataSetChanged()
        return state
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_fragment_a.adapter = RecyclerAdapterB(items, ::onCctvClick, ::onWindowClick2)
        rv_fragment_a.setHasFixedSize(true)
        rv_fragment_a.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_fragment_a.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
    }



}
