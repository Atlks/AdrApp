package comx.pkg

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun nowUtc8(): String {
    val now = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"))

    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssZ")

    return now.format(formatter)
}


//val logsDir = File(
//    getExternalFilesDir(null),
//    "logs"
//)
//
//logsDir.mkdirs()
//
//val logFile = File(logsDir, "notification.log")