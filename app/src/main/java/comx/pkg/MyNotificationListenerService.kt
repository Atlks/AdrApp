package comx.pkg

import android.annotation.SuppressLint
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.speech.tts.TextToSpeech
import java.util.Locale

class MyNotificationListenerService : NotificationListenerService(), TextToSpeech.OnInitListener {

    private var textToSpeech: TextToSpeech? = null

    override fun onCreate() {
        super.onCreate()
        textToSpeech = TextToSpeech(this, this)
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
