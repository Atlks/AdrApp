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
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import comx.pkg.MainActivity.Msg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale


/**
 * ord
 * onCreate()..onStartCommand()
 */
class MyNotificationListenerService : NotificationListenerService(), TextToSpeech.OnInitListener {

    private var textToSpeech: TextToSpeech? = null

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


        // 初始化 TextToSpeech，避免影响前台通知的启动
        initializeTextToSpeech()// 执行你的业务逻辑

        Log.d(tagLog, "endfun onStartCommand()")

        //START_STICKY：服务被终止时，系统会尝试重新启动它，并传递 null 的 Intent。
        //START_REDELIVER_INTENT：服务被终止时，系统会重新启动它，并且会再次传递之前的 Intent。
        return START_REDELIVER_INTENT
    }

    private fun initializeTextToSpeech() {
        textToSpeech = TextToSpeech(this, object : TextToSpeech.OnInitListener {
            override fun onInit(status: Int) {
                if (status == TextToSpeech.SUCCESS) {
                    // 进行 TextToSpeech 的配置和调用
                    //success in xm135g..but fail in xm12
                    Log.d(tagLog, "TextToSpeech initialization success..")
                } else {
                    Log.d(tagLog, "TextToSpeech initialization failed with status code: $status")

                    //  Log.d(tagLog, "TextToSpeech initialization failed")
                }
            }
        })
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

        textToSpeech?.stop()
        textToSpeech?.shutdown()
        super.onDestroy()

        textToSpeech?.shutdown()
        Log.d(tagLog, "endfun onDestroy()")
    }


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
            Log.d(tagLog, "fun onNotificationPosted()")
            val notification = sbn.notification
            val extras = notification.extras

            // 获取通知标题和内容
            val title = extras.getString("android.title") ?: "没有标题"
            val text = extras.getString("android.text") ?: "没有内容"

            // 拼接标题和内容
            val message = "标题: $title, 内容: $text"

            if (title == "没有标题" && text == "没有内容")
                return

            if (title == "" && text == "")
                return

            if (title.toLowerCase() == "timer")
                return

            if (title.toLowerCase() == "im2025")
                return

            if (title.contains("360手机卫士"))
                return


            if (title == "Choose input method")
                return


            // 使用 TTS 阅读通知内容
            speakOut(message)


            val deviceName2 = getDeviceName(this)
            val time = getTimestampInSecs()
            var msgid = encodeMd5(deviceName2 + message + time)
            val msg1obj = Msg(deviceName2, message, time, msgid)
            val encodeJson_msg = encodeJson(msg1obj)


            sendMsg(encodeJson_msg)
            write_row(this, msgid, encodeJson_msg);

            //-----------block show list
            var smsList = ListSms()
            smsList = Fmtforeachx(smsList)
            //order by sendtime
            Log.d(tagLog, "smslist.size:" + smsList.size)
            // binding.textView.text = "cnt:" + smsList.size
            //goto main thrd updt ui
            // 切换到主线程更新 UI
            var ma: MainActivity = AppCompatActivity1main as MainActivity
            ma.runOnUiThread {
                ma.bindData2Table(smsList);
                //滚动到底部
                scrToButtom(ma.binding.scrvw)
            }
        } catch (e: Exception) {
            Log.e(tagLog, "Error onNotificationPosted(): ${e.message}")
        }
        Log.d(tagLog, "endfun onNotificationPosted()")

    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // 可选：当通知被移除时执行操作
    }

    private fun speakOut(message: String) {
        Log.d(tagLog, "fun speakOut()")
        try {
            // 动态设置语言
            val language =  Locale.CHINESE ;//detectLanguage(message)
            // textToSpeech?.language = language
            // 这里可以设置语言、语速等
            val languageResult = textToSpeech?.setLanguage(language)


            // 检查语言是否支持
            if (languageResult == TextToSpeech.LANG_MISSING_DATA || languageResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.d(tagLog, "Language is missing or not supported, attempting to handle this.")
                // 你可以提示用户安装语言包，或者选择其他可用语言
            } else {
                Log.d(tagLog, "Language supported.")
            }
            // 使用 TTS 阅读
            textToSpeech?.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)


            //  textToSpeech?.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)

        } catch (e: Exception) {
            Log.e(tagLog, "Error speakOut(): ${e.message}")
        }

        Log.d(tagLog, "endfun speakOut()")
    }


    override fun onInit(status: Int) {
        Log.d(tagLog, "fun onInit()")
        if (status == TextToSpeech.SUCCESS) {

            textToSpeech?.language = Locale.CHINESE
        }
        Log.d(tagLog, "endfun onInit()")
    }
}
