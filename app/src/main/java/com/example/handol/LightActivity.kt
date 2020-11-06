package com.example.handol

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_light.*
import kotlinx.android.synthetic.main.fragment_a.view.*
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
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                val value: Int? = (p0?.progress)?.times(2.55)?.roundToInt()
                val MyClient = MyClientTask("living_LED_brig:"+value)
                MyClient.execute()
            }
        })

    }

    inner class SeekBarListener : SeekBar.OnSeekBarChangeListener{
        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            tv_light_bright.text = "$p1"
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {
        }

        override fun onStopTrackingTouch(p0: SeekBar?) {
            val value: Int? = (p0?.progress)?.times(2.55)?.roundToInt()
            val MyClient = MyClientTask("living_LED_brig:"+value)
            MyClient.execute()
        }
    }

    inner class MyClientTask (message: String) : AsyncTask<Void?, Void?, Void?>() {

        var response = ""
        var myMessage = ""
        // var dstAddress = "192.168.35.115"
//        var dstAddress = "192.168.0.103"
        var dstAddress = "192.168.35.148"


        var dstPort = 8888
        override fun doInBackground(vararg p0: Void?): Void? {
            var socket: Socket? = null
            myMessage = myMessage
            try {
                socket = Socket(dstAddress, dstPort)
                //송신
                val out = socket.getOutputStream()
                out.write(myMessage.toByteArray())

                //수신
                val byteArrayOutputStream = ByteArrayOutputStream(1024)
                val buffer = ByteArray(1024)
                var bytesRead: Int
                val inputStream = socket.getInputStream()
                /*
                 * notice:
                 * inputStream.read() will block if no data return
                 */while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead)
                    response += byteArrayOutputStream.toString("UTF-8")
                }
                response = "($response)"
            } catch (e: UnknownHostException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
                response = "UnknownHostException: " + e.toString()
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
                response = "IOException: $e"
            } finally {
                if (socket != null) {
                    try {
                        //socket.close()
                    } catch (e: IOException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                }
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
//            tv_rec.textView.text = result.toString()
            //tv_rec.tv_rec_a.text = response
            super.onPostExecute(result)
        }

        //constructor
        init {
            myMessage = message
        }
    }
}