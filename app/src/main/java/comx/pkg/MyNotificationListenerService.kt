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
import com.aaapkg.R
import comx.pkg.MainActivity.Msg
import lib.containsAny2025


import java.util.Locale
import java.util.UUID
import android.util.Log
import lib.getUuid

import org.json.JSONObject
import java.io.IOException
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

/**
 * ord
 * onCreate()..onStartCommand()
 */
class MyNotificationListenerService : NotificationListenerService(), TextToSpeech.OnInitListener {


    override fun onCreate() {
        Log.d(tagLog, "fun MyNotificationListenerService.onCreate()")
        super.onCreate()
        // 在这里不初始化 TextToSpeech，延迟到 onStartCommand
        // textToSpeech = TextToSpeech(this, this)
        Log.d(tagLog, "endfun MyNotificationListenerService.onCreate()")

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(tagLog, "fun onStartCommand()")
        // 创建通知渠道 (仅适用于 Android 8.0 及以上版本)
        newNotificationChannel()

        // 显示前台通知
        //  startForeground(1, buildNotification())
        startForeground(1, newNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)


        iniTTS()

        Log.d(tagLog, "endfun onStartCommand()")

        //START_STICKY：服务被终止时，系统会尝试重新启动它，并传递 null 的 Intent。
        //START_REDELIVER_INTENT：服务被终止时，系统会重新启动它，并且会再次传递之前的 Intent。
        return START_REDELIVER_INTENT
    }

