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
const val SUB_TOPIC_UNKNOWN = "iot_app/unknown"
const val SUB_TOPIC_EMERGENCY = "iot_app/emergency "
const val SERVER_URI = "tcp://192.168.0.138"



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
        lateinit var mqttClient2: Mqtt
        lateinit var mqttClient3: Mqtt

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_test)

            vpMainAcitivty.adapter = MainActivity@ adapter

            tab_layout.setupWithViewPager(vpMainAcitivty)

//            notificationHandler.sendWindowNotification("창문 알람", this)
//            notificationHandler.sendFireNotification("화재 발생", this)
//            notificationHandler.sendCctvNotification("낯선 인물 감지", this)

            iv_weather.setImageResource(R.drawable.sunny)

            switch_outing1.setOnCheckedChangeListener { CompoundButton, onSwitch ->
                if (onSwitch) {
                    toast("외출 모드 on")
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
//                    notificationHandler.sendGasNotification("가스 누수",this)


                } else {
                    toast("외출 모드 off")
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
            mqttClient2 = Mqtt(this, SERVER_URI)
            mqttClient3 = Mqtt(this, SERVER_URI)

            try {
                // mqttClient.setCallback{topic, message ->}
                mqttClient.setCallback(::onReceived)
                mqttClient.connect(arrayOf<String>(SUB_TOPIC))
                // 수정할 것
                mqttClient2.setCallback(::onReceivedUnknown)
                mqttClient2.connect(arrayOf<String>(SUB_TOPIC_UNKNOWN))

                mqttClient3.setCallback(::onReceivedEmergency)
                mqttClient3.connect(arrayOf<String>(SUB_TOPIC_EMERGENCY))
                
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
            Log.d("함수 호출", "ok")

            //if(topic == SUB_TOPIC){
                jasonObjectsExample2(msg)
           // }else if(topic == SUB_TOPIC_EMERGENCY){
                // 1. toilet/waterSensor/[floor/stop] -> split해서 floor면 알림 계속 보내기, stop오면 stop
                // 2. living/DHT/Humi/[HIGH or LOW]
                // 3. kitchen/gas -> 가스 농도 if문 돌려서 일정 수준 넘으면 노티
                // 4. kitchen/fire -> 온도 일정 이상 올라가면 if문 돌려서 노티
           // }else if(topic == SUB_TOPIC_UNKNOWN){
                // 모르는 사람 노티
            //    notificationHandler.sendCctvNotification("등록하지 않은 사람 인식", this)
            //}
            Log.d("함수 끝", "ok")

        }
    fun onReceivedUnknown(topic: String, message: MqttMessage) {
        val msg = String(message.payload)
        val topic = topic
        //  println(msg + topic)
        Log.d(TAG, msg + topic)

        notificationHandler.sendCctvNotification("낯선 인물 감지", this)

    }
    fun onReceivedEmergency(topic: String, message: MqttMessage) {
        val msg = String(message.payload)
        val topic = topic
        //  println(msg + topic)
        Log.d(TAG, msg + topic)

        val msg_split = msg.split("/")

        if(msg_split[2] == "floor"){
            notificationHandler.sendWaterNotification("수도 알림", this)
        }else if(msg_split[1] == "gas"){

                notificationHandler.sendGasNotification("가스 알림", this)

        }else if(msg_split[1] == "fire"){

                notificationHandler.sendFireNotification("화재 알림",this)

        }

        // 1. toilet/waterSensor/[floor/stop] -> split해서 floor면 알림 계속 보내기, stop오면 stop
        // 2. living/DHT/Humi/[HIGH or LOW]
        // 3. kitchen/gas -> 가스 농도 if문 돌려서 일정 수준 넘으면 노티
        // 4. kitchen/fire -> 온도 일정 이상 올라가면 if문 돌려서 노티
    }

        fun jasonObjectsExample() {//msg:String
//        val jasonString = msg.trimIndent()

            val jasonString = """
            {
                "living": {
                    "dht" : {
                        "te": 28,
                        "hu" :40
                    },
                    "dust" : {
                        "dd" : 30,
                        "dl" : 3
                    },
                    "window" : {
                        "ws" : 0
                    }
                },
                "inner" : {
                    "rain" : {
                        "rs" : 1
                    },
                    "window" : {
                        "ws" : 1
                    },
                    "led" : {
                        "led" : 1
                    }
                },
                "toilet" : {
                    "water" : {
                        "wat_s" : 0  
                    },
                    "pir_s" : {
                        "pir_s" : 1
                    },
                    "vib_s" : {
                        "vib_s" : 0
                    }
                },
                "kitchen" : {
                    "gas" : {
                        "gas" : 30
                    },
                    "fire" : {
                        "fire" : 50
                    }
                },
                "door" : {
                    "door" : 1
                }
            }
        """.trimIndent()


            val jObject = JSONObject(jasonString)
            val livingObject = jObject.getJSONObject("living")
            Log.d(TAG, livingObject.toString())
            val dhtObject = livingObject.getJSONObject("dht")
            Log.d(TAG, dhtObject.toString())
            val temp = dhtObject.getInt("te")
            Log.d(TAG, "temp : $temp")
            val humi = dhtObject.getInt("hu")
            Log.d(TAG, "humi : $humi")
            val dustObject = livingObject.getJSONObject("dust")
            Log.d(TAG, dustObject.toString())
            val dd = dustObject.getInt("dd")
            Log.d(TAG, "dd : $dd")
            val dl = dustObject.getString("dl")
            Log.d(TAG, "dl : $dl")
            val livingwinObject = livingObject.getJSONObject("window")
            Log.d(TAG, livingwinObject.toString())
            val livingwinstate = livingwinObject.getInt("ws")
            Log.d(TAG, "living win state : $livingwinstate")
            val innerObject = jObject.getJSONObject("inner")
            Log.d(TAG, innerObject.toString())
            val rsObject = innerObject.getJSONObject("rain")
            Log.d(TAG, rsObject.toString())
            val rs = rsObject.getString("rs")
            Log.d(TAG, "rs : $rs")
            val innerwinObject = innerObject.getJSONObject("window")
            Log.d(TAG, innerwinObject.toString())
            val innerwin = innerwinObject.getInt("ws")
            Log.d(TAG, "inner win state : $innerwin")
            val ledObject = innerObject.getJSONObject("led")
            Log.d(TAG, ledObject.toString())
            val led = ledObject.getInt("led")
            Log.d(TAG, "led : $led")
            val toiletObject = jObject.getJSONObject("toilet")
            Log.d(TAG, toiletObject.toString())
            val wat_sObejct = toiletObject.getJSONObject("water")
            Log.d(TAG, wat_sObejct.toString())
            val wat_s = wat_sObejct.getInt("wat_s")
            Log.d(TAG, "wat_s : $wat_s")
            val pir_sObject = toiletObject.getJSONObject("pir_s")
            Log.d(TAG, pir_sObject.toString())
            val pir_s = pir_sObject.getString("pir_s")
            Log.d(TAG, "pir_s : $pir_s")
            val vib_sObject = toiletObject.getJSONObject("vib_s")
            Log.d(TAG, vib_sObject.toString())
            val vib_s = vib_sObject.getInt("vib_s")
            Log.d(TAG, "vib_s : $vib_s")
            val kitchenObject = jObject.getJSONObject("kitchen")
            Log.d(TAG, kitchenObject.toString())
            val gasObject = kitchenObject.getJSONObject("gas")
            Log.d(TAG, gasObject.toString())
            val gas = gasObject.getString("gas")
            Log.d(TAG, "gas : $gas")
            val fireObject = kitchenObject.getJSONObject("fire")
            Log.d(TAG, fireObject.toString())
            val fire = fireObject.getString("fire")
            Log.d(TAG, "fire : $fire")
            val doorObject = jObject.getJSONObject("door")
            Log.d(TAG, doorObject.toString())
            val door = doorObject.getInt("door")
            Log.d(TAG, "door : $door")


            var dust_density = "측정중"
            if(dd > 0 && dd <= 30){
                dust_density = "좋음"
            }
            else if(dd > 30 && dd <= 80){
                dust_density = "보통"
            }
            else if(dd > 80 && dd <= 150){
                dust_density = "나쁨"
            }
            else{
                dust_density = "매우 나쁨"
            }
            tv_finedust.text = "$dd ㎍/㎥ $dust_density"

            var weather = ""
            if((temp > 17 && temp <= 28) || (humi > 10 && humi <=50)){
                weather = "맑음"
            } else {
                weather = "흐림"
            }

            var led_state = ""
            if(led == 1){
                led_state = "(켜짐)"
            } else{
                led_state = "(꺼짐)"
            }

            var inner_win_state = ""
            if(innerwin == 1){
                inner_win_state = "(열림)"
            } else {
                inner_win_state = "(닫힘)"
            }

            var washer_state = ""
            if(vib_s == 1){
                washer_state = "(작동중)"
            }else{
                washer_state = "(중지)"
            }

            var living_win_state = ""
            if(livingwinstate == 1){
                living_win_state = "(열림)"
            }else{
                living_win_state = "(닫힘)"
            }

            var water_state = ""
            if(wat_s == 1){
                water_state = "(수도꼭지 열림)"
            }else{
                water_state = "(수도꼭지 닫힘)"
            }

            var door_state = ""
            if(door == 1){
                door_state = "(열림)"
            }else{
                door_state = "(닫힘)"
            }
            val adapter = vpMainAcitivty.adapter as MainAdapter
            adapter.currentFragment?.setText(led_state, "($gas ppm)", inner_win_state, washer_state, "($fire °C)", living_win_state, water_state, door_state, weather)
            adapter.currentFragmentB?.setText(door_state, living_win_state, weather)
            adapter.currentFragmentC?.setText(led_state, inner_win_state)
            adapter.currentFragmentE?.setText(washer_state, water_state)
            adapter.currentFragmentD?.setText("($fire °C)", "($gas ppm)")

            if(led == 1){
                adapter.currentFragment?.btn_setting_led(true)
                adapter.currentFragmentC?.btn_setting_led(true)
            }else if(led == 0){
                adapter.currentFragment?.btn_setting_led(false)
                adapter.currentFragmentC?.btn_setting_led(false)
            }else if(innerwin == 1){
                adapter.currentFragment?.btn_setting_innerwin(true)
                adapter.currentFragmentC?.btn_setting_innerwin(true)
            }else if(innerwin == 0){
                adapter.currentFragment?.btn_setting_innerwin(false)
                adapter.currentFragmentC?.btn_setting_innerwin(false)
            }else if(livingwinstate == 1){
                adapter.currentFragment?.btn_setting_livingwin(true)
                adapter.currentFragmentB?.btn_setting_livingwin(true)
            }else if(livingwinstate == 0){
                adapter.currentFragment?.btn_setting_livingwin(false)
                adapter.currentFragmentB?.btn_setting_livingwin(false)
            }else if(door == 1){
                adapter.currentFragment?.btn_setting_door(true)
                adapter.currentFragmentB?.btn_setting_door(true)
            }else if(door == 0){
                adapter.currentFragment?.btn_setting_door(false)
                adapter.currentFragmentB?.btn_setting_door(false)
            }

            tv_celsius.text = "$temp °C"
            tv_finedust.text = "$dd ㎍/㎥ $dust_density"
            tv_humi.text = "$humi %"
        }

    fun jasonObjectsExample2(msg:String) {//msg:String
//        val jasonString = msg.trimIndent()

        val jasonString = msg.trimIndent()

        Log.d("json들2", jasonString)


        val jObject = JSONObject(jasonString)
        val livingObject = jObject.getJSONObject("living")
        Log.d(TAG, livingObject.toString())
        val dhtObject = livingObject.getJSONObject("dht")
        Log.d(TAG, dhtObject.toString())
        val temp = dhtObject.getInt("te")
        Log.d(TAG, "temp : $temp")
        val humi = dhtObject.getInt("hu")
        Log.d(TAG, "humi : $humi")


//        var dust_density = "측정중"
//        if(dd > 0 && dd <= 30){
//            dust_density = "좋 음"
//        }
//        else if(dd > 30 && dd <= 80){
//            dust_density = "보 통"
//        }
//        else if(dd > 80 && dd <= 150){
//            dust_density = "나 쁨"
//        }
//        else{
//            dust_density = "매우 나쁨"
//        }
//
//        var weather = ""
//        if((temp > 17 && temp <= 28) || (humi > 10 && humi <=50)){
//            weather = "맑음"
//        } else {
//            weather = "흐림"
//        }
//
//        var led_state = ""
//        if(led == 1){
//            led_state = "(켜짐)"
//        } else{
//            led_state = "(꺼짐)"
//        }
//
//        var inner_win_state = ""
//        if(innerwin == 1){
//            inner_win_state = "(열림)"
//        } else {
//            inner_win_state = "(닫힘)"
//        }
//
//        var washer_state = ""
//        if(vib_s == 1){
//            washer_state = "(작동중)"
//        }else{
//            washer_state = "(중지)"
//        }
//
//        var living_win_state = ""
//        if(livingwinstate == 1){
//            living_win_state = "(열림)"
//        }else{
//            living_win_state = "(닫힘)"
//        }
//
//        var water_state = ""
//        if(wat_s == 1){
//            water_state = "(수도꼭지 열림)"
//        }else{
//            water_state = "(수도꼭지 닫힘)"
//        }
//
//        var door_state = ""
//        if(door == 1){
//            door_state = "(열림)"
//        }else{
//            door_state = "(닫힘)"
//        }

        Log.d("텍스트뷰 전!!!!!!!!!!", "ok")
        tv_celsius.text = "$temp °C"
       // tv_finedust.text = "$dd ㎍/㎥"
        tv_humi.text = "$humi %"
//
//        val adapter = vpMainAcitivty.adapter as MainAdapter
//        adapter.currentFragment?.setText(led_state, "($gas ppm)", inner_win_state, washer_state, "($fire °C)", living_win_state, water_state, door_state, weather)
//        adapter.currentFragmentB?.setText(door_state, living_win_state, weather)
//        adapter.currentFragmentC?.setText(led_state, inner_win_state)
//        adapter.currentFragmentE?.setText(washer_state, water_state)
//        adapter.currentFragmentD?.setText("($fire °C)", "($gas ppm)")
//
//        if(led == 1){
//            adapter.currentFragment?.btn_setting_led(true)
//            adapter.currentFragmentC?.btn_setting_led(true)
//        }else if(led == 0){
//            adapter.currentFragment?.btn_setting_led(false)
//            adapter.currentFragmentC?.btn_setting_led(false)
//        }else if(innerwin == 1){
//            adapter.currentFragment?.btn_setting_innerwin(true)
//            adapter.currentFragmentC?.btn_setting_innerwin(true)
//        }else if(innerwin == 0){
//            adapter.currentFragment?.btn_setting_innerwin(false)
//            adapter.currentFragmentC?.btn_setting_innerwin(false)
//        }else if(livingwinstate == 1){
//            adapter.currentFragment?.btn_setting_livingwin(true)
//            adapter.currentFragmentB?.btn_setting_livingwin(true)
//        }else if(livingwinstate == 0){
//            adapter.currentFragment?.btn_setting_livingwin(false)
//            adapter.currentFragmentB?.btn_setting_livingwin(false)
//        }else if(door == 1){
//            adapter.currentFragment?.btn_setting_door(true)
//            adapter.currentFragmentB?.btn_setting_door(true)
//        }else if(door == 0){
//            adapter.currentFragment?.btn_setting_door(false)
//            adapter.currentFragmentB?.btn_setting_door(false)
//        }
        Log.d("함수 끝!!!!!!!!!!", "ok")

    }

//        fun changeFragment(f: Fragment, cleanStack: Boolean = false) {
//            val ft = supportFragmentManager.beginTransaction()
//            ft.replace(R.id.rv_fragment_a, f)
//            ft.addToBackStack(null)
//            ft.commit()
//        }



    }


interface WeatherService{


    @GET("data/2.5/weather")
    fun getCurrentWeatherData(
            @Query("lat") lat: String,
            @Query("lon") lon: String,
            @Query("appid") appid: String) :
            retrofit2.Call<WeatherResponse>
}


