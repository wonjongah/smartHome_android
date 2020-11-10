package com.example.handol

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_unknown_snapshot.*

class UnknownSnapshot : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unknown_snapshot)

        btn_call_report.setOnClickListener {
            var intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:112")
            if(intent.resolveActivity(packageManager)!=null){
                startActivity(intent)
            }
        }

        btn_sms_report.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW)
            intent.putExtra("sms_body","서울특별시 강남구 삼성동 SAC아트홀 6층 iot강의실에 수상한 사람이 계속 있습니다. 출동 부탁드립니다.")
            intent.setType("vnd.android-dir/mms-sms")
            startActivity(intent)
        }


    }
}