package com.example.handol

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.core.view.size
import kotlinx.android.synthetic.main.activity_cctv.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast

class CctvActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cctv)

            wv_stream.setWebViewClient(WebViewClient()) // 클릭시 새창 안뜨게
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
            it.useWideViewPort

        }

        wv_stream.loadUrl("http://192.168.35.207:7072/video_feed"); // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작

        btn_add.setOnClickListener {
            alert("등록할 방법을 선택해주세요."){
                title = "안면인식 등록"
                negativeButton("갤러리"){
                    val intent = Intent(this.ctx, GallerySelect::class.java)
                    startActivity(intent)
                }
                neutralPressed("사진 찍기"){
                    val intent = Intent(this.ctx, PhotoSelect::class.java)
                    startActivity(intent)
                }
            }.show()
        }


    }
}