package comx.pkg
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
fun gotoNtfyUI(context: Context, packageName: String){
    try {

        //例如 Build.VERSION_CODES.O 表示 Android 8.0（API 级别 26）。
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android 8.0 及以上版本
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                 putExtra(Settings.EXTRA_APP_PACKAGE, packageName) // 当前应用包名
              //  putExtra(Settings.EXTRA_CHANNEL_ID, "my_channel_id") // 如果有特定渠道 ID
            }
        } else {
            // Android 8.0 以下版本
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
               // data = android.net.Uri.parse("package:$packageName") // 跳转到应用详情页
            }
        }

        context.  startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // 如果设备不支持对应的 Intent，提示用户
        Toast.makeText(context, "无法打开通知设置，请手动操作", Toast.LENGTH_LONG).show()
        e.printStackTrace()
    }
}

fun sendNotification(context: Context, title: String, message: String) {
    Log.d(tagLog, "fun sendNotification(")
    Log.d(tagLog, "tit="+title)
    Log.d(tagLog, "))")

    val channelId = "my_channel_id"
    val notificationId = 1

    // 创建通知渠道 (仅适用于 Android 8.0 及以上版本)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelName = "My Notification Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = "This is a test notification channel."
        }
        // 注册通知渠道
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // 创建通知
    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.ic_dialog_info) // 设置小图标
        .setContentTitle(title) // 设置标题
        .setContentText(message) // 设置内容
        .setPriority(NotificationCompat.PRIORITY_DEFAULT) // 设置优先级
        .build()

    // 发送通知
    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, notification)
    }
    Log.d(tagLog, "endfun sendNotification")
}