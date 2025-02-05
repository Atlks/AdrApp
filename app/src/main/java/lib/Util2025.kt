package lib

import android.speech.tts.TextToSpeech
import android.util.Log
import comx.pkg.tagLog
import java.util.Locale
import java.util.UUID



/**
 * 功能 检测字符串str是否包含任何所列出的单词..
 * containWords 空格分割的字符串，要检测包含的单词表
 * str 字符串
 */
  fun containsAny2025(containWords: String, str: String): Boolean {

    // 将 containWords 按空格分割成单词列表
    val words = containWords.split(" ")
        .filter { it.isNotBlank() }  // 过滤掉空字符串

    // 遍历每个单词，检查 str 是否包含该单词
    for (word in words) {
        if(word.trim().equals(""))
            continue
        if (str.contains(word, ignoreCase = true)) {
            return true  // 如果 str 包含任意一个单词，返回 true
        }
    }

    // 如果 str 不包含任何一个单词，返回 false
    return false
}
  fun rdTxt(message: String): (textToSpeech22: TextToSpeech?) -> Unit {

    return { textToSpeech ->

        //fun rdtxt
        Log.d(tagLog, "fun rdtxt()")
        // 检查 textToSpeech 是否为空
        if (textToSpeech == null) {
            Log.d(tagLog, "fun rdtxt()#tts is null")
            Log.d(tagLog, "endfun rdtxt() ")
            // return
        }


        if(textToSpeech!=null){
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
            textToSpeech.setOnUtteranceCompletedListener { utteranceId ->
                Log.d(tagLog, "Speech completed for utteranceId: $utteranceId")
                // 语音合成完成后，关闭 TTS
                textToSpeech.shutdown()
                textToSpeech?.shutdown()

                Log.d(tagLog, "TTS shutdown after speech completion.")
            }

            Log.d(tagLog, "endfun rdtxt()")

        }

    }
}

  fun getUuid(): String? {
// 生成一个随机的 UUID，并将其转换为字符串
    return UUID.randomUUID().toString()
}