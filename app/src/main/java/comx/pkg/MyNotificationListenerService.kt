package comx.pkg

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.speech.tts.TextToSpeech
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.util.Locale

class MyNotificationListenerService : NotificationListenerService(), TextToSpeech.OnInitListener {

    private var textToSpeech: TextToSpeech? = null

    override fun onCreate() {
        super.onCreate()
        // 在这里不初始化 TextToSpeech，延迟到 onStartCommand
       // textToSpeech = TextToSpeech(this, this)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        // 创建通知渠道 (仅适用于 Android 8.0 及以上版本)
        newNotificationChannel()

        // 显示前台通知
      //  startForeground(1, buildNotification())
        startForeground(1, newNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)


        // 初始化 TextToSpeech，避免影响前台通知的启动
        textToSpeech = TextToSpeech(this, this)
        // 执行你的业务逻辑
        return START_STICKY
    }

    private fun newNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "your_channel_id"
            val channelName = "My Foreground Service"
            val channelDescription = "Notification Channel for Foreground Service"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun newNotification(): Notification{
        return NotificationCompat.Builder(this, "your_channel_id")
            .setContentTitle("服务正在运行")
            .setContentText("服务runing...."+getNow())
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }

    override fun onDestroy() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        super.onDestroy()
    }

    @SuppressLint("NewApi")
    override fun onNotificationPosted(sbn: StatusBarNotification) {

//       if(!isTimeInDaytim())
//           return

        val notification = sbn.notification
        val extras = notification.extras

        // 获取通知标题和内容
        val title = extras.getString("android.title") ?: "没有标题"
        val text = extras.getString("android.text") ?: "没有内容"

        // 拼接标题和内容
        val message = "标题: $title, 内容: $text"

        if(title=="没有标题" && text=="没有内容")
            return

        if(title=="" && text=="")
            return

        if(title.toLowerCase()=="timer")
            return

        // 使用 TTS 阅读通知内容
        speakOut(message)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // 可选：当通知被移除时执行操作
    }

    private fun speakOut(message: String) {
        // 动态设置语言
        val language = detectLanguage(message)
        textToSpeech?.language = language

        // 使用 TTS 阅读
        textToSpeech?.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)

      //  textToSpeech?.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
    }


    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech?.language = Locale.CHINESE
        }
    }
}
