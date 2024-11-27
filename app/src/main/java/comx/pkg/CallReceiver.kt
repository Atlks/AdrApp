package comx.pkg

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.telephony.TelephonyManager
import android.util.Log
import java.util.Locale

class CallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.PHONE_STATE") {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                incomingNumber?.let {
                    speakCallerInfo(context, it)
                }
            }
        }
    }

    private fun speakCallerInfo(context: Context, number: String) {
        // 声明一个可变变量，用于 TextToSpeech 的实例
        var tts: TextToSpeech? = null

        // 初始化 TextToSpeech 实例
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
               // tts.language = Locale.getDefault()
                tts?.language =     Locale.CHINESE
                val message = "Incoming call from $number"

                // 设置播报状态监听器
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                      override fun onStart(utteranceId: String?) {
                        // 可选：播报开始时的逻辑
                    }

                      override fun onDone(utteranceId: String?) {
                        // 播报完成后释放资源
                        tts?.shutdown()
                    }

                      override fun onError(utteranceId: String?) {
                        // 出现错误时释放资源
                        tts?.shutdown()
                    }
                })

                // 使用指定的 Utterance ID 进行播报
                tts?.speak(message, TextToSpeech.QUEUE_FLUSH, null, "CALLER_ID")
            } else {
                Log.e("TTS", "TextToSpeech 初始化失败")
            }
        }
    }


    @SuppressLint("Range")
    private fun getContactName(context: Context, phoneNumber: String): String {
        val resolver = context.contentResolver
        val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber))
        val cursor = resolver.query(uri, arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME), null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                return it.getString(it.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)) ?: phoneNumber
            }
        }
        return phoneNumber
    }
}
