package com.example.handol

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rv_fragment_a.*

private const val DATA = "data"

class AFragment : Fragment() {


    private var data: String?=null


    var items: MutableList<MainData> = mutableListOf(
        MainData(R.drawable.icon_led,"전등", R.drawable.icon_power),
        MainData(R.drawable.icon_led,"가스", R.drawable.icon_power),
        MainData(R.drawable.icon_led,"창문", R.drawable.icon_power),
        MainData(R.drawable.icon_led,"화재", R.drawable.icon_power),
        MainData(R.drawable.icon_led,"세탁기", R.drawable.icon_power),
        MainData(R.drawable.icon_led,"누수", R.drawable.icon_power)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_fragment_a.adapter = RecyclerAdapter(items)
        rv_fragment_a.setHasFixedSize(true)
        rv_fragment_a.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        }


}
