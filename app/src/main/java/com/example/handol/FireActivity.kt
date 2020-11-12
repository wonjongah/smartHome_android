package com.example.handol

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_fire.*

class FireActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fire)

        btn_fire_call_report.setOnClickListener {
            var intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:119")
            startActivity(intent)
        }

        btn_fire_sms_report.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.putExtra("sms_body", "서울특별시 강남구 삼성동 SAC아트홀 6층 iot강의실에 불이 났습니다. 출동 부탁드립니다.")
            intent.setData(Uri.parse("smsto:"+119))
            startActivity(intent)
        }

    }
}