    var textToSpeech: TextToSpeech? = null
    private fun initializeTextToSpeech(funx: (textToSpeech22: TextToSpeech?) -> Unit) {

        Log.d(tagLog, "\n\n\n")
        Log.d(tagLog, "fun iniTTS()")


        textToSpeech = TextToSpeech(this, object : TextToSpeech.OnInitListener {
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
        Log.d(tagLog, "endfun iniTTS()")
    }


    private fun iniTTS() {

        Log.d(tagLog, "\n\n\n")
        Log.d(tagLog, "fun iniTTS()")


        textToSpeech = TextToSpeech(this, object : TextToSpeech.OnInitListener {
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

    private fun newNotification(): Notification {
        return NotificationCompat.Builder(this, "your_channel_id")
            .setContentTitle("im2025")
            .setContentText("服务runing...." + getNow())
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }

    override fun onDestroy() {
        Log.d(tagLog, "fun onDestroy()")


        super.onDestroy()

        //  textToSpeech?.shutdown()
        Log.d(tagLog, "endfun onDestroy()")
    }

    val set2 = hashSetOf<String>() // 创建一个空的 HashSet

    /**
     * @SuppressLint("NewApi") 是一种用于抑制警告的注解，它告诉编译器在特定的代码中忽略关于 Android API 级别的警告。
     *
     * 解释 @SuppressLint("NewApi")
     * 在 Android 开发中，@SuppressLint 是一个用来抑制特定类型警告的注解。"NewApi" 是其中的一种警告类型，表示你的代码可能使用了针对较新版本 API 的功能，而这可能会导致在较旧版本的 Android 设备上不兼容或抛出异常。
     */
    @SuppressLint("NewApi")
    override fun onNotificationPosted(sbn: StatusBarNotification) {

//       if(!isTimeInDaytim())
//           return

        try {
            Log.d(tagLog, "fun onNotificationPosted(")
            Log.d(tagLog, "StatusBarNotification:" + encodeJson(sbn));
            Log.d(tagLog, "))");

            val notification = sbn.notification
            Log.d(tagLog, "notification:" + encodeJson(notification));

            val extras = notification.extras

            // 获取通知标题和内容
            val title = extras.getString("android.title") ?: "没有标题"
            var text = extras.getString("android.text") ?: "没有内容"
            text = text.replace("Starred Contacts", "");
            text = text.replace("星标联系人", "");


            var mesg = title + text;

            if (set4delp.contains(mesg))
                return;
            set4delp.add(mesg)




            // 拼接标题和.内容
            val deviceName2 = getDeviceName(this)

            //--------same dvc ingrn
            if(mesg.contains(deviceName2))
                return;


            var messageWzFmt = "标题=$title, 内容=$text ,dvc="+deviceName2;



            if (title == "没有标题" && text == "没有内容")
                return

            if (title == "" && text == "")
                return

            if (title.toLowerCase() == "timer")
                return

            if (title.toLowerCase() == "im2025")
                return
            if (title.contains("下午闹钟"))
                return
            if (title.contains("多个应用进行了敏感操作"))
                return
            if (title.contains("省电模式"))
                return
            if (title.contains("网络共享或热点"))
                return
            if (title.contains("正在通过USB充电"))
                return
            if (title.contains("正在连接到USB"))
                return
            if (title.contains("已连接到USB"))
                return
            if (title.contains("正在获取服务信息"))
                return
            if (containsAny2025(title, "闹钟 闹铃"))
                return
            if (containsAny2025("白资 百家乐 盈利 赌场 迪拜 反水 返水 盈利 佣金", messageWzFmt))
                return
            if (containsAny2025("黑U usdt USDT  高仿 虚拟币 反水 返水 盈利 佣金", messageWzFmt))
                return
            if (title.contains("输入法"))
                return
            if (containsAny2025("奸淫 父女 出轨 大片 人妻 乱伦", messageWzFmt))
                return
            if (containsAny2025("环境问题对接 产研 产研中心 救火", messageWzFmt))
                return
            if (containsAny2025(
                    "出境漫游  中国移动 中国联通 联通 美好的一天从收钱开始",
                    messageWzFmt
                )
            )
                return
            if (containsAny2025("正在备份照片  产研 产研中心 救火", messageWzFmt))
                return
            if (title.contains("360手机卫士"))
                return
            if (title.contains("短信") && title.contains("正在运行"))
                return
            if (title.startsWith("正在下载"))
                return
            if (containsAny2025("闹钟 响铃", messageWzFmt))
                return
            if (containsAny2025("降息 备用金 收钱提醒助手", messageWzFmt))
                return
            if (containsAny2025("特惠航线 特惠专场 旅行团 抢票 火车票 心动之旅", messageWzFmt))
                return
            if (containsAny2025("登录过期 备用金 ", messageWzFmt))
                return

            if (containsAny2025
                    ("热点 USB充电 USB调试 自动任务", messageWzFmt)
            )
                return

            if (title.startsWith("正在通过USB充电"))
                return

            if (title.startsWith("已连接到USB调试"))
                return

            if (isAllNumber(title) && title.length > 7 && text.isEmpty()) {
                //tel call

                val nmb22 = formatPhoneNumberForTTS(title)
                messageWzFmt = "标题: 电话$nmb22, 内容: $text"

            } else {
                //all english
                if (!isContainCjkChar(title) && (!isContainCjkChar(text)))
                    return
            }


            if ((!isContainCjkChar(title)) && (!isAllNumber(title))) {
                return
            }

            if (title == "Choose input method")
                return

            if (containsAny2025("姜育恒 歌曲 ", messageWzFmt))
                return


            //for xm12
            if (containsAny2025(messageWzFmt, "所有人 抽查  打卡 meet google")) {
                playNtfyMp3()
                //playAudio("/storage/emulated/0/Documents/Darin-Be What You Wanna Be HQ.mp3")
            }


            //todos  tts需要独立出去

            // 使用 TTS 阅读通知内容
            speakOut(messageWzFmt)

            sendTgTxtMsg(messageWzFmt)


            Thread(Runnable {
                sendNecho(messageWzFmt)
            }).start()
        } catch (e: Exception) {
            Log.e(tagLog, "Error onNotificationPosted(): ${e.message}")
        }
        Log.d(tagLog, "endfun onNotificationPosted()")

    }
    fun rdTxtV2(message: String) {



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


    //bot  .me/ntfycoll2025bot.
    //grp  ntfy mir coll
    private  val TOKEN = "8121736741:AAGKUS35nyeTodd7JXIb8u-sNQPN2sh6olw"
    private  val CHAT_ID = "-1002209160657" // 你的群组 ID
    private  val TELEGRAM_API_URL = "https://api.telegram.org/bot$TOKEN/sendMessage"

    /**
     * 调用telegrame bot api 发送文本消息
     */
    private fun sendTgTxtMsg(messageWzFmt: String) {

//        grpid=-1002209160657  //ntfy mir coll grp
//
//// 你的 Telegram 机器人 Token
//        const token =  "8121736741:AAGKUS35nyeTodd7JXIb8u-sNQPN2sh6olw"
//
//        。。。
        Thread {
            try {
                val url = URL(TELEGRAM_API_URL)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/json")

                // 构造 JSON 请求体
                val json = JSONObject().apply {
                    put("chat_id", CHAT_ID)
                    put("text", messageWzFmt)
                    put("parse_mode", "Markdown") // 可选: "HTML" 或 "Markdown"
                }

                // 发送请求
                val writer = OutputStreamWriter(conn.outputStream)
                writer.write(json.toString())
                writer.flush()
                writer.close()

                // 读取响应
                val responseCode = conn.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d("TelegramBot", "消息发送成功")
                } else {
                    Log.e("TelegramBot", "发送失败，状态码: $responseCode")
                }

                conn.disconnect()
            } catch (e: Exception) {
                Log.e("TelegramBot", "发生异常: ${e.message}")
            }
        }.start() // 在子线程中执行网络请求，避免阻塞主线程
    }


  //send tg replace
    @Deprecated("")
    private fun sendNecho(message: String) {
      // return;
        try {
            val deviceName2 = getDeviceName(this)
            val time = getTimestampInSecs()
            var msgid = encodeMd5(deviceName2 + message + time)
            val msg1obj = Msg(deviceName2, message, time, msgid)
            val encodeJson_msg = encodeJson(msg1obj)


            sendMsg(encodeJson_msg)
            write_row(this, msgid, encodeJson_msg);

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


    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // 可选：当通知被移除时执行操作
    }

    private fun speakOut(message: String) {
        Log.d(tagLog, "fun speakOut((" + message)
        Log.d(tagLog, "))")
        try {
            // 初始化 TextToSpeech，避免影响前台通知的启动
            // initializeTextToSpeech(rdTxt(message))

            rdTxtV2(message)
            //end  initializeTextToSpeech
            //  textToSpeech?.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)

        } catch (e: Exception) {
            Log.e(tagLog, "Error speakOut(): ${e.message}")
        }

        Log.d(tagLog, "endfun speakOut()")
    }


    override fun onInit(status: Int) {
        Log.d(tagLog, "fun onInit()")
        if (status == TextToSpeech.SUCCESS) {

            //textToSpeech?.language = Locale.CHINESE
        }
        Log.d(tagLog, "endfun onInit()")
    }
}
