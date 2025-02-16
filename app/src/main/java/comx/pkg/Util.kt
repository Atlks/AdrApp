package comx.pkg

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaScannerConnection
import android.net.Uri
import android.widget.Toast
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.os.Handler
import java.security.MessageDigest

import android.provider.Settings
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import org.json.JSONObject
import java.io.IOException
import java.time.LocalTime
val set4delp = hashSetOf<String>() // 创建一个空的 HashSet


/**
 *如果key不存在,返回def值
 *
 *
 *
 * 其他各种错误情况下，返回def值
 */
fun getFldLong(jsonobj: JSONObject?, key: String, def: Long): Long {
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
fun getFld(jsonobj: JSONObject?, key: String): String {
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
        return "1970-01-01 00:00:00"
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
        return "1970-01-01 00:00:00"
    }


}

fun getDvcIdFlFrg(): String {
    return "${Build.BRAND}@${Build.MODEL}".trimIndent()
}


fun getDvcId(): String {
    return "${Build.BRAND}_${Build.MODEL}".trimIndent()
}

/**
 * kotlin 实现播放
 *   playAudio("Documents/Darin-Be What You Wanna Be HQ.mp3")
 *
 */
@SuppressLint("Range")
fun playAudio(mp3: String) {


    // 创建 MediaPlayer 实例

    // 确保已经请求了权限
    // android.Manifest.permission.READ_EXTERNAL_STORAGE
    if (ContextCompat.checkSelfPermission(
            AppCompatActivity1main,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            AppCompatActivity1main,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )
        // return
    }

    Thread(Runnable {
        try {
            Log.d(tagLog, "\n\n\n")
            Log.d(tagLog, "fun playAudio($mp3)")

            val existx = isExistFile(mp3)
            Log.d(tagLog, "isExistFile=" + existx)


//add mp3
            val contentResolver = AppCompatActivity1main.contentResolver // 或者直接使用 'contentResolver'

            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "Be What You.mp3") // 文件名
                put(MediaStore.Audio.Media.TITLE, "Be What You")
                put(
                    MediaStore.Audio.Media.DATA,
                    "/storage/emulated/0/Music/Darin-Be What You Wanna Be HQ.mp3"
                ) // 文件的绝对路径
                put(MediaStore.Audio.Media.MIME_TYPE, "audio/mpeg")
                put(MediaStore.Audio.Media.SIZE, getFileLen(mp3))
                put(MediaStore.Audio.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            }

            val contentUri = MediaStore.Files.getContentUri("external")
            // val contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val uriMp3 = contentResolver.insert(contentUri, values)
            //  content://media/external/audio/media/1000029723

// 插入后可以获得 URI，可以在这里进行处理或返回成功提示
            if (uriMp3 != null) {
                Log.d(tagLog, "File added: $uriMp3")
            }


            // 插入文件和扫描：在 MediaStore 中插入文件后，使用 MediaScannerConnection.scanFile 扫描文件，这样文件就会被系统识别。
            // 4. 使用 MediaScanner 扫描文件并等待其准备完毕
            val filePath = "/storage/emulated/0/Music/Darin-Be What You Wanna Be HQ.mp3"
            MediaScannerConnection.scanFile(
                AppCompatActivity1main,
                arrayOf(filePath),
                null
            ) { path, uri ->
                Log.d("MediaScanner", "Scanned file: $path, Uri: $uri")
            }
            Thread.sleep(2000)


            val mediaPlayer = MediaPlayer()
            if (mediaPlayer == null) {
                Log.d(tagLog, "Failed to initialize MediaPlayer")

            }

            // mediaPlayer.reset()  // 重置播放器
            // 设置音频资源的路径，假设你传入的是文件路径
            //hrer urimp3==contentUri
            Log.d(tagLog, "setDataSource uriMp3=" + uriMp3)


            // mediaPlayer.setDataSource(  "/storage/emulated/0/Music/1732962347874.mp3")


            if (uriMp3 != null) {
                val contentUri = Uri.parse(uriMp3.toString()) // 获取的 content URI
                //contentUri==content://media/external/audio/media/<audio_id>
                mediaPlayer.setDataSource(
                    AppCompatActivity1main,
                    contentUri
                ) // 使用 content URI 设置数据源
                // var fpath="file:///storage/emulated/0/Music/Darin-Be What You Wanna Be HQ.mp3"
                //    mediaPlayer.setDataSource(AppCompatActivity1main,Uri.parse(fpath))
                //  mediaPlayer.setDataSource(AppCompatActivity1main, uriMp3)
            }

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer.setOnErrorListener { mp, what, extra ->
                Log.e(tagLog, "MediaPlayer error occurred. What: $what, Extra: $extra")
                true // Returning true to indicate the error was handled
            }
            mediaPlayer.isLooping = true


            // 准备音频文件
            Log.d(tagLog, "Preparing media player with source: $mp3")
            mediaPlayer.prepare()
            Log.d(tagLog, "Media player prepared, starting playback.")
            mediaPlayer.start()


            // 监听播放完成事件
            mediaPlayer.setOnCompletionListener {
                // 播放完成后可以释放资源
                mediaPlayer.release()
            }
        } catch (e: IOException) {
            Log.e(tagLog, "playAudio() Caught exception", e)

            e.printStackTrace() // 打印异常日志
        }

        Log.d(tagLog, "endfun playAudio()")

    }).start()

    Thread(Runnable {

    }).start()
}

/**
 *
 *         //---------add mp3 to  contentResolver
 * //        val filePath = "/storage/emulated/0/Music/Darin-Be What You Wanna Be HQ.mp3"
 * //        MediaScannerConnection.scanFile(AppCompatActivity1main, arrayOf(filePath), null) { path, uri ->
 * //            Log.d("MediaScanner", "Scanned file: $path, Uri: $uri")
 * //            println(11)
 * //        }
 * //        Thread.sleep(500)  // 等待2秒，确保扫描完成
 *
 *         //add
 *
 *
 *       //  val contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
 *      //   MediaStore.Audio.Media.
 *        // val contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
 *         val cursor = contentResolver.query(contentUri, null, null, null, null)
 *         cursor?.let {
 *             while (it.moveToNext()) {
 *                 val filePath = it.getString(it.getColumnIndex(MediaStore.Audio.Media.DATA))
 *                 // 对文件路径进行处理
 *               Log.d(tagLog, filePath)
 *                 Log.d(tagLog, "\n...\n")
 *             }
 *         }
 *         cursor?.close()
 */

fun getFileLen(mp3: String): Long {
    val file = File(mp3)
    return file.length()
}

fun isExistFile(mp3: String): Any {
    // 假设 mp3 是文件的相对路径，文件存储在设备的内存中
    val file = File(mp3)

    // 检查文件是否存在
    return file.exists()
}

fun formatPhoneNumberForTTS(phoneNumber: String): String {
    val numberMapping = mapOf(
        '0' to "零", '1' to "一", '2' to "二", '3' to "三", '4' to "四",
        '5' to "五", '6' to "六", '7' to "七", '8' to "八", '9' to "九"
    )

    val formatted = StringBuilder()
    for (ch in phoneNumber) {
        if (ch.isDigit()) {
            formatted.append(numberMapping[ch] ?: ch)
        } else {
            formatted.append(ch)
        }
    }
    return formatted.toString()
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
    val dateFormat = getFileNameWithCurrentTime()
    val currentTime = dateFormat.replace("_", "T")
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
