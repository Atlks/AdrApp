package comx.pkg

import android.content.Context
import android.widget.Toast
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.os.Handler
import java.security.MessageDigest

import android.provider.Settings
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import org.json.JSONObject
import java.time.LocalTime


/**
 *如果key不存在,返回def值
 *
 *
 *
 * 其他各种错误情况下，返回def值
 */
  fun getFldLong(jsonobj: JSONObject?, key: String,def:Long): Long {
    return try {
        if (jsonobj != null && jsonobj.has(key) && !jsonobj.isNull(key)) {
            jsonobj.getLong(key)
        } else {
            def
        }
    } catch (e: Exception) {
        def
    }
}
/**
 * 获取值，如果值为null，或没有此key，返回空字符串
 */
fun getFld(jsonobj: JSONObject?,key:String): String {
    return try {
        if (jsonobj != null && jsonobj.has(key) && !jsonobj.isNull(key)) {
            jsonobj.getString(key)
        } else {
            ""
        }
    } catch (e: Exception) {
        ""
    }
}


fun getNow(): String {
    try {
        val date = Date() // 将时间戳转换为 Date 对象
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()) // 定义格式
        return format.format(date) // 格式化日期
    } catch (e: Exception) {
        // 处理异常
        Log.e(tagLog, "Caught exception", e)
        return  "1970-01-01 00:00:00"
    }


}

fun formatTimestamp(timestamp: Long): String {
    try {
        val date = Date(timestamp) // 将时间戳转换为 Date 对象
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()) // 定义格式
        return format.format(date) // 格式化日期
    } catch (e: Exception) {
        // 处理异常
        Log.e(tagLog, "Caught exception", e)
        return  "1970-01-01 00:00:00"
    }


}

fun getDvcIdFlFrg(): String {
    return "${Build.BRAND}@${Build.MODEL}".trimIndent()
}


fun getDvcId(): String {
    return "${Build.BRAND}_${Build.MODEL}".trimIndent()
}

fun getDeviceName(context: Context): String {
    return Settings.Secure.getString(context.contentResolver, "bluetooth_name")
}
fun getDeviceInfo(): String {
    return """
        品牌 (Brand): ${Build.BRAND}
        制造商 (Manufacturer): ${Build.MANUFACTURER}
        型号 (Model): ${Build.MODEL}
        产品 (Product): ${Build.PRODUCT}
        硬件 (Hardware): ${Build.HARDWARE}
        设备 (Device): ${Build.DEVICE}
        主板 (Board): ${Build.BOARD}
        系统版本 (OS Version): ${Build.VERSION.RELEASE} (API Level: ${Build.VERSION.SDK_INT})
        构建 ID (Build ID): ${Build.ID}
        用户 (User): ${Build.USER}
    """.trimIndent()
}

//8--23
@RequiresApi(Build.VERSION_CODES.O)
fun isTimeInDaytim(): Boolean {
    // 获取当前时间
    val now = LocalTime.now()

    // 定义时间范围
    val startTime = LocalTime.of(10, 0) // 早上10点
    val endTime = LocalTime.of(23, 0)  // 下午7点

    // 判断当前时间是否在范围内
    if (now.isAfter(startTime) && now.isBefore(endTime)) {
        // println("888")
        return true
    }
    return false
}
/**
 * 10---21
 */
@RequiresApi(Build.VERSION_CODES.O)
fun isTimeInWktim(): Boolean {
    // 获取当前时间
    val now = LocalTime.now()

    // 定义时间范围
    val startTime = LocalTime.of(10, 0) // 早上10点
    val endTime = LocalTime.of(19, 0)  // 下午7点

    // 判断当前时间是否在范围内
    if (now.isAfter(startTime) && now.isBefore(endTime)) {
       // println("888")
        return true
    }
    return false
}


fun encodeMd5(s: String): String {
// 获取 MD5 MessageDigest 实例
    val digest = MessageDigest.getInstance("MD5")

    // 将字符串转换为字节数组并进行哈希计算
    val hashBytes = digest.digest(s.toByteArray())

    // 将字节数组转换为十六进制字符串
    return hashBytes.joinToString("") { "%02x".format(it) }
}
fun getTimestampInSecs(): Long {
    return System.currentTimeMillis() / 1000
}
fun showToast(context: Context, message: String, delaySec: Long) {
    // 显示Toast
    val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
    toast.show()

    // 使用Handler延迟7秒后取消Toast
    val handler = Handler()
    handler.postDelayed({
        toast.cancel()  // 取消Toast
    }, delaySec * 1000)  // 延迟7秒
}
  fun toLongx(numStr: String?): Long {
    // 如果 numStr 为空或者无法转换为 Long，则返回 0 或其他默认值
    return numStr?.toLongOrNull() ?: 0L
}

  fun joinToStr(toTypedArray: Array<String>, separator: String): String {
    // 使用 joinToString 方法将数组元素连接为一个字符串，并用指定的分隔符分隔
    return toTypedArray.joinToString(separator)
}

/**
 * txt: aaa bbb
 * 希望可以返回  arrayOf("%aaa%", "%bbb%")
 */
  fun to_arrayOf(txt: String): Array<String>? {
    if (txt.isBlank()) return emptyArray() // 如果输入为空或全是空格，返回空数组 // 如果输入为空，返回 null
    return txt.split(" ") // 按空格分割字符串
        .filter { it.isNotBlank() } // 过滤掉空白项
        .map { "%$it%" } // 添加 '%' 符号
        .toTypedArray() // 转为 Array
}

fun decodeJson(message: String): JSONObject? {
    return try {
        // 使用 JSONObject 解析 JSON 字符串
        JSONObject(message)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


/**
 * 使用 Gson 将对象编码为 JSON 字符串
 * @param obj 要编码的对象
 * @return 对象的 JSON 字符串表示形式
 */
fun encodeJson(obj: Any): String {
    val gson = Gson()
    return gson.toJson(obj)
}

/**
 * 使用gson来实现
 */
//fun encodeJson(obj: Any): String {
//    // Ensure the object is serializable
//    if (obj is Serializable) {
//        val jsonString = Json.encodeToString(obj)
//        println(jsonString)
//        return jsonString
//    } else {
//        throw IllegalArgumentException("Object must be serializable")
//    }
//}
//fun encodeJson(selectionArgs: Any): String {
//    return if (selectionArgs != null) {
//
//        // 创建 SelectionArgs 对象并将其序列化为 JSON 字符串
//        // val selectionArgsObj = SelectionArgs(selectionArgs)
//        val jsonString = Json.encodeToString(selectionArgs)
//        jsonString
//    } else {
//        ""
//    }
//}
fun getFileNameWithCurrentTime(): String {
    val dateFormat = SimpleDateFormat("MMdd_HHmmss", Locale.getDefault())
    val currentTime = dateFormat.format(Date())
    return currentTime
}

fun getFileNameWzTime4FlNmFrg(): String {
    val dateFormat =getFileNameWithCurrentTime()
    val currentTime = dateFormat.replace("_","T")
    return currentTime
}



/**
 * 创建文件f所在的文件夹，如果父文件夹不存在，级联创建
 */
fun mkdir2024(f: String) {
    val file = File(f)
    val parentDir = file.parentFile
    if (parentDir != null && !parentDir.exists()) {
        parentDir.mkdirs() // 级联创建父文件夹
    }
}
