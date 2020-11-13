package com.example.handol

import java.io.*
import java.net.ServerSocket
import java.net.Socket


class SocketServer : Runnable {
    override fun run() {
        try {
            println("S: Connecting...")
            val serverSocket = ServerSocket(SocketServer.Companion.ServerPort)
            while (true) {
                val client: Socket = serverSocket.accept()
                println("S: Receiving...")
                try {
                    val `in` = BufferedReader(InputStreamReader(client.getInputStream()))
                    val str: String = `in`.readLine()
                    println("S: Received: '$str'")
                    val out = PrintWriter(BufferedWriter(OutputStreamWriter(client.getOutputStream())), true)
                    out.println("Server Received $str")
                } catch (e: Exception) {
                    println("S: Error")
                    e.printStackTrace()
                } finally {
                    client.close()
                    println("S: Done.")
                }
            }
        } catch (e: Exception) {
            println("S: Error")
            e.printStackTrace()
        }
    }

    companion object {
        const val ServerPort = 9999
        const val ServerIP = "localhost"

        @JvmStatic
        fun main(args: Array<String>) {
            val desktopServerThread = Thread(SocketServer())
            desktopServerThread.start()
        }
    }
}