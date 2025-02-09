package lib

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import comx.pkg.appContext
var sharedPreferences = appContext. getSharedPreferences("ringtone_prefs", Context.MODE_PRIVATE)

//var KEY_RINGTONE_URI="KEY_RINGTONE_URI1"
/**
 * 获取已存储的铃声 URI
 */
  fun getSavedRingtoneUri(KEY_RINGTONE_URI:String ): Uri? {
    // var sharedPreferences: SharedPreferences
    sharedPreferences = appContext. getSharedPreferences("ringtone_prefs", Context.MODE_PRIVATE)
    val uriString = sharedPreferences.getString(KEY_RINGTONE_URI, null)
    return uriString?.let { Uri.parse(it) }
}

/**
 * 存储铃声 URI 到 SharedPreferences
 */
  fun setRingtoneUri(KEY_RINGTONE_URI:String,uri: Uri) {
    sharedPreferences = appContext. getSharedPreferences("ringtone_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit()
        .putString(KEY_RINGTONE_URI, uri.toString())
        .apply()
}

fun getInShrPref(KEY_RINGTONE_URI:String ): Uri? {
    // var sharedPreferences: SharedPreferences
    sharedPreferences = appContext. getSharedPreferences("ringtone_prefs", Context.MODE_PRIVATE)
    val uriString = sharedPreferences.getString(KEY_RINGTONE_URI, null)
    return uriString?.let { Uri.parse(it) }
}
fun setInShrPref(KEY_RINGTONE_URI:String,uri: Uri) {
    sharedPreferences = appContext. getSharedPreferences("ringtone_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit()
        .putString(KEY_RINGTONE_URI, uri.toString())
        .apply()
}


/**
 * 2️⃣ 预选上次选择的铃声
 * 📌 当用户打开铃声选择界面时，默认选中上次选择的铃声：
 *
 * kotlin
 * 复制
 * 编辑
 * val savedUri = getSavedRingtoneUri()
 * putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, savedUri)
 */

/**
 * 加载并播放已存储的铃声（可选）
 */
  fun PlayRingtone(KEY_RINGTONE_URI:String) {

    val savedUri = getSavedRingtoneUri(KEY_RINGTONE_URI)
    if (savedUri != null) {
        var ringtone = RingtoneManager.getRingtone(appContext, savedUri)
        ringtone?.stop() // 停止之前的铃声
        ringtone?.play()
    }
}