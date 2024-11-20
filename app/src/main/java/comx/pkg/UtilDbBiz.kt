package comx.pkg

import android.content.Context

fun insertDB(context: Context, deviceName: String, msg: String) {
  //  val deviceName = deviceName // 设备名称，可以通过 Settings.Secure 或 Build.MODEL 获取
    // val msg = msg// 消息内容
    val time = System.currentTimeMillis() // 当前时间戳

    val rowId = insertMessage(context, deviceName, msg, time)
    if (rowId != -1L) {
        println("消息插入成功，ID: $rowId")
    } else {
        println("消息插入失败")
    }
}
