package com.example.handol

import android.graphics.ImageDecoder
import android.net.Uri
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
import java.io.File

class GallerySelect : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        btn_gallery_cancel.setOnClickListener{
            finishActivity(3)
        }
    }


    override fun onStart() {
        super.onStart()

        val i = intent ?: Log.d("인텐트 값 없음", "없네...")

        val rec_path = intent.getStringExtra("url")
        val file = File(rec_path)

        iv_gallery.setImageURI(rec_path?.toUri())

    }
}



