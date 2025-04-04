package comx.pkg

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


//UtilDbSqltV2
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

