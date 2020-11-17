package com.example.handol

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Message
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService

class NotificationHandler(private val ctx: Context) {

    private val notificationManager: NotificationManager by lazy {
        ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun sendWaterNotification(message: String, applicationContext:Context){
        val notificationIntent = Intent(applicationContext, LeakActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, 4, notificationIntent, 0)
        val bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.water_drop)
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder : NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.water_drop)
                .setContentTitle(message)
                .setContentText("수도가 계속 틀어져 있습니다")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setColor(Color.WHITE)


        notificationManager.notify(4, builder.build())
    }

    fun sendFireNotification(message: String, applicationContext:Context){
        val notificationIntent = Intent(applicationContext, FireActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, 3, notificationIntent, 0)
        val bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.fire)
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder : NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setLargeIcon(bitmap)
            .setSmallIcon(R.drawable.fire)
            .setContentTitle(message)
            .setContentText("화재가 발생했습니다")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setWhen(System.currentTimeMillis())
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setSound(defaultSound)
            .setColor(Color.WHITE)


        notificationManager.notify(3, builder.build())
    }

    fun sendGasNotification(message: String, applicationContext:Context){
        val notificationIntent = Intent(applicationContext, GasActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, 0, notificationIntent, 0)
        val bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.gas_leak)
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder : NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.gas_leak)
                .setContentTitle(message)
                .setContentText("가스가 누수되었습니다")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setColor(Color.WHITE)


        notificationManager.notify(0, builder.build())
    }

    fun sendMotionNotification(message: String, applicationContext:Context){
        val bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.vib)
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder : NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.vib)
                .setContentTitle(message)
                .setContentText("움직임이 감지되었습니다. 시간을 다시 설정하세요")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setColor(Color.WHITE)


        notificationManager.notify(0, builder.build())
    }



    fun sendWindowNotification(message: String, applicationContext:Context){
        val closeBtn = Intent(applicationContext, MainActivity::class.java)
        val notificationIntent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, 1, notificationIntent, 0)
        val broadcastIntent = Intent(applicationContext, Receiver::class.java)
        val actionIntent : PendingIntent = PendingIntent.getBroadcast(applicationContext, 1, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.window)
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder : NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.window)
                .setContentTitle(message)
                .setContentText("미세먼지 농도가 높습니다. 창문을 닫겠습니까?")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .addAction(0, "창문을 닫습니다", actionIntent)
               // .setStyle(NotificationCompat.BigTextStyle().bigText("창문을 닫기 위해서 알림창을 펼쳐주세요."))
                .setColor(Color.WHITE)



        notificationManager.notify(1, builder.build())
    }

    fun sendRainWindowNotification(message: String, applicationContext:Context){
        val closeBtn = Intent(applicationContext, MainActivity::class.java)
        val notificationIntent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, 7, notificationIntent, 0)
        val broadcastIntent = Intent(applicationContext, Receiver::class.java)
        val actionIntent : PendingIntent = PendingIntent.getBroadcast(applicationContext, 7, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.window)
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder : NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.window)
                .setContentTitle(message)
                .setContentText("비가 내립니다. 창문을 닫겠습니까?")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .addAction(0, "창문을 닫습니다", actionIntent)
                // .setStyle(NotificationCompat.BigTextStyle().bigText("창문을 닫기 위해서 알림창을 펼쳐주세요."))
                .setColor(Color.WHITE)



        notificationManager.notify(7, builder.build())
    }

    fun sendCctvNotification(message: String, applicationContext:Context){
        val notificationIntent = Intent(applicationContext, UnknownSnapshot::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, 2, notificationIntent, 0)
        val bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.cctv)
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder : NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.cctv)
                .setContentTitle(message)
                .setContentText("등록하지 않은 사람이 CCTV에 감지되었습니다.")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setColor(Color.WHITE)


        notificationManager.notify(2, builder.build())
    }

    companion object {
        private const val CHANNEL_ID = "com.example.handol"
        private const val NOTIFICATION_ID = 1001
    }

    fun CreateNotificationChannel(){

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showNotification(contentText: String, resultIntent: Intent) {
        val pendingIntent = PendingIntent.getActivity(ctx, 0, resultIntent, 0)

        val notification = Notification.Builder(ctx, CHANNEL_ID)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.gas_leak)
                .setChannelId(CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .build()

        notification.flags = Notification.FLAG_NO_CLEAR

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    fun dismissNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
    }
}