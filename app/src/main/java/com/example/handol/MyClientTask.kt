package com.example.handol

import android.os.AsyncTask
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.Socket
import java.net.UnknownHostException

class MyClientTask (message: String) : AsyncTask<Void?, Void?, Void?>() {

    var response = ""
    var myMessage = ""
    // var dstAddress = "192.168.35.115"
//        var dstAddress = "192.168.0.103"
    var dstAddress = "192.168.35.148"


    var dstPort = 8888
    override fun doInBackground(vararg p0: Void?): Void? {
        var socket: Socket? = null
        myMessage = myMessage
        try {
            socket = Socket(dstAddress, dstPort)
            //송신
            val out = socket.getOutputStream()
            out.write(myMessage.toByteArray())

            //수신
            val byteArrayOutputStream = ByteArrayOutputStream(1024)
            val buffer = ByteArray(1024)
            var bytesRead: Int
            val inputStream = socket.getInputStream()
            /*
             * notice:
             * inputStream.read() will block if no data return
             */while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead)
                response += byteArrayOutputStream.toString("UTF-8")
            }
            response = "($response)"
        } catch (e: UnknownHostException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            response = "UnknownHostException: " + e.toString()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            response = "IOException: $e"
        } finally {
            if (socket != null) {
                try {
                    socket.close()
                } catch (e: IOException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
            }
        }
        return null
    }

    override fun onPostExecute(result: Void?) {
//            tv_rec.textView.text = result.toString()
        super.onPostExecute(result)
    }

    //constructor
    init {
        myMessage = message
    }
}
