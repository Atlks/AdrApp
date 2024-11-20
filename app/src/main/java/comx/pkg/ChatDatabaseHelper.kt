package comx.pkg

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ChatDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "chat2.db"
        private const val DATABASE_VERSION = 1

        // 表名和字段
        const val TABLE_MESSAGES = "msgTbl"
        const val COLUMN_ID = "id"
        const val COLUMN_DEVICE_NAME = "deviceName"
        const val COLUMN_MESSAGE = "msg"
        const val COLUMN_TIME = "time"
        const val COLUMN_msgidUnq = "msgidUnq"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_MESSAGES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_DEVICE_NAME TEXT,
                $COLUMN_MESSAGE TEXT,
                $COLUMN_TIME INTEGER,
               $COLUMN_msgidUnq TEXT NOT NULL UNIQUE
            )
        """
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
       // db.execSQL("DROP TABLE IF EXISTS $TABLE_MESSAGES")
       // onCreate(db)
    }



}
fun insertMessage(context: Context, deviceName: String, msg: String, time: Long): Long {
    val dbHelper = ChatDatabaseHelper(context)
    val db = dbHelper.writableDatabase

    val values = ContentValues().apply {
        put(ChatDatabaseHelper.COLUMN_DEVICE_NAME, deviceName)
        put(ChatDatabaseHelper.COLUMN_MESSAGE, msg)
        put(ChatDatabaseHelper.COLUMN_TIME, time)
        put(ChatDatabaseHelper.COLUMN_msgidUnq, encodeMd5(ChatDatabaseHelper.COLUMN_DEVICE_NAME+ChatDatabaseHelper.COLUMN_MESSAGE+ChatDatabaseHelper.COLUMN_TIME))

    }

    // 插入数据
    val rowId = db.insert(ChatDatabaseHelper.TABLE_MESSAGES, null, values)
    db.close()
    return rowId
}

data class Message(
    val deviceName: String,
    val msg: String,
    val time: Long,
    var id:Long
)


fun getAllMessages(context: Context): List<Message> {
    val dbHelper = ChatDatabaseHelper(context)
    val db = dbHelper.readableDatabase
    val messageList = mutableListOf<Message>()

    val cursor = db.query(
        ChatDatabaseHelper.TABLE_MESSAGES, // 表名
        arrayOf( // 要查询的列
            ChatDatabaseHelper.COLUMN_DEVICE_NAME,
            ChatDatabaseHelper.COLUMN_MESSAGE,
            ChatDatabaseHelper.COLUMN_TIME,
            ChatDatabaseHelper.COLUMN_ID
        ),
        null, // where 子句
        null, // where 参数
        null, // groupBy
        null, // having
        "${ChatDatabaseHelper.COLUMN_TIME} ASC" // 按时间升序排序
    )

    with(cursor) {
        while (moveToNext()) {
            val deviceName = getString(getColumnIndexOrThrow(ChatDatabaseHelper.COLUMN_DEVICE_NAME))
            val msg = getString(getColumnIndexOrThrow(ChatDatabaseHelper.COLUMN_MESSAGE))
            val time = getLong(getColumnIndexOrThrow(ChatDatabaseHelper.COLUMN_TIME))
            val id = getLong(getColumnIndexOrThrow(ChatDatabaseHelper.COLUMN_ID))
            // 将结果添加到列表中
            messageList.add(Message(deviceName, msg, time,id))
        }
        close()
    }
    db.close()
    return messageList
}
