package comx.pkg
//import comx.databinding.ActivityMainBinding
//import comx.pkg.databinding.ActivityMainBinding
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ui.AppBarConfiguration

import comx.pkg.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.lang.Thread.sleep


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val REQUEST_SMS_PERMISSION = 7
    private val tagLog = "MainActivity1114"

    override fun onCreate(savedInstanceState: Bundle?) {

        try {
            Log.d(tagLog, "funx onCrt()")

            keeplive(this, MyNotificationListenerService::class.java)

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
                    android.Manifest.permission.ACCESS_NOTIFICATION_POLICY,
                    android.Manifest.permission.POST_NOTIFICATIONS
                )
                ActivityCompat.requestPermissions(
                    this,
                    permissions,
                    REQUEST_SMS_PERMISSION
                )
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.POST_NOTIFICATIONS
                    )
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            android.Manifest.permission.ACCESS_NOTIFICATION_POLICY,
                            android.Manifest.permission.POST_NOTIFICATIONS
                        ),
                        1
                    )
                }
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


            //-----------------btn click
            val deviceName1 = getDeviceName(this)
            binding.sendBtn.setOnClickListener {
                // 创建一个 Intent 对象，用于启动 SecondActivity
                sendBtnClik(deviceName1)
            }


            binding.resendAllMsg.setOnClickListener {
                // 创建一个 Intent 对象，用于启动 SecondActivity
                // exportSystemNotesToJson(this, "notebek.json")
                sendAgainMsg(this)
            }


            binding.getBdcst.setOnClickListener {

                // 显示Toast
                val toast =
                    Toast.makeText(this, "bdcstIp=" + getDeviceBroadcastIP(), Toast.LENGTH_LONG)
                toast.show()
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

            //打开通知设置界面
            binding.setNtfyAuth.setOnClickListener {
                try {
                    sendNotification(this, "通知标题", "这是通知内容")
                    // 创建一个 Intent 对象，用于启动 SecondActivity
                    // android.setting
                    val intent = Intent("android.settings.ACTION_APP_NOTIFICATION_SETTINGS")
                    //startActivity(intent)
                    gotoNtfyUI(this, "comx.pkg")
                } catch (e: Exception) {
                    Log.e(tagLog, "Erroreee", e)
                }


                // 打开通知设置界面
                val intent2 = Intent().apply {
                    action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    putExtra(Settings.EXTRA_APP_PACKAGE, packageName) // 传递当前应用的包名
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        putExtra(Settings.EXTRA_CHANNEL_ID, "my_channel_id") // 传递通知渠道 ID（如有需要）
                    }
                }
            }
            binding.setNtfyLsnAuth.setOnClickListener {

                //  sendNotification(this, "通知标题", "这是通知内容")

                // 创建一个 Intent 对象，用于启动 SecondActivity

                val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                startActivity(intent)
            }

            var btn11 = binding.gotoSmsBtn;
            binding.gotoSmsBtn.setOnClickListener {


                val java = SmsmngActivity::class.java
                val intent = Intent(this, java)
                startActivity(intent)  // 启动 SecondActivity

            }

            val deviceName = deviceName1
            Log.d(tagLog, deviceName)


            //-----------------otehr
            setRcvMsgLsnr()


            //block show list
            var smsList = ListSms()
            Log.d(tagLog, "smslist.size:" + smsList.size)
            // binding.textView.text = "cnt:" + smsList.size
            bindData2Table(smsList);
            binding.txtbx1.setText("")

            //滚动到底部
            var scrollView = binding.scrvw;
            scrollView.post {
                scrollView.fullScroll(View.FOCUS_DOWN)
            }

            //  keeplive(this)

            Log.d(tagLog, "endfun onCrt()")

        } catch (e: Exception) {
            // 处理异常
            Log.e(tagLog, "Caught exception", e)
        }

    }

    private fun sendAgainMsg(mainActivity: Context) {
        // var smsList = ListSms()
        val messages = getAllrows(this) // 传入 Context
        messages.forEach { message ->
            var v = message.v;
            sendMsg(v)

            // println("Device: ${message.deviceName}, Message: ${message.msg}, Time: ${message.time}")
        }


    }

    private fun setRcvMsgLsnr() {
        val udpListener = UdpListener(18888)
        udpListener.startListening { message ->
            // 这里是处理接收到的消息的地方
            Log.d(tagLog, "startListening msg rcv ..")
            sleep(200);
            Log.d(tagLog, "startListening msg rcv after 3sec")

            RcvmsgHdlr(message)
            Log.d(tagLog, "end startListening ..")
            //  insertDB(jsonObj["devicename"], jsonObj["msg"]);

        }
        val udpListener2 = UdpListener(28888)
        udpListener2.startListening { message ->
            // 这里是处理接收到的消息的地方
            Log.d(tagLog, "startListening msg rcv ..")
            sleep(400);
            Log.d(tagLog, "startListening msg rcv after 3sec")

            RcvmsgHdlr(message)
            Log.d(tagLog, "end startListening ..")
            //  insertDB(jsonObj["devicename"], jsonObj["msg"]);

        }
    }

    private fun sendBtnClik(deviceName1: String) {
        val deviceName2 = getDeviceName(this)
        try {
            //----block send
            var msg = binding.txtbx1.text.toString()
            val time = getTimestampInSecs()
            var msgid = encodeMd5(deviceName1 + msg + time)
            val msg1 = Msg(deviceName1, msg, time, msgid)
            val encodeJson_msg = encodeJson(msg1)
            sendMsg(encodeJson_msg.toString())


            lifecycleScope.launch(Dispatchers.IO) {


                //----------block insert
                insertDB(this@MainActivity, msgid, encodeJson_msg);


                //-----------block show list
                var smsList = ListSms()
                Log.d(tagLog, "smslist.size:" + smsList.size)
                // binding.textView.text = "cnt:" + smsList.size
                //goto main thrd updt ui
                withContext(Dispatchers.Main) {

                    bindData2Table(smsList);
                    //滚动到底部
                    var scrollView = binding.scrvw;
                    scrollView.post {
                        scrollView.fullScroll(View.FOCUS_DOWN)

                    }
                }
            }




            binding.txtbx1.setText("")


        } catch (e: Exception) {
            Log.e(tagLog, "Error while searching SMS or showing list", e)
        }
    }

    private fun RcvmsgHdlr(messageStr: String) {
        Log.d(tagLog, "fun msgrecv((")
        Log.d(tagLog, "message=" + messageStr);
        Log.d(tagLog, ")))")
        Log.d(tagLog, "Message received: $messageStr")
        // 例如，更新 UI 或保存消息
        var jsonObj = decodeJson(messageStr)
        // 检查 jsonObj 是否为 null，并确保 devicename 和 msg 键存在
        if (jsonObj == null) {
            Log.d(tagLog, "endfun msgrecv()#ret=")
            return
        }


        // if my msg  ret
        val deviceName = jsonObj.optString("dvcnm")
        if (deviceName.equals(getDeviceName(this)))
            return


        val msg = jsonObj.optString("msg")
        val msgid = jsonObj.optString("id")// 使用 optString 来安全获取值
        insertDB(this, msgid, messageStr)


        //-------bing to list
        var smsList = ListSms()
        Log.d(tagLog, "smslist.size:" + smsList.size)
        // binding.textView.text = "cnt:" + smsList.size
        // 切换到主线程更新 UI
        runOnUiThread {
            bindData2Table(smsList);
            //滚动到底部
            var scrollView = binding.scrvw;
            scrollView.post {
                scrollView.fullScroll(View.FOCUS_DOWN)
            }
        }



        Log.d(tagLog, "endfun msgrecv()#ret=")
    }


    private fun sendMsg(msg: String) {

        CoroutineScope(Dispatchers.IO).launch {
            val message = msg.toString()
            val address = "255.255.255.255" // 或广播地址 "255.255.255.255"
            val port = 18888

            send(message, address, port)
            send(message, getDeviceBroadcastIP(), port)

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

    @Serializable
    data class Msg(val dvcnm: String, val msg: String, val time: Long, val id: String)

    private fun ListSms(): List<Msg> {
        Log.d(tagLog, "fun ListSms((")

        Log.d(tagLog, ")))")


        val smsList = mutableListOf<Msg>()
        val messages = getAllrows(this) // 传入 Context
        messages.forEach { message ->
            var v = message.v;
            var jsonobj = decodeJson(v)
            var dvcnm = getFld(jsonobj, "dvcnm")
            var msg = getFld(jsonobj, "msg")
            var timestmp = getFldLong(jsonobj, "time", 0)
            val sms = Msg(dvcnm, msg, timestmp, "")
            smsList.add(sms)

            // println("Device: ${message.deviceName}, Message: ${message.msg}, Time: ${message.time}")
        }
        orderMsgList(smsList)

        return smsList
    }


    private fun orderMsgList(smsList: MutableList<MainActivity.Msg>): MutableList<Msg> {
        return smsList.sortedBy { it.time }.toMutableList()
    }


    // 用于存储数据行的 CheckBox 引用
    var checkBoxList = mutableListOf<CheckBox>()

    @SuppressLint("SetTextI18n")
    private fun bindData2Table(dataList: List<Msg>) {
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
        for (item: Msg in dataList) {
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
            dataText.text = item.id.toString() + " " + item.dvcnm + ":" + item.msg
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


