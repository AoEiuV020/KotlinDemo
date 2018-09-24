@file:Suppress("unused")

package cc.aoeiuv020.jsonpath

import com.google.gson.JsonElement
import com.jayway.jsonpath.Configuration
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.ReadContext
import com.jayway.jsonpath.TypeRef
import java.io.InputStream

/**
 * Created by AoEiuV020 on 2018.09.24-17:11:43.
 */
object JsonPathUtils {
    // 用到的地方都提前调用一下这个初始化，
    fun initGson() {
        // 重复设置无效，什么都不会发生，必须在第一次被使用前设置，
        Configuration.setDefaults(GsonJsonPathConfiguration)
    }
}

inline fun <reified T> typeRef(): TypeRef<T> = object : TypeRef<T>() {}

inline fun <reified T> JsonElement.read(path: String): T = read(path, typeRef())
fun <T> JsonElement.read(path: String, typeRef: TypeRef<T>): T = JsonPath.parse(this).read(path, typeRef)
val String.jsonPath: ReadContext get() = JsonPath.parse(this)
val InputStream.jsonPath: ReadContext get() = JsonPath.parse(this)
val JsonElement.jsonPath: ReadContext get() = JsonPath.parse(this)
// @是根节点，
inline fun <reified T> ReadContext.get(path: String = "@"): T = read(path, typeRef())
