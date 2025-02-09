package comx.pkg

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class SQLiteOpenHelper88(context: Context, DATABASE_NAME: String, tableName1: String) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    var tbNm88 = tableName1
    val COLUMN_K = "k"
    val COLUMN_V = "v"

    companion object {
        //  private const val DATABASE_NAME = DATABASE_NAME2
        private const val DATABASE_VERSION = 1


    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $tbNm88 (
                
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
fun write_rowV88(k: String, v: String, db: SQLiteDatabase, tbnm: String): Long {
    Log.d(tagLog, "fun write_rowV2((")
    Log.d(tagLog, "k=" + k)
    Log.d(tagLog, "v=" + v)
    Log.d(tagLog, ")))")

    //  val dbHelper = UtilDbSqlt(context)
    //  val db = dbHelper.writableDatabase

    val values = ContentValues().apply {
        put(SQLiteOpenHelper1.COLUMN_K, k)
        put(SQLiteOpenHelper1.COLUMN_V, v)


    }

    // 插入数据
    val rowId = db.insert(tbnm, null, values)
    //  db.close()
    Log.d(tagLog, "endfun write_rowV2()#ret rowId=" + rowId)
    return rowId
}

//, tbNm: String
fun getAllrowsV88(db: SQLiteDatabase, tbnm: String): List<KVrow> {
    // val dbHelper = UtilDbSqlt(context)
    //  val db = dbHelper.readableDatabase
    val messageList = mutableListOf<KVrow>()

    val cursor = db.query(
        tbnm, // 表名
        arrayOf( // 要查询的列
            SQLiteOpenHelper1.COLUMN_K,
            SQLiteOpenHelper1.COLUMN_V

        ),
        null, // where 子句
        null, // where 参数
        null, // groupBy
        null, // having
        "" // 按时间升序排序
    )

    with(cursor) {
        while (moveToNext()) {
            val k = getString(getColumnIndexOrThrow(SQLiteOpenHelper1.COLUMN_K))
            val v = getString(getColumnIndexOrThrow(SQLiteOpenHelper1.COLUMN_V))

            // 将结果添加到列表中
            messageList.add(KVrow(k, v))
        }
        close()
    }
    //db.close()
    return messageList
}


//, tbnm: String


/**
 *
 */
fun del_row88(id: String,   db: SQLiteDatabase, tbnm: String): Int {
    Log.d(tagLog, "fun del_row((")
    Log.d(tagLog, "k=" + id)
    Log.d(tagLog, "tbNm=" + tbnm)
    Log.d(tagLog, ")))")

    //  val dbHelper = UtilDbSqlt(context)
    //  val db = dbHelper.writableDatabase


    //

    val rowId = db.delete(tbnm, "k = ?", arrayOf(id))
    //   db.close()
    Log.d(tagLog, "endfun del_row()#ret rowId=" + rowId)
    return rowId
}
