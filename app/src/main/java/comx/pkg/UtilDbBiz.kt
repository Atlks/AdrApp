package comx.pkg

import android.content.Context
import comx.pkg.MainActivity.Msg

fun insertDB(context: Context, deviceName: String, msg: String) {
  //  val deviceName = deviceName // 设备名称，可以通过 Settings.Secure 或 Build.MODEL 获取
    // val msg = msg// 消息内容
    val time = getCurrentTimestampInSeconds() // 当前时间戳
    var msgid= encodeMd5(deviceName+msg+time)
     val sms = Msg(deviceName, msg, time, msgid)

    var v= encodeJson(sms)


    val rowId = insertRow(context, msgid, v)
    if (rowId != -1L) {
        println("消息插入成功，rowID: $rowId")
    } else {
        println("消息插入失败")
    }
}
