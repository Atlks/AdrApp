package comx.pkg

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteOpenHelper2(context: Context, DATABASE_NAME: String ) :
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

