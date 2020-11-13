package com.example.handol

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_cctv.*
import kotlinx.android.synthetic.main.activity_unknown_snapshot.*

class UnknownSnapshot : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unknown_snapshot)

        wv_unknown.setWebViewClient(object : WebViewClient(){
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar_webView2.setVisibility(View.VISIBLE)

            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar_webView2.setVisibility(View.GONE)
            }
        }) // 클릭시 새창 안뜨게




        wv_unknown.setHorizontalScrollbarOverlay(false)
        wv_unknown.setVerticalScrollbarOverlay(false)
        wv_unknown.settings.let {
            it.setSupportZoom(false)
            it.displayZoomControls
            it.useWideViewPort
            it.javaScriptEnabled
            it.cacheMode
            it.domStorageEnabled
            it.layoutAlgorithm
            it.loadWithOverviewMode

        }


        wv_unknown.loadUrl("http://192.168.0.138:7072/video_fed"); // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작

        btn_call_report.setOnClickListener {
            var intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:112")
            if(intent.resolveActivity(packageManager)!=null){
                startActivity(intent)
            }
        }

        btn_sms_report.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.putExtra("sms_body", "서울특별시 강남구 삼성동 SAC아트홀 6층 iot강의실에 수상한 사람이 계속 있습니다. 출동 부탁드립니다.")
            intent.setData(Uri.parse("smsto:"+112))
            startActivity(intent)
        }


    }
}