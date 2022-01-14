package cc.aoeiuv020

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationActivity : AppCompatActivity() {
    companion object {
        @Suppress("unused")
        fun start(ctx: Context) {
            ctx.startActivity(Intent(ctx, NotificationActivity::class.java))
        }
    }
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        val manager = NotificationManagerCompat.from(this)
        val intent = Intent(this, NotificationActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
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


        findViewById<View>(R.id.btnNotification).setOnClickListener {
            ++count
            nb.setContentText("notification $count")
            manager.notify(1, nb.build())
        }
    }
}
