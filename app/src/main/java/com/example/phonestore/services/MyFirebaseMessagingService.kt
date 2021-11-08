package com.example.phonestore.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.example.phonestore.R
import com.example.phonestore.view.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService: FirebaseMessagingService() {
    private var notificationBuilder: NotificationCompat.Builder? = null
    override fun onNewToken(token: String) {
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
//        val intent = Intent(this, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(
//            this, 0 /* Request code */, intent,
//            PendingIntent.FLAG_ONE_SHOT
//        )
        if(p0.notification?.body?.contains("phản hồi") == true){
            val channelId = Constant.CHANNEL_ID
            notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(p0.notification?.title)
                .setStyle(NotificationCompat.BigTextStyle().bigText(p0.notification?.body))
                .setContentText(p0.notification?.body)
                .setAutoCancel(true)
        }else{
            val first = p0.notification?.body?.indexOf("#")
            val second  = p0.notification?.body?.substring(first?.plus(1)!!)
            val third = second?.indexOf(" ")
            var idOrder = second?.substring(0, third!!)
            val channelId = Constant.CHANNEL_ID
            notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(p0.notification?.title)
                .setStyle(NotificationCompat.BigTextStyle().bigText(p0.notification?.body))
                .setContentText(p0.notification?.body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent(this, idOrder))
        }


        val name = getString(R.string.app_name)
        val channel = NotificationChannel(getString(R.string.default_notification_channel_id), name, NotificationManager.IMPORTANCE_HIGH)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(
            Constant.NOTIFICATION_ID /* ID of notification */,
            notificationBuilder?.build()
        )
    }

    private fun sendRegistrationToServer(token: String?) {
        Log.d("NOTIFICATION", "sendRegistrationTokenToServer($token)")
    }
    private fun pendingIntent(context: Context, idOrder: String?): PendingIntent {
        return NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.fragmentOrder)
            .setArguments(bundleOf("idOrder" to idOrder?.toInt(), "key" to true, "FROMNOTIFICATION" to true))
            .createPendingIntent()
    }


}