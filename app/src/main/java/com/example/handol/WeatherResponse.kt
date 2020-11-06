package com.example.handol

import com.google.gson.annotations.SerializedName

class WeatherResponse(){
    @SerializedName("weather") var weather = ArrayList<WeatherC>()
    @SerializedName("main") var main: Main? = null
    @SerializedName("wind") var wind : Wind? = null
    @SerializedName("sys") var sys: Sys? = null
}