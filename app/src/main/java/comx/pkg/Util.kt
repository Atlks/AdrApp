package comx.pkg

import android.content.Context
import android.widget.Toast
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.os.Handler
import java.security.MessageDigest


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

fun getFileNameWithCurrentTime(): String {
    val dateFormat = SimpleDateFormat("MMdd_HHmmss", Locale.getDefault())
    val currentTime = dateFormat.format(Date())
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
