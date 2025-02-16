package comx.pkg

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.speech.tts.TextToSpeech

import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.aaapkg.R
import lib.containsAny2025


import android.util.Log
import lib.Notify2025
import lib.containsAll
import lib.context8
import lib.deviceName
import lib.iniTTS
import lib.sendMsgTg
import lib.sendNecho
import lib.speakOut

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
            val postTime = sbn.postTime // 通知的时间戳
            val packageName = sbn.packageName // 应用包名，例如 "com.whatsapp"
            val extras = notification.extras

            // 获取通知标题和内容
            val title = extras.getString("android.title") ?: "没有标题"
            var text = extras.getString("android.text") ?: "没有内容"
            text = text.replace("Starred Contacts", "");
            text = text.replace("星标联系人", "");
            // val deviceName = getDeviceName(this)
            val ntfy1 = Notify2025(title, text, postTime, packageName, deviceName);

            var mesg = title + text;

            if (set4delp.contains(mesg))
                return;
            set4delp.add(mesg)


            // 拼接标题和.内容
            val deviceName2 = getDeviceName(this)

            //--------same dvc ingrn
            if (mesg.contains(deviceName2))
                return;


            var messageWzFmt = "标题=$title, 内容=$text ,device=" + deviceName2;



            if (title == "没有标题" && text == "没有内容")
                return

            if (title == "" && text == "")
                return

            if (title.toLowerCase() == "timer")
                return

            if (title.toLowerCase() == "im2025")
                return


            if (title.contains("短信") && title.contains("正在运行"))
                return
            if (title.startsWith("正在下载"))
                return
            if (chkfltNotOk(messageWzFmt))
                return


            var messageWzFmt4readSpk = messageWzFmt
            if (isAllNumber(title) && title.length > 7 && text.isEmpty()) {
                //tel call

                val nmb22 = formatPhoneNumberForTTS(title)
                messageWzFmt4readSpk = "标题: 电话$nmb22, 内容: $text"

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

            if (containsAny2025(
                    "姜育恒 歌曲 醉酒歌 我的唇吻不到我爱的人 再见也是朋友 再见只是陌生人 女人的选择 世纪精选 漫漫人海我遇见了你",
                    messageWzFmt
                )
            )
                return


            //for xm12
            if (containsAny2025(messageWzFmt, "所有人 抽查  打卡 meet google")) {
                playNtfyMp3()
                //playAudio("/storage/emulated/0/Documents/Darin-Be What You Wanna Be HQ.mp3")
            }


            //todos  tts需要独立出去

            // 使用 TTS 阅读通知内容  tel fmt need fmt
            speakOut(messageWzFmt4readSpk)

            sendMsgTg(messageWzFmt)


            Thread(Runnable {
                //here noly log ,not show refesh ui,too slow every time,
                //maybe timer refresh is better
                sendNecho(messageWzFmt)
            }).start()
        } catch (e: Exception) {
            Log.e(tagLog, "Error onNotificationPosted(): ${e.message}")
        }
        Log.d(tagLog, "endfun onNotificationPosted()")

    }

    private fun chkfltNotOk(messageWzFmt: String): Boolean {

        if (containsAll("微信 功能 还剩",messageWzFmt))
            return true;

        if (containsAll("大麻 麻姑",messageWzFmt))
            return true;

        if (containsAll("免息   折起",messageWzFmt))
            return true;

        if (containsAny2025("省呗 预审成功 拒收请回复 usdt USDT  高仿 虚拟币 反水 返水 盈利 佣金", messageWzFmt))
            return true;

        if (containsAny2025("输入法 360手机卫士 黑U usdt USDT  高仿 虚拟币 反水 返水 盈利 佣金", messageWzFmt))
            return true;



        if (containsAny2025(
                "机票劵 优惠价 优惠劵 优惠升级卡  刮刮乐 公司内部频道 免单城市 购物满额 美团支付劵 财富路径 美好的节日里  定制贷款 大额产品 优惠活动 提醒您多次了  办卡优惠 以审为准 拒收回复 拒收请回复",
                messageWzFmt
            )
        )
            return true


        if (containsAny2025(
                "所有未接电话 全场景音乐服务  应用正在后台运行 下午闹钟 多个应用进行了敏感操作 省电模式 网络共享或热点 正在通过USB充电 正在连接到USB 已连接到USB 闹钟 闹铃 正在获取服务信息",
                messageWzFmt
            )
        )
            return true



        if (containsAny2025
                ("已连接到USB调试 正在通过USB充电 重要应用会自动更新 热点 USB充电 USB调试 自动任务", messageWzFmt)
        )
            return true

        if (containsAny2025("帝豪 阿梦 再次提醒", messageWzFmt))
            return true;


        if (containsAny2025("特惠航线 特惠专场 旅行团 抢票 火车票 心动之旅", messageWzFmt))
            return true;
        if (containsAny2025("登录过期  备用金 ", messageWzFmt))
            return true;
        if (containsAny2025("还款金 积分奖励 订单奖励 尊享 权益 特价 毗邻 日利率 ", messageWzFmt))
            return true;

        if (containsAny2025(
                " 幸运注单 新人首存 最高奖励 送不停 彩金专员 天降红包 白资 百家乐 盈利 赌场 迪拜 反水 返水 盈利 佣金",
                messageWzFmt
            )
        )
            return true;

        if (containsAny2025("闹钟 响铃 正在备份", messageWzFmt))
            return true;
        if (containsAny2025("降息 备用金 收钱提醒助手", messageWzFmt))
            return true;

        //cp sys tips
        if (containsAny2025("备份正在进行中", messageWzFmt))
            return true;


        if (containsAny2025("帝豪 奸淫 父女 出轨 大片 人妻 乱伦", messageWzFmt))
            return true
        if (containsAny2025("环境问题对接 产研 产研中心 救火", messageWzFmt))
            return true
        if (containsAny2025(
                "出境漫游  中国移动 中国联通 联通 美好的一天从收钱开始",
                messageWzFmt
            )
        )
            return true
        if (containsAny2025("正在备份照片  产研 产研中心 救火", messageWzFmt))
            return true

        return false;

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
