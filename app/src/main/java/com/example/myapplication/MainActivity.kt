package com.example.myapplication

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.ui.AppBarConfiguration
import com.example.myapplication.databinding.ActivityMainBinding
import android.Manifest
import android.annotation.SuppressLint
import android.view.View
import android.widget.CheckBox

//import kotlinx.android.synthetic.main.activity_main.*
//import ac

private const val tagLog = "MainActivity1114"

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val REQUEST_SMS_PERMISSION = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化 ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // setContentView(R.layout.activity_main)  // 确保这一行调用


        //----reg sms auth
        //检查并请求权限
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_SMS),
                REQUEST_SMS_PERMISSION
            )
        }

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


        // 获取 CheckBox 的选中状态并打印到日志中
        binding.delBtn.setOnClickListener {
            checkBoxList.forEach { smsWithCheckBox ->
                if (smsWithCheckBox.isChecked)
                    Log.d(tagLog, "SMS ID: ${smsWithCheckBox.text} ")
            }
        }


        // val intent = Intent(this, SecondActivity::class.java)
        // startActivity(intent)

//end   设置点击事件监听器

        // 初始化数据
        // initData()


        //   showList()


        //ssss 设置按钮点击事件，跳转到 SecondActivity 页面
//        val gotoFormButton = binding.gotoFormButton
//        gotoFormButton.setOnClickListener {
//            // 创建一个 Intent 对象，用于启动 SecondActivity
//            val intent = Intent(this, SecondActivity::class.java)
//            startActivity(intent)  // 启动 SecondActivity
////            val intent = Intent(
////                this@MainActivity,
////               // SecondActivity::class.java
////            )
////            startActivity(intent)
//        }


    }


    private fun searchSms(text: Editable?): List<Sms> {
        Log.d(tagLog, "fun srchSms((")
        Log.d(tagLog, text.toString())
        Log.d(tagLog, ")))")
        val smsList = mutableListOf<Sms>()
        val projection = arrayOf("address", "body", "date", "_id")
        val selection = "body LIKE ?"
        val selectionArgs = arrayOf("%${text.toString()}%")
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
        val REQUEST_SMS_PERMISSION = 1
        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
        if (requestCode == REQUEST_SMS_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // 权限已授予
            Log.d(tagLog, "权限已授予")

        } else {
            Log.e(tagLog, "Permission denied to read SMS")
        }
    }

}