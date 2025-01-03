package comx.pkg

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

lateinit var appContext: Context
lateinit var AppCompatActivity1main: AppCompatActivity

fun playNtfyMp3() {
    val funname = "playNtfyMp3"
    Log.d(tagLog, "fun ${funname}()")
    try {
        val sharedPreferences =
            AppCompatActivity1main.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        var uriString = sharedPreferences.getString("selected_file_uri", null)
        // uriString="content://com.android.externalstorage.documents/tree/primary%3ADocuments/document/primary%3ADocuments%2FDarin-Be%20What%20You%20Wanna%20Be%20HQ.mp3"
        Log.d(tagLog, "uriString=" + uriString)
        uriString?.let {
            val uri = Uri.parse(it)

            Log.d(tagLog, "uri=" + uri.toString())
//fileUri:content://com.android.externalstorage.documents/tree/primary%3ADocuments/document/primary%3ADocuments%2FDarin-Be%20What%20You%20Wanna%20Be%20HQ.mp3
            playMp3(AppCompatActivity1main, uri)
        }


    } catch (e: Exception) {
        // 处理异常
        Log.e(tagLog, "${funname}() Caught exception", e)
    }
    Log.d(tagLog, "endfun ${funname}()")

}
//        val fileDescriptor1 = context.contentResolver.openFileDescriptor(uri, "r")
//        if (fileDescriptor1 != null) {
//            Log.d(tagLog, "fileDescriptor= ${fileDescriptor1.fileDescriptor}")
//        }
fun playMp3(context: Context, uri: Uri) {
    val funname = "playMp3"
    Log.d(tagLog, "\n\n")
    Log.d(tagLog, "fun ${funname}(")
    Log.d(tagLog, "uri=" + uri)
    Log.d(tagLog, ")")
    try {

        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(context, uri)



        mediaPlayer.prepare() //  准备，

        // MediaPlayer.duration 是在音频文件准备好后才会返回有效的时长
        val duration = mediaPlayer.duration
        Log.d(tagLog, "duration=" + duration)
        if (duration <= 0) {
            Log.e(tagLog, "Audio file duration is invalid.")
        }
        mediaPlayer.start() // 在准备好后开始播放


    } catch (e: Exception) {
        Log.e(tagLog, "playAudio() Caught exception", e)


    }
    Log.d(tagLog, "endfun ${funname}()")

}

/**
 * context, YourService::class.java
 */
fun keeplive4FrgrdSvrs(context: Context, serviceClass: Class<*>) {
    val serviceIntent = Intent(context, serviceClass)
    ContextCompat.startForegroundService(context, serviceIntent)
}

fun checkTtsEngineAvailability() {
//    val availableEngines = TextToSpeech.Engine.availableEngines
//    if (availableEngines.isEmpty()) {
//        Log.e(tagLog, "No TTS engine available on this device")
//        // Handle the case where no engine is available
//    } else {
//        Log.d(tagLog, "TTS engine is available")
//        // Proceed with TTS initialization or handling missing data
//    }
}

fun scrToButtom(scrollView: ScrollView) {
    scrollView.post {
        scrollView.fullScroll(View.FOCUS_DOWN)
    }
}

fun keeplive2alarmManager(context: Context, serviceClass: Class<*>) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, serviceClass)
    val pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        System.currentTimeMillis(),
        AlarmManager.INTERVAL_HOUR,
        pendingIntent
    )
}

fun keeplive3JobScheduler(context: Context, serviceClass: Class<*>) {
    val jobId = 9898989
    val jobInfo = JobInfo.Builder(jobId, ComponentName(context, serviceClass))
        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
        .setPersisted(true) // 保持持久化
        .build()

    val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
    jobScheduler.schedule(jobInfo)
}


// 显示删除确认对话框,如果确定了，执行回调函数
fun showDeleteConfirmationDialog(context: Context, fun1: () -> Unit) {
    val dialog = AlertDialog.Builder(context)
        .setTitle("确认删除")
        .setMessage("确定要删除吗？")
        .setPositiveButton("确定") { dialog, which ->
            // 用户点击“确定”，调用删除方法
            // del()
            dialog.dismiss()
            //del smss
            fun1()
        }
        .setNegativeButton("取消") { dialog, which ->
            // 用户点击“取消”，什么都不做
            dialog.dismiss()
        }
        .create()

    dialog.show()
}


// 在服务中启动前台服务
class YourService : Service() {
    @SuppressLint("ForegroundServiceType")
    override fun onCreate() {
        super.onCreate()
        val notification = NotificationCompat.Builder(this, "YOUR_CHANNEL_ID")
            .setContentTitle("应用正在运行")
            .setContentText("应用正在进行通知 TTS 通报")
            // .setSmallIcon(R.drawable.ic_notification)
            .build()

        startForeground(1, notification) // 启动前台服务
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}

fun gotoNtfyUI(context: Context, packageName: String) {
    try {

        //例如 Build.VERSION_CODES.O 表示 Android 8.0（API 级别 26）。
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android 8.0 及以上版本
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, packageName) // 当前应用包名
                //  putExtra(Settings.EXTRA_CHANNEL_ID, "my_channel_id") // 如果有特定渠道 ID
            }
        } else {
            // Android 8.0 以下版本
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                // data = android.net.Uri.parse("package:$packageName") // 跳转到应用详情页
            }
        }

        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // 如果设备不支持对应的 Intent，提示用户
        Toast.makeText(context, "无法打开通知设置，请手动操作", Toast.LENGTH_LONG).show()
        e.printStackTrace()
    }
}

fun sendNotification2024(context: Context, title: String, message: String) {
    Log.d(tagLog, "fun sendNotification(")
    Log.d(tagLog, "tit=" + title)
    Log.d(tagLog, "))")

    val channelId = "my_channel_id"
    val notificationId = 1

    // 创建通知渠道 (仅适用于 Android 8.0 及以上版本)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelName = "My Notification Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = "This is a test notification channel."
        }
        // 注册通知渠道
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // 创建通知
    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.ic_dialog_info) // 设置小图标
        .setContentTitle(title) // 设置标题
        .setContentText(message) // 设置内容
        .setPriority(NotificationCompat.PRIORITY_DEFAULT) // 设置优先级
        .build()

    // 发送通知
    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notify(notificationId, notification)
    }
    Log.d(tagLog, "endfun sendNotification")
}