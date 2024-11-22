package comx.pkg

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class ChatDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "chat5.db"
        private const val DATABASE_VERSION = 1

        // 表名和字段
        const val TABLE_MESSAGES = "tbl1"

        const val COLUMN_K = "k"
        const val COLUMN_V = "v"

    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_MESSAGES (
                
                $COLUMN_K TEXT PRIMARY KEY ,
                $COLUMN_V TEXT
               
            )
        """
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
       // db.execSQL("DROP TABLE IF EXISTS $TABLE_MESSAGES")
       // onCreate(db)
    }



}

/**
 * for mysql store engr api spec...read_row()
 */
fun write_row(context: Context, k: String, v: String ): Long {
    Log.d(tagLog, "fun write_row((")
    Log.d(tagLog, "k="+k)
    Log.d(tagLog, "v="+v)
    Log.d(tagLog, ")))")

    val dbHelper = ChatDatabaseHelper(context)
    val db = dbHelper.writableDatabase

    val values = ContentValues().apply {
        put(ChatDatabaseHelper.COLUMN_K, k)
        put(ChatDatabaseHelper.COLUMN_V, v)


    }

    // 插入数据
    val rowId = db.insert(ChatDatabaseHelper.TABLE_MESSAGES, null, values)
    db.close()
    Log.d(tagLog, "endfun write_row()#ret rowId="+rowId)
    return rowId
}

data class KVrow(
    val k: String,
    val v: String

)


fun getAllrows(context: Context): List<KVrow> {
    val dbHelper = ChatDatabaseHelper(context)
    val db = dbHelper.readableDatabase
    val messageList = mutableListOf<KVrow>()

    val cursor = db.query(
        ChatDatabaseHelper.TABLE_MESSAGES, // 表名
        arrayOf( // 要查询的列
            ChatDatabaseHelper.COLUMN_K,
            ChatDatabaseHelper.COLUMN_V

        ),
        null, // where 子句
        null, // where 参数
        null, // groupBy
        null, // having
        "" // 按时间升序排序
    )

    with(cursor) {
        while (moveToNext()) {
            val k = getString(getColumnIndexOrThrow(ChatDatabaseHelper.COLUMN_K))
            val v = getString(getColumnIndexOrThrow(ChatDatabaseHelper.COLUMN_V))

            // 将结果添加到列表中
            messageList.add(KVrow(k, v ))
        }
        close()
    }
    db.close()
    return messageList
}
