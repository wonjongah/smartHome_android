package com.example.handol

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_window2.*
import kotlin.math.roundToInt

class WindowActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_window2)

        tv_window_angle2.text = "${seekBar_window2.progress}"

        var listener = SeekBarListner()
        seekBar_window2.setOnSeekBarChangeListener(listener)
        seekBar_window2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                tv_window_angle2.text = "$p1"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                val value: Int? = (p0?.progress)?.times(2.55)?.roundToInt()
                val MyClient = MyClientTask("living_window_$value")
                MyClient.execute()
            }
        })

    }

    inner class SeekBarListner : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            tv_window_angle2.text = "$p1"
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {
        }

        override fun onStopTrackingTouch(p0: SeekBar?) {
            val value: Int? = (p0?.progress)?.times(2.55)?.roundToInt()
            val MyClient = MyClientTask("living_window_$value")
            MyClient.execute()
        }
    }
}