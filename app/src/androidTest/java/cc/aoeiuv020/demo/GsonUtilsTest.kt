package cc.aoeiuv020.demo

import android.support.test.runner.AndroidJUnit4
import cc.aoeiuv020.gson.GsonUtils
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Created by AoEiuV020 on 2018.09.24-15:13:40.
 */
@RunWith(AndroidJUnit4::class)
class GsonUtilsTest {
    @Test
    fun dateFormat() {
        val s24 = "\"May 29, 2018 23:20:12\""
        val s12 = "\"May 29, 2018 11:20:12 PM\""
        val defaultGson = Gson()
        val myGson = GsonUtils.gson
        assertEquals(
                myGson.fromJson(s12, Date::class.java),
                myGson.fromJson(s24, Date::class.java)
        )
        try {
            // 如果系统是24小时制，会忽略PM解析出上午11点，
            println(defaultGson.fromJson(s12, Date::class.java))
            // 如果系统是12小时制，对于没有PM的s24就无法正确解析，
            println(defaultGson.fromJson(s24, Date::class.java))
        } catch (_: Exception) {
        }
    }
}