package comx.pkg

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class SQLiteOpenHelper1(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "chat5.db"
        private const val DATABASE_VERSION = 1

        // 表名和字段
        const val tableName = "tbl1"

        const val COLUMN_K = "k"
        const val COLUMN_V = "v"

    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $tableName (
                
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

//---------------util db sqlt.funs ------
/**
 * for mysql store engr api spec...read_row()
 */
fun write_row(context: Context, k: String, v: String ): Long {
    Log.d(tagLog, "fun write_row((")
    Log.d(tagLog, "k="+k)
    Log.d(tagLog, "v="+v)
    Log.d(tagLog, ")))")

    val dbHelper = SQLiteOpenHelper1(context)
    val db = dbHelper.writableDatabase

    val values = ContentValues().apply {
        put(SQLiteOpenHelper1.COLUMN_K, k)
        put(SQLiteOpenHelper1.COLUMN_V, v)


    }

    // 插入数据
    val rowId = db.insert(SQLiteOpenHelper1.tableName, null, values)
    //db.close()
    Log.d(tagLog, "endfun write_row()#ret rowId="+rowId)
    return rowId
}


fun write_row( db: String,k: String, v: String ,context: Context): Long {
    Log.d(tagLog, "fun write_row((")
    Log.d(tagLog, "db="+db+",k="+k+",v="+v)

    Log.d(tagLog, ")))")

    val dbHelper = SQLiteOpenHelper1(context)
    val db = dbHelper.writableDatabase

    val values = ContentValues().apply {
        put(SQLiteOpenHelper1.COLUMN_K, k)
        put(SQLiteOpenHelper1.COLUMN_V, v)


    }

    // 插入数据
    val rowId = db.insert(SQLiteOpenHelper1.tableName, null, values)
    //db.close()
    Log.d(tagLog, "endfun write_row()#ret rowId="+rowId)
    return rowId
}

data class KVrow(
    val k: String,
    val v: String

)


fun getAllrows(context: Context): List<KVrow> {
    val dbHelper = SQLiteOpenHelper1(context)
    val db = dbHelper.readableDatabase
    val messageList = mutableListOf<KVrow>()

    val cursor = db.query(
        SQLiteOpenHelper1.tableName, // 表名
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
            messageList.add(KVrow(k, v ))
        }
        close()
    }
    //db.close()
    return messageList
}
