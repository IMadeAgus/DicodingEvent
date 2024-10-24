package com.example.dicodingevent.ui.setting

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.dicodingevent.MainActivity
import com.example.dicodingevent.R
import com.example.dicodingevent.data.EventsRepository
import com.example.dicodingevent.data.local.entity.EventsEntity
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.SyncHttpClient
import cz.msebera.android.httpclient.Header
import java.text.DecimalFormat

//class EventWorker(
//    context: Context,
//    workerParams: WorkerParameters,
//) : CoroutineWorker(context, workerParams) {
//    companion object {
//        // TAG digunakan untuk menandai log
//        private val TAG = EventWorker::class.java.simpleName
//
//        const val NOTIFICATION_ID = 1
//        const val CHANNEL_ID = "channel_01"
//        const val CHANNEL_NAME = "event channel"
//    }
//
//    private var resultStatus: Result? = null
//
//    override suspend fun doWork(): Result {
//       return getEventNotifications()
//    }
//
//    private fun getEventNotifications() {
////        return resultStatus as Result
//    }
//
//    private fun showNotification(title: String, description: String?) {
//        // Mendapatkan NotificationManager dari sistem
//        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        // Membangun notifikasi dengan judul dan pesan yang diberikan
//        val notification: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
//            .setSmallIcon(R.drawable.ic_notification)
//            .setContentTitle(title)
//            .setContentText(description)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setDefaults(NotificationCompat.DEFAULT_ALL)
//
//        // Jika versi Android adalah Oreo atau lebih baru, buat NotificationChannel
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
//            notification.setChannelId(CHANNEL_ID)
//            notificationManager.createNotificationChannel(channel)
//        }
//        // Menampilkan notifikasi dengan ID tertentu
//        notificationManager.notify(NOTIFICATION_ID, notification.build())
//    }
//}