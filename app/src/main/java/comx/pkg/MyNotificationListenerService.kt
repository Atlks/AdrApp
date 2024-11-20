package comx.pkg

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.speech.tts.TextToSpeech
import androidx.core.app.NotificationCompat
import org.bouncycastle.asn1.x500.style.RFC4519Style.description
import java.util.Locale

class MyNotificationListenerService : NotificationListenerService(), TextToSpeech.OnInitListener {

    private var textToSpeech: TextToSpeech? = null

    override fun onCreate() {
        super.onCreate()
        // 在这里不初始化 TextToSpeech，延迟到 onStartCommand
       // textToSpeech = TextToSpeech(this, this)
    }

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        // 创建通知渠道 (仅适用于 Android 8.0 及以上版本)
        createNotificationChannel()

        // 显示前台通知
        startForeground(1, buildNotification())



        // 初始化 TextToSpeech，避免影响前台通知的启动
        textToSpeech = TextToSpeech(this, this)
        // 执行你的业务逻辑
        return START_STICKY
    }

    private fun createNotificationChannel() {
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

    private fun buildNotification(): Notification? {
        return NotificationCompat.Builder(this, "your_channel_id")
            .setContentTitle("服务正在运行")
            .setContentText("这是前台服务")
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
        val notification = sbn.notification
        val extras = notification.extras

        // 获取通知标题和内容
        val title = extras.getString("android.title") ?: "没有标题"
        val text = extras.getString("android.text") ?: "没有内容"

        // 拼接标题和内容
        val message = "标题: $title, 内容: $text"

        if(title=="没有标题" && text=="没有内容")
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

    // 语言检测方法
    private fun detectLanguage(text: String): Locale {
        return if (text.any { it.isCJKCharacter() }) {
            // 如果包含中文字符，使用中文
            Locale.CHINESE
        } else {
            // 否则使用英文
            Locale.ENGLISH
        }
    }

    // 扩展函数：检测是否为 CJK（中日韩）字符
    private fun Char.isCJKCharacter(): Boolean {
        return this.code in 0x4E00..0x9FFF ||  // 常用汉字
                this.code in 0x3400..0x4DBF ||  // 扩展汉字 A
                this.code in 0x20000..0x2A6DF   // 扩展汉字 B
    }
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech?.language = Locale.CHINESE
        }
    }
}
