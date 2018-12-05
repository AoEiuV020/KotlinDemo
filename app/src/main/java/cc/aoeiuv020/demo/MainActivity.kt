package cc.aoeiuv020.demo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.intentFor

class MainActivity : AppCompatActivity() {
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val manager = NotificationManagerCompat.from(this)
        val intent = intentFor<MainActivity>()
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val nb = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("default", "default", NotificationManager.IMPORTANCE_DEFAULT)
            // 关闭响铃，需要清除数据重建这个渠道才会生效，
            channel.setSound(null, null)
            val nManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.createNotificationChannel(channel)
            NotificationCompat.Builder(this, channel.id)
        } else {
            @Suppress("DEPRECATION")
            NotificationCompat.Builder(this)
        }
        nb.setContentTitle(title)
                .setContentText("notification $count")
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher_round)
        manager.notify(1, nb.build())


        btnNotification.setOnClickListener {
            ++count
            nb.setContentText("notification $count")
            manager.notify(1, nb.build())
        }
    }
}
