package comx.pkg

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import java.io.OutputStream
import java.io.OutputStreamWriter


/**
 * 使用 MediaStore 存储文件到公共目录
 */
  fun wrtFilToDocumentDir(
    context: Context,
    jsonString: String?,
    fldr: Any?,
    fileName: String
): Any {
    // 检查传入的 jsonString 是否为空
    if (jsonString.isNullOrEmpty()) {
        Log.e("wrtFilToDocumentDir", "JSON string is empty or null")
        return "Invalid input: JSON string is null or empty"
    }

    try {
        // 获取 ContentResolver
        val contentResolver = context.contentResolver

        // 设置文件名和 MIME 类型
        //   val fileName = "sms_export.json"
        val mimeType = "application/json"

        // 创建 ContentValues
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName) // 文件名
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)    // MIME 类型
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Documents" + fldr)  // 文件存储路径
        }

        // 使用 MediaStore 创建文件
        val uri =
            contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
                ?: return "Failed to create file in MediaStore"

        // 打开输出流写入文件内容
        val outputStream: OutputStream? = uri?.let {
            contentResolver.openOutputStream(it)
        }

        outputStream?.use {
            val writer = OutputStreamWriter(it)
            writer.write(jsonString)
            writer.flush()
        }

        return "File successfully saved to Documents/aasms/$fileName"
    } catch (e: Exception) {
        Log.e("wrtFilToDocumentDir", "Error saving file: ${e.message}")
        return "Error saving file: ${e.message}"
    }
}
