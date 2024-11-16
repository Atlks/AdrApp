package comx.pkg
import kotlinx.serialization.*
import kotlinx.serialization.json.*
//import comx.databinding.ActivityMainBinding
//import comx.pkg.databinding.ActivityMainBinding
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Telephony
import android.text.Editable
import android.util.Log
import android.widget.CheckBox
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.ui.AppBarConfiguration

import comx.pkg.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val REQUEST_SMS_PERMISSION = 7
    private val tagLog = "MainActivity1114"

    override fun onCreate(savedInstanceState: Bundle?) {

        try { Log.d(tagLog, "funx onCrt()")

            // 设置全局异常捕获
//            Thread.setDefaultUncaughtExceptionHandler { thread: Thread, throwable: Throwable? ->
//                // 这里可以做一些日志记录或错误上报
//                Log.e("UncaughtException", "Thread: " + thread.name, throwable)
//            }

            //            // 创建一个 Intent 对象，用于启动 SecondActivity
//            val intent = Intent(this, MmncActivity::class.java)
//            startActivity(intent)  // 启动 SecondActivity




            super.onCreate(savedInstanceState)

            // 初始化 ViewBinding
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            // setContentView(R.layout.activity_main)  // 确保这一行调用


            //----reg sms writefile auth
            //检查并请求权限
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_SMS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val permissions = arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_SMS,

                    )
                ActivityCompat.requestPermissions(
                    this,
                    permissions,
                    REQUEST_SMS_PERMISSION
                )
            }
            // 检查是否已获得写入外部存储权限
            //requestCode 是自定义的整数，用于在权限回调中识别请求。
//        var requestCode4wrt = 111
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//            != PackageManager.PERMISSION_GRANTED
//        ) {
//
//            // 如果没有权限，则请求权限
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                requestCode4wrt
//            )
//        }


            binding.srchBtn.setOnClickListener {
                // 创建一个 Intent 对象，用于启动 SecondActivity
                try {
                    var smsList = searchSms(binding.txtbx1.text)
                    Log.d(tagLog, "smslist.size:" + smsList.size)
                    binding.textView.text = "cnt:" + smsList.size
                    showList(smsList);

                } catch (e: Exception) {
                    Log.e(tagLog, "Error while searching SMS or showing list", e)
                }
            }

            binding.exptBtn.setOnClickListener {

                try {
                    var f = exportAllSms(applicationContext)
                    // val toast = Toast.makeText(this, "save file:"+f, Toast.LENGTH_LONG)
                    //  toast.show()
                    showToast(this, "save file:" + f, 15)
                } catch (e: Exception) {
                    Log.e(tagLog, "Error while searching SMS or showing list", e)
                }

            }


            // 获取 CheckBox 的选中状态并打印到日志中
            binding.delBtn.setOnClickListener {
                showDeleteConfirmationDialog()

            }


            // val intent = Intent(this, SecondActivity::class.java)
            // startActivity(intent)

