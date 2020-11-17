package com.example.handol

import android.os.AsyncTask
import android.util.Log
import java.io.*
import java.lang.Exception
import java.net.Socket
import java.nio.BufferOverflowException

//const val SERVER = "192.168.35.148"
//const val SERVER = "192.168.0.101"
const val SERVER = "172.20.10.8"
const val PORT = 8890

class SocketFile(val outfile : String, val filename : String)  : AsyncTask<Void?, Void?, Void?>() {
    val TAG = "CommThread"
    var socket: Socket? = null


    override fun doInBackground(vararg p0: Void?): Void? {
        try {
            Log.i(TAG, outfile)
            val f = File(outfile)

            val socket = Socket(SERVER, PORT)

            val br = BufferedInputStream(socket.getInputStream())
            val bo = BufferedOutputStream(socket.getOutputStream())

            // 데이터 크기 전송
            val size = f.length()
            Log.i(TAG, "name, size: $filename, $size")
            bo.write((filename + " " + size.toString()).toByteArray())
            bo.flush()

            // 준비 상태 수신
            val ba = ByteArray(10)
            val len = br.read(ba)

//            val dis = DataOutputStream(socket.getOutputStream())


            Log.i(TAG, "파일 전송 시작")
            f.inputStream().copyTo(bo)
            bo.flush()
            Log.i(TAG, "파일 전송 완료")

        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } finally {
            if (socket != null) {
                try {
                    socket!!.close()
                } catch (e: IOException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
            }
        }
        return null
    }
}