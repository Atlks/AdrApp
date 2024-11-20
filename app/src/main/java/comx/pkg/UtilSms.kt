package comx.pkg

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.Telephony
import android.util.Log
import android.widget.Toast
  fun toBodyLikeStr(txt: String): String {
    if (txt.isBlank()) return "" // 如果输入为空或全是空格，返回空数组 // 如果输入为空，返回 null
    val toTypedArray = txt.split(" ") // 按空格分割字符串
        .filter { it.isNotBlank() } // 过滤掉空白项
        .map { "body LIKE ? " } // 添加 '%' 符号
        .toTypedArray()
    return joinToStr(toTypedArray, " and ") // 转为 Array
}

/**
 * 根据传入的sms id删除指定的短信
 */
  fun delSms(context: Context, smsid: CharSequence?) {
    Log.d(tagLog, "fun delsms(" + smsid)
    Log.d(tagLog, ")))")
    // 检查 smsid 是否为空
    if (smsid.isNullOrEmpty()) {
        Toast.makeText(context, "短信ID无效", Toast.LENGTH_SHORT).show()
        return
    }

    try {
        // 获取 ContentResolver
        val contentResolver: ContentResolver = context.contentResolver

        // 构建短信的 URI  content://sms/2135
        val smsUri: Uri = Uri.withAppendedPath(Telephony.Sms.CONTENT_URI, smsid.toString())
        Log.d(tagLog, smsUri.toString())
        // 执行删除操作
        val rowsDeleted = contentResolver.delete(smsUri, null, null)
        Log.d(tagLog, "检查删除结果rowsDeleted:" + rowsDeleted.toString())
        // 检查删除结果
        if (rowsDeleted > 0) {
            Toast.makeText(context, "短信删除成功", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "未找到指定的短信", Toast.LENGTH_SHORT).show()
        }
        Log.d(tagLog, "endfun delsms()")

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "删除短信失败", Toast.LENGTH_SHORT).show()
    }
}
