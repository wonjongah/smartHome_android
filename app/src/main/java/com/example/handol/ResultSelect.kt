package com.example.handol

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_result_select.*
import java.io.File

class ResultSelect : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_select)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            1 -> {
                if (requestCode == 2 && resultCode == Activity.RESULT_OK) {

                    // 카메라로부터 받은 데이터가 있을경우에만
                    val file = File(data.toString())
                    if (Build.VERSION.SDK_INT < 28) {
                        val bitmap = MediaStore.Images.Media
                            .getBitmap(contentResolver, Uri.fromFile(file))  //Deprecated
                        iv_result.setImageBitmap(bitmap)
                    } else {
                        val decode = ImageDecoder.createSource(
                            this.contentResolver,
                            Uri.fromFile(file)
                        )
                        val bitmap = ImageDecoder.decodeBitmap(decode)
                        iv_result.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }
}