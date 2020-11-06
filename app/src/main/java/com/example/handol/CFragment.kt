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
            MainData(R.drawable.light_out2,"전 등", R.drawable.imagebtn_states),
            MainData(R.drawable.window,"침실 창문", R.drawable.imagebtn_states)
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

    fun controlOn(state:Boolean): Boolean{


        if (state) {
            for (i in 0..items.size - 1) {
                Log.d("D", items[i].imagebtn.toString())
                items[i].imagebtn = R.drawable.on_64_3
                val myClient = MyClientTask("living_LED_OFF", tv_rec_c)
                val myClient2 = MyClientTask("living_WINDOW_CLOSE", tv_rec_c)
                val myClient3 = MyClientTask("inner_WINDOW_CLOSE",tv_rec_c)
                myClient.execute()
                myClient2.execute()
                myClient3.execute()
            }
            // items를 여기서 갱신, 버튼 이미지를 바꾼다든가
        }else{
            for (i in 0..items.size - 1) {
                Log.d("D", items[i].imagebtn.toString())
                items[i].imagebtn = R.drawable.off_64_3
                val myClient = MyClientTask("living_LED_ON", tv_rec_c)
                val myClient2 = MyClientTask("living_WINDOW_ON", tv_rec_c)
                val myClient3 = MyClientTask("inner_WINDOW_ON", tv_rec_c)
                myClient.execute()
                myClient2.execute()
                myClient3.execute()
            }
        }
        rv_fragment_a?.adapter?.notifyDataSetChanged()
        return state
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_fragment_a.adapter = RecyclerAdapterC(items, ::onLightClick)
        rv_fragment_a.setHasFixedSize(true)
        rv_fragment_a.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_fragment_a.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
    }


    inner class MyClientTask (message: String, private val tv_rec: View) : AsyncTask<Void?, Void?, Void?>() {

        var response = ""
        var myMessage = ""
        // var dstAddress = "192.168.35.115"
        //var dstAddress = "192.168.0.103"
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
            tv_rec.tv_rec_c.text = response
            super.onPostExecute(result)
        }

        //constructor
        init {
            myMessage = message
        }
    }

}
