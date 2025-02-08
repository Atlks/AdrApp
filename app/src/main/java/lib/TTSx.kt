
// tts.kt
package lib;

//import  comx.pkg.UtilMmncKt.tagLog;

import android.content.Context
import android.speech.tts.TextToSpeech;
import android.util.Log;
import comx.pkg.Fmtforeachx
import comx.pkg.ListSms
import comx.pkg.MainActivity.Msg
import comx.pkg.encodeJson
import comx.pkg.encodeMd5
import comx.pkg.getDeviceName
import comx.pkg.getTimestampInSecs
import comx.pkg.sendMsg
import comx.pkg.tagLog
import comx.pkg.write_row
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale

var textToSpeech: TextToSpeech? = null
 @Deprecated("dep...")
fun initializeTextToSpeech(context: Context, funx: (textToSpeech22: TextToSpeech?) -> Unit) {

        Log.d(tagLog, "\n\n\n")
        Log.d(tagLog, "fun iniTTS()")


        //fun TextToSpeech
        textToSpeech = TextToSpeech(context, object : TextToSpeech.OnInitListener {
            override fun onInit(status: Int) {
                Log.d(tagLog, "\n\n\n")
                Log.d(tagLog, "fun tts.onini($status)")
                if (status == TextToSpeech.SUCCESS) {
                    // 进行 TextToSpeech 的配置和调用
                    //success in xm135g..but fail in xm12
                    Log.d(tagLog, "TextToSpeech initialization success..")
                    //  textToSpeech?.let { funx(it) }
                } else {
                    Log.d(tagLog, "TextToSpeech initialization failed with status code: $status")

                    //  Log.d(tagLog, "TextToSpeech initialization failed")
                }
                Log.d(tagLog, "endfun tts.onini()")
            }

            fun onDestroy() {
                Log.d(tagLog, "fun onDestroy2()")


                textToSpeech?.stop()
                textToSpeech?.shutdown()
                Log.d(tagLog, "endfun onDestroy2()")
            }


        })
     // end TextToSpeech()


        Log.d(tagLog, "endfun iniTTS()")
    }


//lks use this
 fun iniTTS(context: Context) {

    Log.d(tagLog, "\n\n\n")
    Log.d(tagLog, "fun iniTTS()")


    textToSpeech = TextToSpeech(context, object : TextToSpeech.OnInitListener {
        override fun onInit(status: Int) {
            Log.d(tagLog, "\n\n\n")
            Log.d(tagLog, "fun tts.onini($status)")
            if (status == TextToSpeech.SUCCESS) {
                // 进行 TextToSpeech 的配置和调用
                //success in xm135g..but fail in xm12
                Log.d(tagLog, "TextToSpeech initialization success..")
                //  textToSpeech?.let { funx(it) }
            } else {
                Log.d(tagLog, "TextToSpeech initialization failed with status code: $status")

                //  Log.d(tagLog, "TextToSpeech initialization failed")
            }
            Log.d(tagLog, "endfun tts.onini()")
        }

//            fun onDestroy() {
//                Log.d(tagLog, "fun onDestroy2()")
//
//
//                textToSpeech?.stop()
//                textToSpeech?.shutdown()
//                Log.d(tagLog, "endfun onDestroy2()")
//            }


    })
    Log.d(tagLog, "endfun iniTTS()")
}


fun rdTxtByTTSV2(message: String) {



    //fun rdtxt
    Log.d(tagLog, "fun rdTxtV2($message)")
    // 检查 textToSpeech 是否为空


    // 执行你的业务逻辑
    // 动态设置语言
    val language = Locale.CHINESE;//detectLanguage(message)
    // textToSpeech?.language = language
    // 这里可以设置语言、语速等
    val languageResult = textToSpeech?.setLanguage(language)
    //
    //
    // 检查语言是否支持
    if (languageResult == TextToSpeech.LANG_MISSING_DATA || languageResult == TextToSpeech.LANG_NOT_SUPPORTED) {
        Log.d(
            tagLog,
            "Language is missing or not supported, attempting to handle this."
        )
        // 你可以提示用户安装语言包，或者选择其他可用语言
    } else {
        Log.d(tagLog, "Language supported.")
    }
    // 使用 TTS 阅读
    Log.d(tagLog, "tts.spk() msg=" + message)


    // 设置语音合成完成后的监听器
    val utteranceId = getUuid()  // 唯一的ID，用来标识这次语音合成
//                val params = Bundle().apply {
//                    putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
//                }


    //  TTS 队列：TextToSpeech.QUEUE_FLUSH 表示清空之前的语音队列，立即播放当前消息。如果你希望多个语音消息依次播放，可以使用 TextToSpeech.QUEUE_ADD。

    textToSpeech?.speak(message, TextToSpeech.QUEUE_ADD, null, utteranceId)


    //if here then no spk,bcs cls too early
    // textToSpeech?.stop()
    // 设置完成语音合成后的监听器
//                textToSpeech.setOnUtteranceCompletedListener { utteranceId ->
//                    Log.d(tagLog, "Speech completed for utteranceId: $utteranceId")
//                    // 语音合成完成后，关闭 TTS
//                    textToSpeech.shutdown()
//                    textToSpeech?.shutdown()
//
//                    Log.d(tagLog, "TTS shutdown after speech completion.")
//                }

    Log.d(tagLog, "endfun rdtxtV2()")



}



fun speakOut(message: String) {
    Log.d(tagLog, "fun speakOut((" + message)
    Log.d(tagLog, "))")
    try {
        // 初始化 TextToSpeech，避免影响前台通知的启动
        // initializeTextToSpeech(rdTxt(message))

        rdTxtByTTSV2(message)
        //end  initializeTextToSpeech
        //  textToSpeech?.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)

    } catch (e: Exception) {
        Log.e(tagLog, "Error speakOut(): ${e.message}")
    }

    Log.d(tagLog, "endfun speakOut()")
}
//send tg replace
@Deprecated("")
  fun sendNecho(message: String) {
    // return;
    try {

        val time = getTimestampInSecs()
        var msgid = encodeMd5(deviceName + message + time)
        val msg1obj = Msg(deviceName, message, time, msgid)
        val encodeJson_msg = encodeJson(msg1obj)

         //our im
        sendMsg(encodeJson_msg)
        write_row(context8, msgid, encodeJson_msg);

        //-----------block show list
        //todo also need dep ?
        var smsList = ListSms()
        smsList = Fmtforeachx(smsList)
        //order by sendtime
        Log.d(tagLog, "smslist.size:" + smsList.size)
        // binding.textView.text = "cnt:" + smsList.size
        //goto main thrd updt ui
        // 切换到主线程更新 UI
        //not refrsh ui,bcs send to tg
//            var ma: MainActivity = AppCompatActivity1main as MainActivity
//            ma.runOnUiThread {
//                ma.bindData2Table(smsList);
//                //滚动到底部
//                scrToButtom(ma.binding.scrvw)
//            }
    } catch (e: Exception) {
        Log.e(tagLog, "Error sendNecho(): ${e.message}")
    }

}


