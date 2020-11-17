package com.example.handol

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_a.view.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.Socket
import java.net.UnknownHostException

class Receiver : BroadcastReceiver(){

    override fun onReceive(p0: Context?, p1: Intent?) {
        val message = p1?.getStringExtra("toastMessage")
        Toast.makeText(p0, message, Toast.LENGTH_LONG).show()

        val myClient = MyClientTask("living_window_OFF")
        val myClient2 = MyClientTask("inner_window_OFF")
        myClient.execute()
        myClient2.execute()
    }
}