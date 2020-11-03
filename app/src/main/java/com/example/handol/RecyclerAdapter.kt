package com.example.handol

import android.content.ContentValues.TAG
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_a.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.Socket
import java.net.UnknownHostException
import java.util.*
import kotlin.concurrent.timer


class RecyclerAdapter(var items: MutableList<MainData>,
                      val onLightClick:()->Unit, val onGasClick:()->Unit,
                      val onFireClick:()->Unit, val onLeakClick:()->Unit, val onCctvClick:()->Unit) : RecyclerView.Adapter<RecyclerAdapter.MainViewHolder>() {
// var -> 멤버변수 선언, var 안 쓰면 생성자에서만 쓰이는 지역변수
    // 생성자의 매개변수 앞에 var, val 키워드가 붙으면 멤버변수로 운영하겠다란 뜻

    // 3번째 호출
    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val iconA = itemView.iv_a
        val tvfurStateA = itemView.tv_furState_a
        val imageBtnA = itemView.ib_a
        val tvrec = itemView.tv_rec_a
        // 카드뷰 자식으로 메인 컨텐트가 뭐냐를 참조로 넣음
        // 뷰홀더와 연관된 데이터를 나중에 설정할 것, 그곳이 저기다
    }

    inner class MyClientTask (var dstAddress: String, var dstPort: Int, message: String, private val tv_rec: View) : AsyncTask<Void?, Void?, Void?>() {

        var response = ""
        var myMessage = ""
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
                        socket.close()
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
            tv_rec.tv_rec_a.text = response
            super.onPostExecute(result)
        }

        //constructor
        init {
            myMessage = message
        }
    }



    // 2번째 호출
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        // ViewHolder 만들어내는 역할
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_a, parent, false)
        // 레이아웃아이디, parent, 인플레이트한 걸 페어런트에게 즉시 추가할 거냐(false는 아직은 넣지 마라)
        return MainViewHolder(view) // 메인뷰홀더 생성시키는데 매개변수 view(inflate 결과로 리턴되는 루트엘리먼트(카드뷰))
        // 메인뷰홀더 -> item_main 하나당 홀더 하나가 만들어진다
        // 홀더 안에 자신의 컴포넌트 정보를 연결할 수 있다
    }

    // 4번째 호출
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        // bindViewHolder -> 각각의 컴포넌트를 홀더에 넣는 역할
        items[position].let { // 실제데이터와 뷰 연결
            // items[position] -> item 선택
            // item말고 it으로 생량 가능
            // with -> apply와 유사, holder를 this로 삼겠다 (holder.apply)

            // holder.tvTitle.text = it.title과 밑의 코드는 같음

            with(holder) {
                iconA.setImageResource(it.icon)
                tvfurStateA.text = it.content
                imageBtnA.setImageResource(it.imagebtn)

//                timer(period = 100000, initialDelay = 1000) {
//                    val myClientTask = MyClientTask("192.168.0.103", 8888, "humi", itemView.tv_rec_a)
//                    myClientTask.execute()
//                }

                if (position == 0) {
                    itemView.setOnClickListener {
                        onLightClick()
                    }
                    imageBtnA.setOnClickListener {
                        if(imageBtnA.isSelected){
                            imageBtnA.setSelected(false)
                            val myClientTask = MyClientTask("192.168.0.103", 8888, "living_LED_ON", itemView.tv_rec_a)
                            // val myClientTast = MyClientTask("192.168.0.103", 8888, "on", itemView.tv_rec_a)
                            myClientTask.execute()
                        }
                        else{
                            imageBtnA.setSelected(true)
                            val myClientTask = MyClientTask("192.168.0.103", 8888, "living_LED_OFF", itemView.tv_rec_a)
                            myClientTask.execute()
                        }
                        //jasonObjectsExample()
                    }
                }

                else if (position == 1){
                    itemView.setOnClickListener {
                        onGasClick()
                    }
                    imageBtnA.setOnClickListener {
                        if(imageBtnA.isSelected){
                            imageBtnA.setSelected(false)
                        }
                        else{
                            imageBtnA.setSelected(true)
                        }
                    }
                }

                else if(position == 2){
                    imageBtnA.setOnClickListener {
                        if(imageBtnA.isSelected){
                            imageBtnA.setSelected(false)
                        }
                        else{
                            imageBtnA.setSelected(true)
                        }
                    }
                }

                else if (position == 3){
                    itemView.setOnClickListener {
                        onFireClick()
                    }
                    imageBtnA.setOnClickListener {
                        if(imageBtnA.isSelected){
                            imageBtnA.setSelected(false)
                        }
                        else{
                            imageBtnA.setSelected(true)
                        }
                    }
                }

                else if(position == 4){
                    imageBtnA.setOnClickListener {
                        if(imageBtnA.isSelected){
                            imageBtnA.setSelected(false)
                        }
                        else{
                            imageBtnA.setSelected(true)
                        }
                    }
                }

                else if (position == 5){
                    itemView.setOnClickListener {
                        onLeakClick()
                    }
                    imageBtnA.setOnClickListener {
                        if(imageBtnA.isSelected){
                            imageBtnA.setSelected(false)
                        }
                        else{
                            imageBtnA.setSelected(true)
                        }
                    }
                }

                else if (position == 6){
                    itemView.setOnClickListener {
                        onCctvClick()
                    }
                    imageBtnA.setOnClickListener {
                        if(imageBtnA.isSelected){
                            imageBtnA.setSelected(false)
                        }
                        else{
                            imageBtnA.setSelected(true)
                        }
                    }
                }

                else if(position == 7){
                    imageBtnA.setOnClickListener {
                        if(imageBtnA.isSelected){
                            imageBtnA.setSelected(false)
                        }
                        else{
                            imageBtnA.setSelected(true)
                        }
                    }
                }
            }


        }
    }


    // 1번째 호출
    override fun getItemCount(): Int = items.size
    // 데이터 몇 개 있는가

//    fun startTimer(){
//        var timerTask: Timer? = null
//        timerTask = timer(period = 10000){
//            val myClientTask = MyClientTask("192.168.0.103", 8888, "humi", itemView.tv_rec_a)
//            myClientTask.execute()
//            runOnUiThread{
//
//            }
//        }
//    }

    fun jasonObjectsExample(){
        val jasonString = """
            {
                "IoT3": {
                    {
                        "room" : "living",
                        "sensor" : "LED",
                        "order" : "ON"
                    },
                    {
                        "room" : "living",
                        "sensor" : "LED",
                        "order" : "OFF"
                    }
                }
            }
        """.trimIndent()

        val jObject = JSONObject(jasonString)
        val jArray = jObject.getJSONArray("IoT3")

        for (i in 0 until jArray.length()){
            val obj = jArray.getJSONObject(i)
            val room = obj.getString("room")
            val sensor = obj.getString("sensor")
            val order = obj.getString("order")
            Log.d(TAG, "room: $room")
            Log.d(TAG, "sensor: $sensor")
            Log.d(TAG, "order: $order")

        }


    }

}