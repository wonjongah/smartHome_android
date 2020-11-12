package com.example.handol

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_window.*
import kotlin.math.roundToInt

class WindowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_window)

        tv_window_angle.text = "${seekBar_window.progress}"

        var listener = SeekBarListner()
        seekBar_window.setOnSeekBarChangeListener(listener)
        seekBar_window.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                tv_window_angle.text = "$p1"
                if(p1 >= 0 && p1 <= 50){
                    iv_window.setImageResource(R.drawable.close_win_inner)
                }else{
                    iv_window.setImageResource(R.drawable.inner_win_inner)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                val value : Int? = (p0?.progress)?.times(2.55)?.roundToInt()
                val MyClient = MyClientTask("inner_window_$value")
                MyClient.execute()
            }
        })

    }

    inner class SeekBarListner : SeekBar.OnSeekBarChangeListener{
        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            tv_window_angle.text = "$p1"
            if(p1 >= 0 && p1 <= 50){
                iv_window.setImageResource(R.drawable.close_win_inner)
            }else{
                iv_window.setImageResource(R.drawable.inner_win_inner)
            }
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {
        }

        override fun onStopTrackingTouch(p0: SeekBar?) {
            val value: Int? = (p0?.progress)?.times(2.55)?.roundToInt()
            val MyClient = MyClientTask("inner_window_$value")
            MyClient.execute()
        }
    }
}