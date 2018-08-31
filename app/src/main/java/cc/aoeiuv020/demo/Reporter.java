package cc.aoeiuv020.demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;

import java.net.UnknownHostException;

/**
 * 封装异常上报，
 * 当前使用，腾讯的bugly,
 * <p>
 * 要在Application.onCreate中调用init方法初始化，
 *
 * @author linenlian
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class Reporter {
    @SuppressLint("StaticFieldLeak")
    private static final Reporter INSTANCE = new Reporter();

    private static final String TAG = "Reporter";
    private Context ctx;

    private Reporter() {
    }

    public static Reporter getInstance() {
        return INSTANCE;
    }

    public static void init(Context ctx) {
        getInstance().ctx = ctx.getApplicationContext();
        getInstance().initBugly();
    }

    private void initBugly() {
        // 初始化，第三个参数为SDK调试模式开关，打开会导致开发机上报异常，
        CrashReport.initCrashReport(ctx, BuildConfig.BUGLY_APP_ID, BuildConfig.DEBUG && Log.isLoggable("Bugly", Log.DEBUG));
        // 貌似设置了开发设备就不上报了，不是很靠得住，依然可能上报，
        CrashReport.setIsDevelopmentDevice(ctx, BuildConfig.DEBUG);
        @SuppressLint("HardwareIds")
        String androidId = android.provider.Settings.Secure.getString(ctx.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        CrashReport.setUserId(androidId);
        CrashReport.setAppChannel(ctx, BuildConfig.BUGLY_APP_CHANNEL);
        if (!BuildConfig.DEBUG) {
            // 发行版上报的版本号最后跟上时间，这样app版本号可以不每次都改，
            // 以防万一debug版上报了日志导致版本号过多，只改release版，
            CrashReport.setAppVersion(ctx, BuildConfig.RELEASE_BUGLY_APP_VERSION);
        }
    }

    public void debug(String message, Throwable t) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message, t);
        }
    }

    /**
     * 对不可空的参数调用该方法，上报参数异常并抛出，
     */
    public <E> E notNullOrReport(E e) {
        return notNullOrReport(e, "value");
    }

    /**
     * 对不可空的参数调用该方法，上报参数异常并抛出，
     */
    public <E> E notNullOrReport(E e, String value) {
        if (e == null) {
            String message = value + "不可空，";
            RuntimeException t = new IllegalArgumentException(message);
            post(message, t);
            throw t;
        }
        return e;
    }

    /**
     * 无法到达的代码块调用这个方法，
     * 以防万一到达了可以看到，
     */
    public void unreachable() {
        post("不可到达，");
    }

    /**
     * 无法到达的代码块调用这个方法，
     * 以防万一到达了可以看到，
     */
    public void unreachable(Throwable t) {
        post("不可到达，", t);
    }

    public void post(String message) {
        if (message == null) {
            message = "null";
        }
        Throwable t = new IllegalStateException(message);
        debug(message, t);
        postException(t);
    }

    public void post(String message, Throwable t) {
        if (message == null) {
            message = "null";
        }
        debug(message, t);
        postException(new IllegalStateException(message, t));
    }

    public void postException(Throwable t) {
        if (t == null) {
            return;
        }
        // 开发过程不要上报，
        if (BuildConfig.DEBUG) {
            return;
        }
        Throwable cause = t;
        while (cause != null) {
            if (isNoInternetException(cause)) {
                // 没有网络连接导致的异常不上报，
                return;
            }
            // 以防万一，虽然应该不会出现cause就是本身导致死循环，
            if (cause.getCause() == cause) {
                break;
            }
            cause = cause.getCause();
        }
        CrashReport.postCatchedException(t);
    }

    /**
     * 判断这个异常是不是没有网络导致的，
     * 不准确，只是过滤部分明显的情况，
     *
     * @return 是断网导致的异常则返回true,
     */
    private boolean isNoInternetException(Throwable t) {
        if (t == null) {
            return false;
        }
        if (t instanceof UnknownHostException) {
            return true;
        }
        //noinspection RedundantIfStatement
        if (t.getMessage().contains("No address associated with hostname")) {
            // 有的设备报的不是UnknownHostException，原因不明，
            // android_getaddrinfo failed: EAI_NODATA (No address associated with hostname)
            return true;
        }
        return false;
    }
}
