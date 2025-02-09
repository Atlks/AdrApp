package lib

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import android.widget.Toast
import comx.pkg.tagLog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.system.exitProcess

/**
 * todo
 * glb ex cptch 3lev...  process.thrd..corout
 */


// setDefaultUncaughtExceptionHandler4  Coroutine
fun setGlbExCaptch4crtn(): CoroutineExceptionHandler {
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e("CoroutineError", "协程异常: ", exception)
    }
    return coroutineExceptionHandler
}

lateinit var appContext1: Context
lateinit var packageManager: PackageManager
var packageName: String = ""


fun setDefaultUncaughtExceptionHandler4thrd(
    applicationContext: Context,
    packageManager2: PackageManager,
    packageName2: String
) {
    appContext1 = applicationContext
    packageManager = packageManager2
    packageName = packageName2

    // 设置全局异常处理器
    Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
        handleUncaughtException(thread, throwable)
    }
}


// 在协程中使用异常处理器
//CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
//    throw RuntimeException("协程发生错误")
//}


fun handleUncaughtException(thread: Thread, throwable: Throwable) {
    // 记录错误日志
    Log.e(tagLog, "未捕获异常: ", throwable)

    // 可以上传错误日志到服务器
    uploadCrashReport(throwable)

    // 显示 Toast 提示（需要在主线程中执行）
    Thread {
        Looper.prepare()
        Toast.makeText(appContext1, "程序发生异常，即将重启", Toast.LENGTH_LONG).show()
        Looper.loop()
    }.start()

    // 延迟 2 秒后，重启应用
    // todo enhs sttbtky
    Thread.sleep(15000)
    restartApp()

    // 结束进程
    exitProcess(0)
}


fun uploadCrashReport(throwable: Throwable) {
    // 在这里上传日志到服务器
}

fun restartApp() {

    val intent = packageManager.getLaunchIntentForPackage(packageName)
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    appContext1.startActivity(intent)
}