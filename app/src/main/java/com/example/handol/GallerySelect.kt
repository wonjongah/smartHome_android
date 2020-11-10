package com.example.handol

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.AsyncTask.execute
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.android.synthetic.main.activity_result_select.*
import org.jetbrains.anko.toast
import java.io.File

class GallerySelect : AppCompatActivity(){

    lateinit var file_url : String

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        btn_gallery_cancel.setOnClickListener{
            onBackPressed()
        }

        btn_gallery_select.setOnClickListener {
            val send_socket = SocketFile(file_url)
            send_socket.execute()
            toast("얼굴 인식을 정상 등록했습니다.")
            //onBackPressed()
        }
    }


    override fun onStart() {
        super.onStart()

        val i = intent ?: Log.d("인텐트 값 없음", "없네...")

        val send_path : String = intent.getStringExtra("send_url").toString()
        val rec_path : String = intent.getStringExtra("url").toString()
        file_url = send_path
        val file = File(rec_path)


        iv_gallery.setImageURI(rec_path?.toUri())

    }
}



