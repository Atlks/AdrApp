package comx.pkg

import android.content.Context
import android.widget.Toast
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.os.Handler
import java.security.MessageDigest


import android.os.Build
import android.util.Log
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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

fun encodeMd5(s: String): String {
// 获取 MD5 MessageDigest 实例
    val digest = MessageDigest.getInstance("MD5")

    // 将字符串转换为字节数组并进行哈希计算
    val hashBytes = digest.digest(s.toByteArray())

    // 将字节数组转换为十六进制字符串
    return hashBytes.joinToString("") { "%02x".format(it) }
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

fun encodeJson(selectionArgs: Array<String>?): Any? {
    return if (selectionArgs != null) {

        // 创建 SelectionArgs 对象并将其序列化为 JSON 字符串
        // val selectionArgsObj = SelectionArgs(selectionArgs)
        val jsonString = Json.encodeToString(selectionArgs)
        jsonString
    } else {
        ""
    }
}
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
