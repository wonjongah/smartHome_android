package com.example.handol

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_light.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.Socket
import java.net.UnknownHostException
import kotlin.math.roundToInt

class LightActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_light)


            tv_light_bright.text = "${seekBar_light.progress}"

        var listener = SeekBarListener()
        seekBar_light.setOnSeekBarChangeListener(listener)

        seekBar_light.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                tv_light_bright.text = "$p1"
                if(p1 >= 0 && p1 <= 50){
                    iv_light.setImageResource(R.drawable.inner_light_off)
                }else{
                    iv_light.setImageResource(R.drawable.inner_light_on)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                val value: Int? = (p0?.progress)?.times(2.55)?.roundToInt()
                val MyClient = MyClientTask("living_LED_$value")
                MyClient.execute()
            }
        })

    }

    inner class SeekBarListener : SeekBar.OnSeekBarChangeListener{
        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            tv_light_bright.text = "$p1"
            if(p1 >= 0 && p1 <= 50){
                iv_light.setImageResource(R.drawable.inner_light_off)
            }else{
                iv_light.setImageResource(R.drawable.inner_light_on)
            }
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {
        }

        override fun onStopTrackingTouch(p0: SeekBar?) {
            val value: Int? = (p0?.progress)?.times(2.55)?.roundToInt()
            val MyClient = MyClientTask("living_LED_$value")
            MyClient.execute()
        }
    }

}