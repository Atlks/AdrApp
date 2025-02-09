package comx.pkg

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log

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



/**
 *
 */
fun del_row88(id: String,   db: SQLiteDatabase): Int {
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