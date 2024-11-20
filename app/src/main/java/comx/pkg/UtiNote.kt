package comx.pkg



import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.google.gson.Gson
import java.io.File

// 数据类定义
data class Note(
    val id: String,
    val title: String,
    val content: String,
    val timestamp: Long
)

fun exportSystemNotesToJson(context: Context, fileName: String = "system_notes.json"): String? {
    // 假设系统记事本的 ContentProvider URI
    val notesUri: Uri = Uri.parse("content://com.android.notes/notes") // 请根据实际情况替换 URI

    // 查询数据
    val cursor: Cursor? = context.contentResolver.query(
        notesUri,
        arrayOf("id", "title", "content", "timestamp"), // 需要的字段
        null,
        null,
        null
    )

    cursor?.use {
        val notes = mutableListOf<Note>()

        // 遍历游标
        while (it.moveToNext()) {
            val id = it.getString(it.getColumnIndexOrThrow("id"))
            val title = it.getString(it.getColumnIndexOrThrow("title"))
            val content = it.getString(it.getColumnIndexOrThrow("content"))
            val timestamp = it.getLong(it.getColumnIndexOrThrow("timestamp"))

            notes.add(Note(id, title, content, timestamp))
        }

        // 转换为 JSON
        val gson = Gson()
        val jsonString = gson.toJson(notes)

        // 将 JSON 写入文件
        wrtFilToDocumentDir(context,jsonString,"/notedir/",fileName)

        return "documents/notedir/"+fileName
    }

    // 如果没有查询到数据
    return null
}
