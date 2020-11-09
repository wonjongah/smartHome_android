package com.example.handol

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_cctv.*
import org.jetbrains.anko.alert
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CctvActivity : AppCompatActivity() {

    lateinit var currentPhotoPath: String
    lateinit var path : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cctv)

        wv_stream.setWebViewClient(object : WebViewClient(){
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar_webView.setVisibility(View.VISIBLE)

            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar_webView.setVisibility(View.GONE)
            }
        }) // 클릭시 새창 안뜨게




        wv_stream.setHorizontalScrollbarOverlay(false)
        wv_stream.setVerticalScrollbarOverlay(false)
        wv_stream.settings.let {
            it.setSupportZoom(false)
            it.displayZoomControls
            it.useWideViewPort
            it.javaScriptEnabled
            it.cacheMode
            it.domStorageEnabled
            it.layoutAlgorithm
            it.loadWithOverviewMode

        }


        wv_stream.loadUrl("http://192.168.35.207:7072/video_feed"); // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작
        //wv_stream.loadUrl("https://www.naver.com")

        btn_add.setOnClickListener {
            alert("등록할 방법을 선택해주세요."){
                title = "얼굴인식 등록"
                negativeButton("갤러리"){
                    if(checkPersmission()){
                        openGalleryForImage()
                    }
                    else{
                        requestPermission()
                        openGalleryForImage()

                    }
                }
                neutralPressed("사진 찍기"){
                    if(checkPersmission()){
                        dispatchTakePictureIntent()
                    }
                    else{
                        requestPermission()
                    }
                }
            }.show()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            1 -> {
                if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

                    Log.d("haha", "haha")
                    val url = data?.getStringExtra("url")
                    if (url != null) {
                        Log.d("PIC@@@@@@", url)
                    }

                    val intent = Intent(this, ResultSelect::class.java)

                    intent.putExtra("url", currentPhotoPath)
                    galleryAddPic()
                    startActivityForResult(intent, 2)

                }
            }

            3->{
                if (resultCode == Activity.RESULT_OK && requestCode == 3){
                    val url = data?.data.toString()
                    Log.d("갤러리 url", url)

                    val intent = Intent(this, GallerySelect::class.java)

                    intent.putExtra("url", url)
                    startActivityForResult(intent, 3)
                }
            }
        }

    }
    // 카메라 열기
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                // 찍은 사진을 그림파일로 만들기
                val photoFile: File? =
                    try {
                        createImageFile()
                    } catch (ex: IOException) {
                        Log.d("TAG", "그림파일 만드는도중 에러생김")
                        null
                    }

                // 그림파일을 성공적으로 만들었다면 onActivityForResult로 보내기
                photoFile?.also {
                    if (Build.VERSION.SDK_INT < 24) {
                        if(photoFile != null){

                            val photoURI = Uri.fromFile(photoFile)

                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            Log.d("PIC1", photoURI.toString())
                            path = photoURI.toString()
                            takePictureIntent.putExtra("url", path)
                            startActivityForResult(takePictureIntent, 1)

                        }
                    }
                    else{
                        photoFile?.also {
                            val photoURI: Uri = FileProvider.getUriForFile(
                                this, "com.example.handol.fileprovider", it
                            )
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            startActivityForResult(takePictureIntent, 1)
                        }
                    }
                }
            }
        }
    }

//    fun testSavePic(){
//        val os = openFileOutput(currentPhotoPath, Context.MODE_PRIVATE)
//        os.write()
//    }

    private fun galleryAddPic() {
        Log.i("galleryAddPic", "Call");
        val mediaScanIntent: Intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        // 해당 경로에 있는 파일을 객체화(새로 파일을 만든다는 것으로 이해하면 안 됨)
        val f: File = File(currentPhotoPath);
        val contentUri: Uri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }

    // 카메라로 촬영한 이미지를 파일로 저장해준다
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    // 카메라 권한 요청
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE, CAMERA),
            1)
    }

    // 카메라 권한 체크
    private fun checkPersmission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    // 권한요청 결과
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("TAG", "Permission: " + permissions[0] + "was " + grantResults[0] + "카메라 허가 받음 예이^^")
        }else{
            Log.d("TAG","카메라 허가 못받음 ㅠ 젠장!!")
        }
    }


    private fun openGalleryForImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 3)
    }


}