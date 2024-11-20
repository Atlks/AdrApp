package comx.pkg
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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