package com.example.myapplication

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.myapplication.databinding.ActivityMainBinding
//import kotlinx.android.synthetic.main.activity_main.*
//import ac

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化 ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // setContentView(R.layout.activity_main)  // 确保这一行调用


        // 初始化数据
       // initData()


        // 获取 TableLayout 组件
        val tableLayout =  binding.tableLayout;


        // 构建表头
        val headerRow = TableRow(this)
        val dateHeader = TextView(this)
        dateHeader.text = "日期"
        dateHeader.setPadding(16, 16, 16, 16)
        headerRow.addView(dateHeader)

        val dataHeader = TextView(this)
        dataHeader.text = "数据"
        dataHeader.setPadding(16, 16, 16, 16)
        headerRow.addView(dataHeader)

        tableLayout.addView(headerRow)


        val dataList=initData();
        // 填充表格数据
        for (item in dataList) {
            val dataRow = TableRow(this)
            val dateText = TextView(this)
            dateText.text = item.get("date")
            dateText.setPadding(16, 16, 16, 16)
            dataRow.addView(dateText)

            val dataText = TextView(this)
            dataText.text = item.get("data")
            dataText.setPadding(16, 16, 16, 16)
            dataRow.addView(dataText)

            tableLayout.addView(dataRow)
        }


        // 设置按钮点击事件，跳转到 SecondActivity 页面
        val gotoFormButton = binding.gotoFormButton
        gotoFormButton.setOnClickListener {
//            val intent = Intent(
//                this@MainActivity,
//               // SecondActivity::class.java
//            )
//            startActivity(intent)
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
}