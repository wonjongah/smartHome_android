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

private const val DATAD= "datad"

class DFragment : Fragment() {


    private var datad: String?=null


    var items: MutableList<MainData> = mutableListOf(
            MainData(R.drawable.fire,"화 재",0, ""),
            MainData(R.drawable.valve,"가 스", 0, "")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            datad = it.getString(DATAD)
        }


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.rv_fragment_a, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


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

    fun setText(fire:String, gas:String){
        items[0].state = fire
        items[1].state = gas

        rv_fragment_a?.adapter?.notifyDataSetChanged()

    }



    fun controlOn(state:Boolean): Boolean{


        if (state) {

            val myClient = MyClientTask("living_LED_ON")
            val myClient2 = MyClientTask("living_window_ON")
            val myClient3 = MyClientTask("inner_window_ON")
            val myClient4 = MyClientTask("door_door_ON")
            myClient.execute()
            myClient2.execute()
            myClient3.execute()
            myClient4.execute()

        }else{

            val myClient = MyClientTask("living_LED_OFF")
            val myClient2 = MyClientTask("living_window_OFF")
            val myClient3 = MyClientTask("inner_window_OFF")
            val myClient4 = MyClientTask("door_door_OFF")
            myClient.execute()
            myClient2.execute()
            myClient3.execute()
            myClient4.execute()

        }
        rv_fragment_a?.adapter?.notifyDataSetChanged()
        return state
    }


    fun onGasClick(){
        val nextIntext = Intent(context, GasActivity::class.java)
        startActivity(nextIntext)
    }

    fun onFireClick(){
        val nextIntext = Intent(context, FireActivity::class.java)
        startActivity(nextIntext)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_fragment_a.adapter = RecyclerAdapterD(items, ::onFireClick, ::onGasClick)
        rv_fragment_a.setHasFixedSize(true)
        rv_fragment_a.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_fragment_a.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
    }


}
