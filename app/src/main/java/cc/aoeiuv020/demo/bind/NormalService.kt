package cc.aoeiuv020.demo.bind

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import org.jetbrains.anko.*

class NormalService : Service(), AnkoLogger {
    companion object : AnkoLogger {
        override val loggerTag: String = "NormalService"
        private const val KEY_COMMENT: String = "comment"
        fun start(ctx: Context, comment: String) {
            info { "start: $ctx, $comment" }
            ctx.startService<NormalService>(KEY_COMMENT to comment)
        }

        /**
         * 不是同一个ServiceConnection对象无法解绑，
         */
        private val conn = object : ServiceConnection {
            /**
             * 解绑时不会调用onServiceDisconnected，
             * 可能时绑定中服务断开时会调，
             */
            override fun onServiceDisconnected(name: ComponentName?) {
                info { "onServiceDisconnected: $name" }
            }

            /**
             * 绑定时会调用这个，
             */
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                info { "onServiceConnected: $name, $service" }
                info { "service: ${(service as? Binder)?.getService()}" }
            }
        }

        fun bind(ctx: Context, comment: String) {
            info { "bind: $ctx, $comment" }
            // BIND_AUTO_CREATE 会马上初始化service, 否则要等调用start才会一想回调onBind,
            // 但没初始化service也可以unbind和stop,
            ctx.bindService(ctx.intentFor<NormalService>(KEY_COMMENT to comment), conn, BIND_AUTO_CREATE).also {
                info { it }
            }
        }

        fun unbind(ctx: Context, comment: String) {
            info { "unbind: $ctx, $comment" }
            try {
                ctx.unbindService(conn)
            } catch (e: IllegalArgumentException) {
                // 没绑定的服务解绑时抛异常，
                // Service not registered,
                error { "unbind error: ${e.message}" }
            }
        }

        fun stop(ctx: Context, comment: String) {
            info { "stop: $ctx, $comment" }
            ctx.stopService(ctx.intentFor<NormalService>(KEY_COMMENT to comment)).also {
                // service没有start只有bind时调用stop也会返回true, 但是什么都没做，
                info { it }
            }
        }
    }

    /**
     * 只会有一个实例，
     * init和onCreate只调用一次，
     */
    init {
        info { "init: $this" }
    }

    /**
     * 只会有一个实例，
     * init和onCreate只调用一次，
     */
    override fun onCreate() {
        super.onCreate()
        info { "onCreate" }
    }

    /**
     * 每次start都会调用这个onStartCommand，无论是否bind,
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        info { "onStartCommand: $intent, $flags, $startId" }
        intent?.extras?.keySet()?.forEach { key ->
            info { "$key => ${intent.extras[key]}" }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * 多次bind只调用一次onBind，无论是否start,
     */
    override fun onBind(intent: Intent): IBinder {
        info { "onBind: $intent" }
        intent.extras.keySet().forEach { key ->
            info { "$key => ${intent.extras[key]}" }
        }
        return Binder()
    }

    /**
     * 第一次解绑会调用onUnbind，之后抛异常，
     */
    override fun onUnbind(intent: Intent?): Boolean {
        info { "onUnbind: $intent" }
        intent?.extras?.keySet()?.forEach { key ->
            info { "$key => ${intent.extras[key]}" }
        }
        return super.onUnbind(intent)
    }

    /**
     * 如果既有bind又有start, 相应的也要unbind加上stop才会销毁这个service,
     * 但不保证马上destroy,
     */
    override fun onDestroy() {
        info { "onDestroy" }
        super.onDestroy()
    }

    inner class Binder : android.os.Binder() {
        fun getService(): NormalService = this@NormalService
    }
}
