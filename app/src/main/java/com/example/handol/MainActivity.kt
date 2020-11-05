package com.example.handol

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.activity_test.view.*
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.jetbrains.anko.toast
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.Socket
import java.net.UnknownHostException

const val SUB_TOPIC = "iot_app"
const val SERVER_URI = "tcp://192.168.0.103"

class MainActivity : AppCompatActivity() {

    private val adapter by lazy{MainAdapter(supportFragmentManager)}
    val TAG = "MqttActivity"
    val switchOn = "LED_ON"
    val swtichOff = "LED_OFF"
    lateinit var mqttClient: Mqtt
    var textValriable = "This to be read from the fragments"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        vpMainAcitivty.adapter = MainActivity@adapter

        tab_layout.setupWithViewPager(vpMainAcitivty)

        textValriable = "I can also change this text"

        val name = intent.getStringExtra("input_txt")
        if (name != null) {
            AFragment.newInstance(name)
        }

        switch_outing1.setOnCheckedChangeListener{CompoundButton, onSwitch->
            if(onSwitch){
                toast("switch on")
                // 프레그먼트에 데이터 보내서 on 버튼들 세팅하기
                // 아두이노에게 명령 보내기? -> 이거 프레그먼트에서 해야 함?
               // getOnData()
                // passDataCom()

                // ViewPager의 현재 프레그먼트를 찾아야 함.
                val state = false
                val adapter = vpMainAcitivty.adapter as MainAdapter
                adapter.currentFragment?.controlOn(true)


            }
            else{
                toast("switch off")
                // 프레그먼트에 데이터 보내서 off 버튼들 세팅하기
                // 아두이노에게 명령 보내기? -> 이거 프레그먼트에서 해야 함?
                val state = true
                val fg = AFragment
                //fg.controlOn(state)
            }
        }
        mqttClient = Mqtt(this, SERVER_URI)

        try{
            // mqttClient.setCallback{topic, message ->}
            mqttClient.setCallback(::onReceived)
            mqttClient.connect(arrayOf<String>(SUB_TOPIC))
        } catch(e:Exception){
            e.printStackTrace()
        }

//        timer(period = 10000, initialDelay = 100) {
//
//            val myClientTask = MyClientTask2("192.168.0.103", 8888, "temp", tv_celsius)
//            val myClientTask2 = MyClientTask3("192.168.0.103", 8888, "humi", tv_humi)
//            myClientTask.execute()
//            myClientTask2.execute()
//        }

    }

        fun passDataCom() {
        val bundle = Bundle(1)
        val myMessage = "Message received"
        bundle.putString("input_txt",myMessage)

        val transaction = this.supportFragmentManager.beginTransaction()
        val fraga = AFragment()
        fraga.arguments = bundle
//
//        transaction.replace(R.id.rv_fragment_a, fraga)
//        transaction.addToBackStack(null)
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//        transaction.commit()
    }

    fun getOnData(): String {
        return switchOn
    }

    fun getOffData(): String{
        return swtichOff
    }

    fun onReceived(topic:String, message:MqttMessage){
        val msg = String(message.payload)
        val topic = topic
       //  println(msg + topic)
        Log.d(TAG, msg + topic)

        jasonObjectsExample()
        //jasonObjectsExample(msg)
        //tv_celsius.text = msg
    }

    fun jasonObjectsExample(){//msg:String
//        val jasonString = msg.trimIndent()

        val jasonString = """
            {
                "living": {
                    "DHT" : {
                        "Temp": 24,
                        "Humi" :30
                    },
                    "LED" : {
                        "Brig" : 255,
                        "stat" : 1
                    }
                },
                "bathroom" : {
                    "tap" : {
                        "str" : 0,
                        "open" : 0
                    }
                }
                
            }
        """.trimIndent()


        val jObject = JSONObject(jasonString)
        val livingObject = jObject.getJSONObject("living")
        Log.d(TAG, livingObject.toString())
        val dhtObject = livingObject.getJSONObject("DHT")
        Log.d(TAG, dhtObject.toString())
        val temp = dhtObject.getString("Temp")
        Log.d(TAG, "temp : $temp")
        val humi = dhtObject.getString("Humi")
        Log.d(TAG, "humi : $humi")

        tv_celsius.text = temp
        tv_humi.text = humi
//        val jArray = jObject.getJSONArray("IoT3")
//
//        for (i in 0 until jArray.length()){
//            val obj = jArray.getJSONObject(i)
//            val room = obj.getString("room")
//            val sensor = obj.getString("sensor")
//            val order = obj.getString("order")
//            Log.d(ContentValues.TAG, "room: $room")
//            Log.d(ContentValues.TAG, "sensor: $sensor")
//            Log.d(ContentValues.TAG, "order: $order")
//
//        }


    }

    fun changeFragment(f: Fragment, cleanStack: Boolean = false){
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.rv_fragment_a, f)
        ft.addToBackStack(null)
        ft.commit()
    }


    class MyClientTask2 (var dstAddress: String, var dstPort: Int, message: String, private val tv_rec: View) : AsyncTask<Void?, Void?, Void?>() {

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
                response = "$response"
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
            tv_rec.tv_celsius.text = response + " °C"
            super.onPostExecute(result)
        }

        //constructor
        init {
            myMessage = message
        }
    }
    class MyClientTask3 (var dstAddress: String, var dstPort: Int, message: String, private val tv_rec: View) : AsyncTask<Void?, Void?, Void?>() {

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
                response = "$response"
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
            tv_rec.tv_humi.text = response
            super.onPostExecute(result)
        }

        //constructor
        init {
            myMessage = message
        }
    }
}