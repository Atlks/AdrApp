package comx.pkg

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class UtilDbSqltV2(context: Context, DATABASE_NAME: String) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {



      val COLUMN_K = "k"
      val COLUMN_V = "v"

    companion object {
        //  private const val DATABASE_NAME = DATABASE_NAME2
        private const val DATABASE_VERSION = 1



    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $tbNm (
                
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

// 表名和字段
  val tbNm = "tab1"

/**
 * for mysql store engr api spec...read_row()
 */
fun write_rowV2(k: String, v: String, db: SQLiteDatabase): Long {
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
    val rowId = db.insert(tbNm, null, values)
  //  db.close()
    Log.d(tagLog, "endfun write_rowV2()#ret rowId=" + rowId)
    return rowId
}


/**
 *
 */
fun del_row(id: String,   db: SQLiteDatabase): Int {
    Log.d(tagLog, "fun del_row((")
    Log.d(tagLog, "k=" + id)
    Log.d(tagLog, "tbNm=" + tbNm)
    Log.d(tagLog, ")))")

    //  val dbHelper = UtilDbSqlt(context)
    //  val db = dbHelper.writableDatabase


    //

    val rowId = db.delete(tbNm, "k = ?", arrayOf(id))
 //   db.close()
    Log.d(tagLog, "endfun del_row()#ret rowId=" + rowId)
    return rowId
}

//, tbNm: String
fun getAllrowsV2(  db: SQLiteDatabase): List<KVrow> {
    // val dbHelper = UtilDbSqlt(context)
    //  val db = dbHelper.readableDatabase
    val messageList = mutableListOf<KVrow>()

    val cursor = db.query(
        tbNm, // 表名
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
