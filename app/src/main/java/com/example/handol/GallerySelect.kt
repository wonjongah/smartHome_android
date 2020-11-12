package com.example.handol

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.media.ExifInterface
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
import java.io.IOException

class GallerySelect : AppCompatActivity(){

    lateinit var file_url : String

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        btn_gallery_cancel.setOnClickListener{
            onBackPressed()
        }

        btn_gallery_select.setOnClickListener {
            val filename = edt_gallery_name.text.toString()
            val send_socket = SocketFile(file_url, filename)
            send_socket.execute()
            toast("얼굴 인식을 정상 등록했습니다.")
            onBackPressed()
        }
    }


    override fun onStart() {
        super.onStart()

        val i = intent ?: Log.d("인텐트 값 없음", "없네...")

        val send_path : String = intent.getStringExtra("send_url").toString()
        val rec_path : String = intent.getStringExtra("url").toString()
        file_url = send_path
        val file = File(file_url)


//        iv_gallery.setImageURI(rec_path?.toUri())
        iv_gallery.setImageBitmap(rotateImageIfRequired(file.absolutePath))

    }

    private fun rotateImageIfRequired(imagePath: String): Bitmap? {
        var degrees = 0
        try {
            val exif = ExifInterface(imagePath)
            val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degrees = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degrees = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degrees = 270
            }
        } catch (e: IOException) {
            Log.e("ImageError", "Error in reading Exif data of $imagePath", e)
        }

        val decodeBounds: BitmapFactory.Options = BitmapFactory.Options()
        decodeBounds.inJustDecodeBounds = true
        var bitmap: Bitmap? = BitmapFactory.decodeFile(imagePath, decodeBounds)
        val numPixels: Int = decodeBounds.outWidth * decodeBounds.outHeight
        val maxPixels = 2048 * 1536 // requires 12 MB heap
        val options: BitmapFactory.Options = BitmapFactory.Options()
        options.inSampleSize = if (numPixels > maxPixels) 2 else 1
        bitmap = BitmapFactory.decodeFile(imagePath, options)
        if (bitmap == null) {
            return null
        }

        val matrix = Matrix()
        matrix.setRotate(degrees.toFloat())
        bitmap = Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.width,
                bitmap.height, matrix, true
        )
        return bitmap
    }
}