//end   设置点击事件监听器

            // 初始化数据
            // initData()


            //   showList()


            //ssss 设置按钮点击事件，跳转到 SecondActivity 页面
            val gotoFormButton = binding.gotoFormButton
            gotoFormButton.setOnClickListener {
                // 创建一个 Intent 对象，用于启动 SecondActivity
                val java = MmncActivity::class.java
                val intent = Intent(this, java)
                startActivity(intent)  // 启动 SecondActivity

            }

            Log.d(tagLog,getDvcId())
            Log.d(tagLog, "endfun onCrt()")

        } catch (e: Exception) {
            // 处理异常
            Log.e(tagLog, "Caught exception", e)
        }

    }


    /**
     * 导出所有短信到   /sms_export.json文件
     * applicationContext
     */
    fun exportAllSms(context: Context): String {

        val dvcId = getDvcId()

        val contentResolver: ContentResolver = context.contentResolver
        val smsUri = Telephony.Sms.CONTENT_URI
        val cursor = contentResolver.query(smsUri, null, null, null, null)

        val smsArray = JSONArray()

        cursor?.use {
            while (it.moveToNext()) {
                val smsObject = JSONObject()
                // Telephony.Sms.
                smsObject.put("id", it.getString(it.getColumnIndexOrThrow(Telephony.Sms._ID)))
                smsObject.put(
                    "address",
                    it.getString(it.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                )
                smsObject.put("body", it.getString(it.getColumnIndexOrThrow(Telephony.Sms.BODY)))
                val date = it.getString(it.getColumnIndexOrThrow(Telephony.Sms.DATE))
                smsObject.put("date", date)
                smsObject.put("datestr",formatTimestamp(toLongx(date)) )



                smsObject.put("dvc", dvcId)
                smsArray.put(smsObject)
            }
        }

        Log.d(tagLog, "sms size:" + smsArray.length())
        ///aasms
        val jsonString = smsArray.toString(4)
//        val f = "/storage/emulated/0/sms_export" + getFileNameWithCurrentTime() + ".json"
//        mkdir2024(f)
//
//

//
//        val directory = File("/storage/emulated/0/aasms")
//        if (!directory.exists()) {
//            directory.mkdirs()  // 创建目录
//        }

//        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
//            addCategory(Intent.CATEGORY_OPENABLE)
//            type = "application/json"
//            putExtra(Intent.EXTRA_TITLE, "sms_export.json")
//        }
//
//        startActivityForResult(intent, REQUEST_CREATE_FILE)
        // val file = wrtFilToAppdir(context, jsonString)

        var fname = "smsExpt.${getDvcIdFlFrg()}." + getFileNameWzTime4FlNmFrg() + ".json";
        val fldr = "/aasms/"
        val file = wrtFilToDocumentDir(context, jsonString, fldr, fname)



        Log.d(tagLog, "expt ok..." + file.toString())
        return "document" + fldr + fname;
    }

    private fun toLongx(numStr: String?): Long {
        // 如果 numStr 为空或者无法转换为 Long，则返回 0 或其他默认值
        return numStr?.toLongOrNull() ?: 0L
    }


    /**
     * 使用 MediaStore 存储文件到公共目录
     */
    private fun wrtFilToDocumentDir(
        context: Context,
        jsonString: String?,
        fldr: Any?,
        fileName: String
    ): Any {
        // 检查传入的 jsonString 是否为空
        if (jsonString.isNullOrEmpty()) {
            Log.e("wrtFilToDocumentDir", "JSON string is empty or null")
            return "Invalid input: JSON string is null or empty"
        }

        try {
            // 获取 ContentResolver
            val contentResolver = context.contentResolver

            // 设置文件名和 MIME 类型
            //   val fileName = "sms_export.json"
            val mimeType = "application/json"

            // 创建 ContentValues
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName) // 文件名
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)    // MIME 类型
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Documents" + fldr)  // 文件存储路径
            }

            // 使用 MediaStore 创建文件
            val uri =
                contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
                    ?: return "Failed to create file in MediaStore"

            // 打开输出流写入文件内容
            val outputStream: OutputStream? = uri?.let {
                contentResolver.openOutputStream(it)
            }

            outputStream?.use {
                val writer = OutputStreamWriter(it)
                writer.write(jsonString)
                writer.flush()
            }

            return "File successfully saved to Documents/aasms/$fileName"
        } catch (e: Exception) {
            Log.e("wrtFilToDocumentDir", "Error saving file: ${e.message}")
            return "Error saving file: ${e.message}"
        }
    }

    private fun wrtFilToAppdir(context: Context, jsonString: String): File {
        // 获取外部存储根目录  or doc dir..
        val fldr = "aasms"
        // val rootDir = File(Environment.getExternalStorageDirectory(), fldr)
        val rootDir = File(context.getExternalFilesDir(null), fldr)

        if (!rootDir.exists()) {
            rootDir.mkdirs() // 创建文件夹
        }
        // 定义文件路径和内容
        val file = File(rootDir, "sms_export.json")

        // val file = File(context.getExternalFilesDir(null), f)
        //val file = File(f);
        FileOutputStream(file).use { outputStream ->
            outputStream.write(jsonString.toByteArray())
        }
        return file
    }


    /**
     * 根据传入的sms id删除指定的短信
     */
    private fun delSms(context: Context, smsid: CharSequence?) {
        Log.d(tagLog, "fun delsms(" + smsid)
        Log.d(tagLog, ")))")
        // 检查 smsid 是否为空
        if (smsid.isNullOrEmpty()) {
            Toast.makeText(context, "短信ID无效", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            // 获取 ContentResolver
            val contentResolver: ContentResolver = context.contentResolver

            // 构建短信的 URI  content://sms/2135
            val smsUri: Uri = Uri.withAppendedPath(Telephony.Sms.CONTENT_URI, smsid.toString())
            Log.d(tagLog, smsUri.toString())
            // 执行删除操作
            val rowsDeleted = contentResolver.delete(smsUri, null, null)
            Log.d(tagLog, "检查删除结果rowsDeleted:" + rowsDeleted.toString())
            // 检查删除结果
            if (rowsDeleted > 0) {
                Toast.makeText(context, "短信删除成功", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "未找到指定的短信", Toast.LENGTH_SHORT).show()
            }
            Log.d(tagLog, "endfun delsms()")

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "删除短信失败", Toast.LENGTH_SHORT).show()
        }
    }

    // 显示删除确认对话框
    private fun showDeleteConfirmationDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("确认删除")
            .setMessage("确定要删除吗？")
            .setPositiveButton("确定") { dialog, which ->
                // 用户点击“确定”，调用删除方法
                // del()
                dialog.dismiss()
                //del smss
                checkBoxList.forEach { smsWithCheckBox ->
                    if (smsWithCheckBox.isChecked) {
                        var smsid = smsWithCheckBox.text

                        //showDeleteConfirmationDialog
                        Log.d(tagLog, " checkBoxList.forEach#SMS ID: ${smsWithCheckBox.text} ")
                        delSms(this, smsid)
                    }

                }
            }
            .setNegativeButton("取消") { dialog, which ->
                // 用户点击“取消”，什么都不做
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun searchSms(text: Editable?): List<Sms> {
        Log.d(tagLog, "fun srchSms((")
        val txt = text.toString()
        Log.d(tagLog, txt)
        Log.d(tagLog, ")))")
        val smsList = mutableListOf<Sms>()
        val projection = arrayOf("address", "body", "date", "_id")
      //  val selectionArgs = arrayOf("%aaa%", "%bbb%")
       // var arr= txt.split(" ");
      //  val selection = "body LIKE ? AND body LIKE ?"
        val selection =  toBodyLikeStr(txt);
        Log.d(tagLog, "selection="+selection)
        val selectionArgs =to_arrayOf(txt)
        Log.d(tagLog, "selectionArgs="+encodeJson(selectionArgs))
        val cursor = contentResolver.query(
            Uri.parse("content://sms/inbox"),
            projection,
            selection,
            selectionArgs,
            "date DESC"
        )

        cursor?.use {
            val addressIndex = cursor.getColumnIndexOrThrow("address")
            val bodyIndex = cursor.getColumnIndexOrThrow("body")
            val dateIndex = cursor.getColumnIndexOrThrow("date")
            val idIndex = cursor.getColumnIndexOrThrow("_id")

            while (cursor.moveToNext()) {
                val address = cursor.getString(addressIndex)
                val body = cursor.getString(bodyIndex)
                val date = cursor.getLong(dateIndex)
                val id = cursor.getLong(idIndex)
                val sms = Sms(address, body, date, id)
                smsList.add(sms)
            }
        }
        return smsList
    }



    private fun toBodyLikeStr(txt: String): String {
        if (txt.isBlank()) return "" // 如果输入为空或全是空格，返回空数组 // 如果输入为空，返回 null
        val toTypedArray = txt.split(" ") // 按空格分割字符串
            .filter { it.isNotBlank() } // 过滤掉空白项
            .map { "body LIKE ? " } // 添加 '%' 符号
            .toTypedArray()
        return  joinToStr(toTypedArray, " and " ) // 转为 Array
    }

    private fun joinToStr(toTypedArray: Array<String>, separator: String): String {
        // 使用 joinToString 方法将数组元素连接为一个字符串，并用指定的分隔符分隔
        return toTypedArray.joinToString(separator )
    }

    /**
     * txt: aaa bbb
     * 希望可以返回  arrayOf("%aaa%", "%bbb%")
     */
    private fun to_arrayOf(txt: String): Array<String>? {
        if (txt.isBlank()) return emptyArray() // 如果输入为空或全是空格，返回空数组 // 如果输入为空，返回 null
        return txt.split(" ") // 按空格分割字符串
            .filter { it.isNotBlank() } // 过滤掉空白项
            .map { "%$it%" } // 添加 '%' 符号
            .toTypedArray() // 转为 Array
    }

    data class Sms(val address: String, val body: String, val date: Long, val id: Long)

    // 用于存储数据行的 CheckBox 引用
    var checkBoxList = mutableListOf<CheckBox>()

    @SuppressLint("SetTextI18n")
    private fun showList(dataList: List<Sms>) {
        // 获取 TableLayout 组件
        val table1 = binding.tableLayout;
// 清空现有的表格内容
        table1.removeAllViews()
        checkBoxList = mutableListOf<CheckBox>()

        // 构建表头
        val headerRow = TableRow(this)


        // 添加 CheckBox 表头
        val checkBoxHeader = CheckBox(this).apply {
            text = ""

        }
        headerRow.addView(checkBoxHeader)


        val dataHeader = TextView(this)
        dataHeader.text = "数据"

        headerRow.addView(dataHeader)

        table1.addView(headerRow)


        // val dataList = initData();
        // 填充表格数据
        for (item: Sms in dataList) {
            val dataRow = TableRow(this)


            // 添加 CheckBox
            val checkBox = CheckBox(this).apply {
                //  setPadding(16, 16, 16, 16)
                // 隐藏 CheckBox 的文本，并将宽高设置为 0
                layoutParams = TableRow.LayoutParams(10, 50)

                text = item.id.toString() // 设置文本，但文本不会显示
                //   visibility = View.INVISIBLE // 确保文本不可见
                //visi cant also show position
                //  setVisible(false)

            }

            dataRow.addView(checkBox)
            checkBoxList.add(checkBox)


            //android:layout_width="wrap_content"
            val dataText = TextView(this)
            // 使用 LayoutParams 设置 TextView 的宽度
            dataText.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            //dataText.width="wrap_content"
            dataText.text = item.id.toString() + " " + item.body
            dataText.setPadding(0, 3, 46, 3)
            dataRow.addView(dataText)

            table1.addView(dataRow)
        }


        // 设置全选 CheckBox 的点击事件
        checkBoxHeader.setOnCheckedChangeListener { _, isChecked ->
            checkBoxList.forEach { checkBox ->
                checkBox.isChecked = isChecked
                //ischk from waimyer inpiut
            }
        }

    }


    // 模拟获取数据（类似 Dart 中的 getData()）
    // 模拟获取数据（类似 Dart 中的 getData()）
    private fun initData(): List<Map<String, String>> {

        // 模拟的数据（类似于 Dart 中的 JSON）
        val dataList: MutableList<Map<String, String>> = mutableListOf()

        // 创建第一行数据
        val row1: MutableMap<String, String> = mutableMapOf()
        row1["date"] = "2024"
        row1["data"] = "1111"
        dataList.add(row1)

        // 创建第二行数据
        val row2: MutableMap<String, String> = mutableMapOf()
        row2["date"] = "2025"
        row2["data"] = "2222"
        dataList.add(row2)

        return dataList
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
        Log.d(tagLog, "fun onRequestPermissionsResult(")
        Log.d(tagLog, "requestCode:" + requestCode.toString())
        Log.d(tagLog, "permissions:" + permissions.toString())
        Log.d(tagLog, "grantResults:" + grantResults.toString())
        Log.d(tagLog, " )))")



        if (requestCode == REQUEST_SMS_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // 权限已授予
            Log.d(tagLog, "权限已授予REQUEST_SMS_PERMISSION")

        } else {
            Log.e(tagLog, "Permission denied to read SMS")
        }
        var requestCode4wrt = 111
        if (requestCode == requestCode4wrt) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // 权限已授予
                // 权限已授予
                Log.d(tagLog, "权限已授予requestCode4wrt")

            } else {
                // 权限被拒绝
                Log.d(tagLog, "权限被拒绝requestCode4wrt")

            }
        }
    }


//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == REQUEST_CODE) {
//            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                // 权限已授予
//            } else {
//                // 权限被拒绝
//            }
//        }
//    }
}