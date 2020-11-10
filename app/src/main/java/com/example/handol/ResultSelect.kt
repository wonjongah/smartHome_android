package com.example.handol

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_result_select.*
import java.io.File
import java.io.IOException

class ResultSelect : AppCompatActivity() {

    lateinit var file_url : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_select)

        val i = intent ?: Log.d("인텐트 값 없음", "없네...")

        val rec_path = intent.getStringExtra("url")
        file_url = rec_path.toString()
        val file = File(rec_path)



        btn_photo_cancel.setOnClickListener{
            onBackPressed()
        }

        btn_photo_select.setOnClickListener {
            val socket = SocketFile(file_url)
            socket.execute()
            onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()

        val i = intent ?: Log.d("인텐트 값 없음", "없네...")

        val rec_path = intent.getStringExtra("url")
        file_url = rec_path.toString()
        val file = File(rec_path)
        if (Build.VERSION.SDK_INT < 28) {
            val bitmap = MediaStore.Images.Media
                    .getBitmap(contentResolver, Uri.fromFile(file))  //Deprecated
            iv_result.setImageBitmap(rotateImageIfRequired(file.path))
        } else {
            val decode = ImageDecoder.createSource(
                    this.contentResolver,
                    Uri.fromFile(file)
            )
            val bitmap = ImageDecoder.decodeBitmap(decode)
            iv_result.setImageBitmap(rotateImageIfRequired(file.path))
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        when (requestCode) {
//            2 -> {
//                if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
//
//                    Log.d("REC!!!!", "받긴 한데 받은 게 널인듯")
//                    // 카메라로부터 받은 데이터가 있을경우에만
//                    val data2 = data?.getStringExtra("url")
//                    if (data2 != null) {
//                        Log.d("RESULTPIC", data2)
//                    }
//
//                    val file = File(data2)
//                    if (Build.VERSION.SDK_INT < 28) {
//                        val bitmap = MediaStore.Images.Media
//                            .getBitmap(contentResolver, Uri.fromFile(file))  //Deprecated
//                        iv_result.setImageBitmap(rotateImageIfRequired(file.path))
//                    } else {
//                        val decode = ImageDecoder.createSource(
//                            this.contentResolver,
//                            Uri.fromFile(file)
//                        )
//                        val bitmap = ImageDecoder.decodeBitmap(decode)
//                        iv_result.setImageBitmap(rotateImageIfRequired(file.path))
//                    }
//                }
//            }
//        }
//    }


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