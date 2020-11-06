package com.example.handol

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_test.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class WeatherC : AppCompatActivity() {

    companion object {
        var BaseUrl = "http://api.openweathermap.org/"
        var AppId = "69e1fd5353fda6a66022ce70e12f5dfe"
        var lat = "37.445293"
        var lon = "126.785823"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        //Create Retrofit Builder
        val retrofit = Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create(WeatherService::class.java)
        val call = service.getCurrentWeatherData(lat, lon, AppId)
        call.enqueue(object : retrofit2.Callback<WeatherResponse> {
            override fun onFailure(call: retrofit2.Call<WeatherResponse>, t: Throwable) {
                Log.d("MainActivity", "result :" + t.message)
            }

            override fun onResponse(
                    call: retrofit2.Call<WeatherResponse>,
                    response: retrofit2.Response<WeatherResponse>
            ) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()
                    Log.d("MainActivity", "result: " + weatherResponse.toString())
                    var cTemp = weatherResponse!!.main!!.temp - 273.15  //켈빈을 섭씨로 변환
                    var minTemp = weatherResponse!!.main!!.temp_min - 273.15
                    var maxTemp = weatherResponse!!.main!!.temp_max - 273.15
                    val stringBuilder =
                            "지역: " + weatherResponse!!.sys!!.country + "\n" +
                                    "현재기온: " + cTemp + "\n" +
                                    "최저기온: " + minTemp + "\n" +
                                    "최고기온: " + maxTemp + "\n" +
                                    "풍속: " + weatherResponse!!.wind!!.speed + "\n" +
                                    "일출시간: " + weatherResponse!!.sys!!.sunrise + "\n" +
                                    "일몰시간: " + weatherResponse!!.sys!!.sunset + "\n"

                    tv_celsius.text = stringBuilder
                }
            }

        })


    }
}






class Weather {
    @SerializedName("id") var id: Int = 0
    @SerializedName("main") var main : String? = null
    @SerializedName("description") var description: String? = null
    @SerializedName("icon") var icon : String? = null
}

class Main {
    @SerializedName("temp")
    var temp: Float = 0.toFloat()
    @SerializedName("humidity")
    var humidity: Float = 0.toFloat()
    @SerializedName("pressure")
    var pressure: Float = 0.toFloat()
    @SerializedName("temp_min")
    var temp_min: Float = 0.toFloat()
    @SerializedName("temp_max")
    var temp_max: Float = 0.toFloat()

}

class Wind {
    @SerializedName("speed")
    var speed: Float = 0.toFloat()
    @SerializedName("deg")
    var deg: Float = 0.toFloat()
}

class Sys {
    @SerializedName("country")
    var country: String? = null
    @SerializedName("sunrise")
    var sunrise: Long = 0
    @SerializedName("sunset")
    var sunset: Long = 0
}
