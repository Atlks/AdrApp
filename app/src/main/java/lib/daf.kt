package lib

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.*

class MyApp : Application() {

    private lateinit var textToSpeech: TextToSpeech
    private val handler = Handler(Looper.getMainLooper())
    private val interval = 30_000L // 30秒

    override fun onCreate() {
        super.onCreate()
        startVoiceBroadcast()
    }

    private fun startVoiceBroadcast() {
        // 初始化 TextToSpeech
        textToSpeech = TextToSpeech(applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale.US // 语言
                scheduleNextBroadcast()
            } else {
                Log.e("TTS", "TextToSpeech 初始化失败")
            }
        }
    }

    private fun scheduleNextBroadcast() {
        handler.postDelayed({
            speak("OK")  // 语音播报
            scheduleNextBroadcast() // 递归调用，保持循环
        }, interval)
    }

    private fun speak(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onTerminate() {
        super.onTerminate()
        textToSpeech.stop()
        textToSpeech.shutdown()
        handler.removeCallbacksAndMessages(null)
    }
}
