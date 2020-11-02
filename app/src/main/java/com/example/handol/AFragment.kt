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
import org.jetbrains.anko.startActivity

private const val DATA = "data"

class AFragment : Fragment() {


    private var data: String?=null


    var items: MutableList<MainData> = mutableListOf(
        MainData(R.drawable.light_out2,"전 등", R.drawable.imagebtn_states),
        MainData(R.drawable.valve,"가 스", R.drawable.imagebtn_states),
        MainData(R.drawable.window,"창 문", R.drawable.imagebtn_states),
        MainData(R.drawable.fire,"화 재", R.drawable.imagebtn_states),
        MainData(R.drawable.washer3,"세 탁 기", R.drawable.imagebtn_states),
        MainData(R.drawable.faucet,"누 수", R.drawable.imagebtn_states),
            MainData(R.drawable.doorlock, "도 어 락", R.drawable.imagebtn_states),
            MainData(R.drawable.sunny, "날 씨", R.drawable.imagebtn_states)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            data = it.getString(DATA)
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
                    putString(DATA, data)
                }
            }
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
