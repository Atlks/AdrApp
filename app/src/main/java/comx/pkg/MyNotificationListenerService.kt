package comx.pkg


import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.aaapkg.R
import lib.Notify2025
import lib.containsAny2025
import lib.context8
import lib.deviceName
import lib.iniTTS
import lib.sendMsgTg
import lib.sendNecho
import lib.speakOut
import java.io.File


/**
 * ord
 * onCreate()..onStartCommand()
 */
class MyNotificationListenerService : NotificationListenerService(), TextToSpeech.OnInitListener {


    override fun onCreate() {
        Log.d(tagLog, "fun MyNotificationListenerService.onCreate()")
        super.onCreate()
        deviceName = getDeviceName(this)
        context8 = this;
        // 在这里不初始化 TextToSpeech，延迟到 onStartCommand
        // textToSpeech = TextToSpeech(this, this)
        Log.d(tagLog, "endfun MyNotificationListenerService.onCreate()")

    }


    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        requestRebind(ComponentName(this, MyNotificationListenerService::class.java))
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


        iniTTS(this)

        Log.d(tagLog, "endfun onStartCommand()")

        //START_STICKY：服务被终止时，系统会尝试重新启动它，并传递 null 的 Intent。
        //START_REDELIVER_INTENT：服务被终止时，系统会重新启动它，并且会再次传递之前的 Intent。
        return START_REDELIVER_INTENT
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
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

   // val set2 = hashSetOf<String>() // 创建一个空的 HashSet
//  package com.aaapkg
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
        val tagLog6 = "onNotificationPostedFunTag"
        try {
            Log.d(tagLog6, "\n\n\n")
            Log.d(tagLog6, "\n\n\n")
            Log.d(tagLog6, "\n\n\n")
            Log.d(tagLog6, "fun onNotificationPosted(")
            Log.d(tagLog6, "StatusBarNotification:" + encodeJson(sbn));
            Log.d(tagLog6, "))");

            val notification = sbn.notification
            Log.d(tagLog6, "notification:" + encodeJson(notification));
            val postTime = sbn.postTime // 通知的时间戳
            val packageName = sbn.packageName // 应用包名，例如 "com.whatsapp"
            val extras = notification.extras

            // 获取通知标题和内容
            val title = extras.getString("android.title") ?: "没有标题"
            var text = extras.getString("android.text") ?: "没有内容"
            text = text.replace("Starred Contacts", "");
            text = text.replace("星标联系人", "");
            // val deviceName = getDeviceName(this)
            Log.d(tagLog6, "title:${title}");

            Log.d(tagLog6, "text:${text}"  );



            var mesg = title + text;

            var messageWzFmt6 = "标题$title, 内容$text ";

            if (text.startsWith("Avg."))
            {
                Log.d(tagLog6, " accubatry msg,ingore ")
                Log.d(tagLog6, "endfun onNotificationPosted()")
                return;
            }



            //===========================dont sent dulp
            if (set4delp.contains(mesg))
            {
                Log.d(tagLog6, " dulp msg ")
                Log.d(tagLog6, "endfun onNotificationPosted()")
                return;
            }else{
                set4delp.add(mesg)
            }




            // 拼接标题和.内容
            val deviceName2 = getDeviceName(this)

            //--------same dvc ingrn
            if (deviceName2.length>3&& mesg.contains(deviceName2))
            {


                Log.d(tagLog6, " same dvc ")
                //not
                Log.d(tagLog6, "endfun onNotificationPosted()")

                return
            }

            //write to dsk
            val ntfy1 = Notify2025(title, text, postTime, packageName, deviceName);


            val dir = File("/storage/emulated/0/Download/aNtfy")

            if (!dir.exists()) {
                dir.mkdirs()
            }

            val f = File(
                dir,
                "notify${nowUtc8()}.log"
            )
//            val f: File = File("/storage/emulated/0/Download/aNtfy/notify"+nowUtc8()+".log"
//            )
//            f.mkdirs();
           //  f.mkdir();
            Log.d(tagLog6, "f_path623:"+f.path)
            f.writeText(encodeJson(ntfy1));

            // ============        =only spk scot
            if(text.toLowerCase().contains("scot") )
            {
                if(messageWzFmt6.toLowerCase().contains("tenxun-hk-pro-dc-db-doris"))
                {
                    //not spk
                }else
                {
                    var  messageWzFmt4readSpk = messageWzFmt6.take(50)
                    speakOut(messageWzFmt4readSpk)
                }

            }


            //=====================send tg n  myIm


            var messageWzFmt = "标题$title, 内容$text ,device=" + deviceName2;

//            if(mesg.toLowerCase().contains("scot") )
//            {   speakOut(messageWzFmt)
//            }

//            else
//            {
//                Log.d(tagLog6, " not cotain Scot ")
//                Log.d(tagLog6, "endfun onNotificationPosted()")
//
//                return
//            }


            if (chkNotOk(title, text))
            {

                Log.d(tagLog6, " not chkpass (msg),maybe grab txt ")
                Log.d(tagLog6, "endfun onNotificationPosted()")

                return
            }

            if (chkfltNotOk(messageWzFmt))
            {
                Log.d(tagLog6, "   cotain grab word ")
                Log.d(tagLog6, "endfun onNotificationPosted()")

                return
            }



            var messageWzFmt4readSpk = messageWzFmt
            if (isAllNumber(title) && title.length > 7 && text.isEmpty()) {
                //tel call

                val nmb22 = formatPhoneNumberForTTS(title)
                messageWzFmt4readSpk = "标题: 电话$nmb22, 内容: $text"

            } else {
                //all english
                if (!isContainCjkChar(title) && (!isContainCjkChar(text)))
                {
                    Log.d(tagLog6, "  not  cotain cjk char")
                    Log.d(tagLog6, "endfun onNotificationPosted()")
                    return
                }

            }


            //for xm12
            if (containsAny2025(messageWzFmt, "所有人 抽查  打卡 scot")) {
                playNtfyMp3()
                //playAudio("/storage/emulated/0/Documents/Darin-Be What You Wanna Be HQ.mp3")
            }


            //todos  tts需要独立出去

            // 使用 TTS 阅读通知内容  tel fmt need fmt
            //kotlin ,截取字符串前200个字符。如果不足200个，则不截取
            //防止过长消息阅读 ad msg
            messageWzFmt4readSpk = messageWzFmt4readSpk.take(200)
          //  speakOut(messageWzFmt4readSpk)

            sendMsgTg(messageWzFmt)


            Thread(Runnable {
                //here noly log ,not show refesh ui,too slow every time,
                //maybe timer refresh is better
                sendNecho(messageWzFmt)
            }).start()
        } catch (e: Exception) {
            Log.e(tagLog6, "Error onNotificationPosted(): ${e.message}")
        }
        Log.d(tagLog6, "endfun onNotificationPosted()")

    }




    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // 可选：当通知被移除时执行操作
    }


    override fun onInit(status: Int) {
        Log.d(tagLog, "fun onInit()")
        if (status == TextToSpeech.SUCCESS) {

            //textToSpeech?.language = Locale.CHINESE
        }
        Log.d(tagLog, "endfun onInit()")
    }
}
