package com.example.handol

import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.activity_test.view.*
import okhttp3.Callback
import okhttp3.Response
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.jetbrains.anko.toast
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.telecom.Call
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_a.*
import kotlinx.android.synthetic.main.fragment_a.view.*
import kotlinx.android.synthetic.main.rv_fragment_a.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.Socket
import java.net.URI.create
import java.net.UnknownHostException


const val SUB_TOPIC = "iot_app"
const val SUB_TOPIC_INFO = "iot_app/info"
const val SUB_TOPIC_UNKNOWN = "iot_app/unknown"
const val SUB_TOPIC_EMERGENCY = "iot_app/emergency "
const val SERVER_URI = "tcp://192.168.0.103"



class MainActivity : AppCompatActivity() {
        private val notificationHandler: NotificationHandler by lazy{
            NotificationHandler(applicationContext)
        }

        companion object {
            var BaseUrl = "http://api.openweathermap.org/"
            var AppId = "69e1fd5353fda6a66022ce70e12f5dfe"
            var lat = "37.445293"
            var lon = "126.785823"
        }

        private val adapter by lazy { MainAdapter(supportFragmentManager) }
        val TAG = "MqttActivity"
        lateinit var mqttClient: Mqtt

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_test)

            vpMainAcitivty.adapter = MainActivity@ adapter

            tab_layout.setupWithViewPager(vpMainAcitivty)

            notificationHandler.sendWindowNotification("창문 알람", this)
            notificationHandler.sendCctvNotification("CCTV 알람", this)

            switch_outing1.setOnCheckedChangeListener { CompoundButton, onSwitch ->
                if (onSwitch) {
                    toast("switch on")
                    // 프레그먼트에 데이터 보내서 on 버튼들 세팅하기
                    // 아두이노에게 명령 보내기? -> 이거 프레그먼트에서 해야 함?
                    // getOnData()
                    // passDataCom()

                    // ViewPager의 현재 프레그먼트를 찾아야 함.

                    val state = false
                    val adapter = vpMainAcitivty.adapter as MainAdapter

                    adapter.currentFragment?.controlOn(false)
                    adapter.currentFragmentB?.controlOn(false)
                    adapter.currentFragmentC?.controlOn(false)
                    adapter.currentFragmentD?.controlOn(false)
                    adapter.currentFragmentE?.controlOn(false)
                    notificationHandler.sendGasNotification("가스 누수",this)


                } else {
                    toast("switch off")
                    // 프레그먼트에 데이터 보내서 off 버튼들 세팅하기
                    // 아두이노에게 명령 보내기? -> 이거 프레그먼트에서 해야 함?


                    val state = true
                    val fg = AFragment
                    val adapter = vpMainAcitivty.adapter as MainAdapter
                    adapter.currentFragment?.controlOn(true)
                    adapter.currentFragmentB?.controlOn(true)
                    adapter.currentFragmentC?.controlOn(true)
                    adapter.currentFragmentD?.controlOn(true)
                    adapter.currentFragmentE?.controlOn(true)

                    //fg.controlOn(state)
                }
            }
            mqttClient = Mqtt(this, SERVER_URI)

            try {
                // mqttClient.setCallback{topic, message ->}
                mqttClient.setCallback(::onReceived)
                mqttClient.connect(arrayOf<String>(SUB_TOPIC))
                mqttClient.connect(arrayOf<String>(SUB_TOPIC_INFO))
                mqttClient.connect(arrayOf<String>(SUB_TOPIC_EMERGENCY))
                
            } catch (e: Exception) {
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


        fun onReceived(topic: String, message: MqttMessage) {
            val msg = String(message.payload)
            val topic = topic
            //  println(msg + topic)
            Log.d(TAG, msg + topic)
            if(topic == SUB_TOPIC_INFO){
                jasonObjectsExample()
            }else if(topic == SUB_TOPIC_EMERGENCY){
                // json 작업해서 한 번 더 거른 다음 때에 따라서 noti 작업
                // gas면 notificationHandler.sendGasNotification("가스 누수",this)
                // cctv면
                // 미세먼지
            }else if(topic == SUB_TOPIC_UNKNOWN){

            }

            //jasonObjectsExample(msg)
            //tv_celsius.text = msg
        }

        fun jasonObjectsExample() {//msg:String
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


        fun changeFragment(f: Fragment, cleanStack: Boolean = false) {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.rv_fragment_a, f)
            ft.addToBackStack(null)
            ft.commit()
        }



    }


interface WeatherService{


    @GET("data/2.5/weather")
    fun getCurrentWeatherData(
            @Query("lat") lat: String,
            @Query("lon") lon: String,
            @Query("appid") appid: String) :
            retrofit2.Call<WeatherResponse>
}


