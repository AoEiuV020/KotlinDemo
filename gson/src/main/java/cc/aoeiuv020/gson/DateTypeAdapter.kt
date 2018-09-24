package cc.aoeiuv020.gson

import com.google.gson.JsonSyntaxException
import com.google.gson.TypeAdapter
import com.google.gson.internal.bind.util.ISO8601Utils
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.text.ParseException
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by AoEiuV020 on 2018.09.24-14:37:56.
 */
@SuppressWarnings("SimpleDateFormat")
internal object DateTypeAdapter : TypeAdapter<Date>() {
    /**
     * 主要使用的格式，
     * 也就是写入json时用的格式，
     * 完整的iso8601,
     * 语言固定英文，
     */
    private val mainFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US)
    /**
     * 读取时检查的格式，
     * 主要是因为gson默认的格式受系统设置24小时制或者12小时制影响，
     * 这意味着24小时制生成的时间字符串在12小时制无法解析，因为没有am.pm.
     * 这里先检查这两种时间格式，都不对再用gson自带的iso8601解析，
     * 语言支持当前语言和英文，gson默认也是这样，
     */
    @SuppressWarnings("ConstantLocale")
    private val checkFormatList = listOf(
            "MMM d, y h:mm:ss a",
            "MMM d, y HH:mm:ss"
    ).flatMap { pattern ->
        listOf(Locale.getDefault(), Locale.US)
                .map { SimpleDateFormat(pattern, it) }
    }

    override fun write(writer: JsonWriter, value: Date?) {
        if (value == null) {
            writer.nullValue()
            return
        }
        val dateFormatAsString = mainFormat.format(value)
        writer.value(dateFormatAsString)
    }

    override fun read(reader: JsonReader): Date? {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return null
        }
        return deserializeToDate(reader.nextString())
    }

    /**
     * 解析时一步步尝试格式，以支付多种格式，
     * 首先是自己首先是自己导出的格式，
     * 然后是gson默认，也是java默认的格式，
     * 包括24小时制和12小时制，以及本地加英文两种语言，
     * 最后是调用gson自带的iso8601解析，
     */
    private fun deserializeToDate(json: String): Date? {
        try {
            return mainFormat.parse(json)
        } catch (_: ParseException) {
        }
        for (sdf in checkFormatList) {
            try {
                return sdf.parse(json)
            } catch (_: ParseException) {
            }
        }
        try {
            return ISO8601Utils.parse(json, ParsePosition(0))
        } catch (_: ParseException) {
            throw JsonSyntaxException("不支持的时间格式<$json>")
        }

    }
}