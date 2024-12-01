package comx.pkg
//import comx.databinding.ActivityMainBinding
//import comx.pkg.databinding.ActivityMainBinding

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ComponentName
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.speech.tts.TextToSpeech
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.lang.Thread.sleep


// val tagLog = "MainActivity1114"

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityMainBinding
    private val REQUEST_SMS_PERMISSION = 889
    val READ_PHONE_STATE_pmscode = 888
    val rmsFOREGROUND_SERVICE_DATA_SYNC = 890
    var pmsPOST_NOTIFICATIONS = 891
    val pmscode_READ_CONTACTS = 892
    val pmscode_READ_EXTERNAL_STORAGE = 893
    override fun onCreate(savedInstanceState: Bundle?) {
        appContext = applicationContext
        AppCompatActivity1main = this

        try {
            Log.d(tagLog, "funx onCrt()")

            // 1. 确保已经请求了权限
            if (ContextCompat.checkSelfPermission(
                    AppCompatActivity1main,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    AppCompatActivity1main,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
                return  // 如果权限未获得，返回
            }
            scanFrmDcmt(this )

            keeplive4FrgrdSvrs(this, MyNotificationListenerService::class.java)

            keeplive2alarmManager(this, MyNotificationListenerService::class.java)
            //     keeplive3JobScheduler(this, MyNotificationListenerService::class.java)

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
                val permission = android.Manifest.permission.FOREGROUND_SERVICE_DATA_SYNC
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(permission), rmsFOREGROUND_SERVICE_DATA_SYNC)
                }
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
                        pmsPOST_NOTIFICATIONS
                    )
                }
            }
            checkPermissions4READ_PHONE_STATE()

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    pmscode_READ_EXTERNAL_STORAGE
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


            //-----------------btn click
            val deviceName1 = getDeviceName(this)
            binding.sendBtn.setOnClickListener {
                // 创建一个 Intent 对象，用于启动 SecondActivity
                sendBtnClik(deviceName1)
            }


            var menudiv = binding.menudiv
            binding.menux.setOnClickListener(View.OnClickListener { // 当按钮被点击时，切换LinearLayout的可见性
                if (menudiv.getVisibility() === View.GONE) {
                    menudiv.setVisibility(View.VISIBLE)
                } else {
                    menudiv.setVisibility(View.GONE)
                }
            })


            //打开设置自启动权限ui
            binding.setautoStartBtn.setOnClickListener {

                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:${packageName}")
                startActivity(intent)

                //val intent = Intent(Settings.autoStart)
                // startActivity(intent)
                if (Build.MANUFACTURER.equals("Xiaomi", ignoreCase = true)) {
                    // Open Xiaomi's Auto Start settings
                    val intent = Intent()
                    intent.component = ComponentName(
                        "com.miui.securitycenter",
                        "com.miui.securitycenter.activity.autostart.AutoStartActivity"
                    )
                    //  startActivity(intent)
                }
            }
            binding.setBtrOptmzBtn.setOnClickListener {
                val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                startActivity(intent)

            }


            /**
             * TextToSpeech API（更常用的做法）
             * 大多数应用程序不会直接使用 Intent.ACTION_TTS_SERVICE 来启动 TTS 服务，而是通过 TextToSpeech API 来初始化 TTS 服务并进行文本到语音的合成。
             */
            binding.setTTSBtn.setOnClickListener {

                try {


                    // Prompt the user to install language data
                    val intent = Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA)
                    startActivity(intent)


                } catch (e: Exception) {
                    // 处理异常
                    Log.e(tagLog, "setTTSBtn Caught exception", e)
                }
//                try {
//
//                    val intent = Intent()
//                    intent.setAction(TextToSpeech.ACTION_TTS_SERVICE)
//                    val packageManager = packageManager
//
//
//// 查找所有可以处理该 Intent 的应用
//                    val services = packageManager.queryIntentServices(intent, 0)
//                    if (services != null && !services.isEmpty()) {
//                        // 有可用的 TTS 服务
//                        Log.d("TTS", "TTS service found")
//                    } else {
//                        // 没有可用的 TTS 服务
//                        Log.d("TTS", "No TTS service available")
//                    }
//
//
//                } catch (e: Exception) {
//                    // 处理异常
//                    Log.e(tagLog, "setTTSBtn Caught exception", e)
//                }

            }



            menudiv.setVisibility(View.GONE)

            binding.resendAllMsg.setOnClickListener {
                // 创建一个 Intent 对象，用于启动 SecondActivity
                // exportSystemNotesToJson(this, "notebek.json")
                sendMsgAllAgainForeach(this)
            }

            //
            binding.noBtrOptmzBtn.setOnClickListener {
                val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                startActivity(intent)
            }

            binding.reqSycMsg.setOnClickListener {
                reqSycMsgClk(deviceName1)
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

            //val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            //startActivity(intent)

            //打开通知设置界面
            binding.setNtfyAuth.setOnClickListener {
                try {
                    sendNotification2024(this, "通知标题", "这是通知内容")
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
         //   setRcvMsgLsnr()


            //block show list
            var smsList = ListSms()
            Log.d(tagLog, "smslist.size:" + smsList.size)
            // binding.textView.text = "cnt:" + smsList.size
            smsList = Fmtforeachx(smsList)

            bindData2Table(smsList);
            binding.txtbx1.setText("")

            //滚动到底部
            var scrollView = binding.scrvw;
            scrToButtom(scrollView)

            //  keeplive(this)

            Log.d(tagLog, "endfun onCrt()")

        } catch (e: Exception) {
            // 处理异常
            Log.e(tagLog, "Caught exception", e)
        }

    }


    /**
     * 这里要扫描 documents文件夹下所有文件
     */
    private fun scanFrmDcmt(context: Context ) {
        Log.d(tagLog, "\n\n\n")
        Log.d(tagLog, "fun scanFrmDcmt(")


        Log.d(tagLog, "))")


        //externalStorageState =mounted
        val externalStorageState = Environment.getExternalStorageState()
        val b = Environment.MEDIA_MOUNTED != externalStorageState
        val bb2= Environment.MEDIA_MOUNTED_READ_ONLY != externalStorageState
        if (b &&bb2) {
            // Storage is not available
            Log.d(tagLog, "Storage is not available")
        }
        val contentResolver = context.contentResolver
        // val mimeType = "audio/mpeg"
        val relativePath = "Documents/aasms/" // 确保 fldr 变量已经被定义且正确

        // 创建查询条件
        var selection = "${MediaStore.MediaColumns.DISPLAY_NAME} = ? AND " +
                "${MediaStore.MediaColumns.RELATIVE_PATH} = ?"
        // _display_name = ? AND relative_path = ?
        selection="relative_path = ?"
        val selectionArgs = arrayOf("$relativePath")


        /**
         * 使用 MediaStore.Files.getContentUri("external") 会获取外部存储（通常是设备的 SD 卡，如果没有 SD 卡，则是设备的内置存储）上所有文件的 URI。这意味着，通过这个 URI 进行的查询会检索到外部存储上的所有文件，而不仅仅是特定类型的文件或特定目录下的文件。
         */
        // 使用 MediaStore 查询文件
  //MediaStore.Files.EXTERNAL_CONTENT_URI,
      //  MediaStore.VOLUME_EXTERNAL
        val contentUri = MediaStore.Files.getContentUri("external")
        Log.d(tagLog, "contentUri=" + contentUri)
        //contentUri==  content://media/external/file
        val cursor = contentResolver.query(
            contentUri,
            null,
            null,
            null,
            null
        )

        cursor?.use {
           // var rztbool=it.moveToNext()
            while (it.moveToNext()) {
                val uri = ContentUris.withAppendedId(
                    contentUri,
                    it.getLong(it.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                )
                Log.d(tagLog, "datasrc uri=" + uri)


                Thread(Runnable {
                  //  playMp3(context, uri)
                }).start()

            }
        }

        Log.d(tagLog, "endfun scanFrmDcmt()")

    }

    private fun playAudio2024FrmDcmt(context: Context, fileName: String) {
        Log.d(tagLog, "\n\n\n")
        Log.d(tagLog, "fun playAudio2024FrmDcmt(")

        Log.d(tagLog, "fileName=" + fileName)
        Log.d(tagLog, "))")
        val contentResolver = context.contentResolver
       // val mimeType = "audio/mpeg"
        val relativePath = "Documents/" // 确保 fldr 变量已经被定义且正确

        // 创建查询条件
        var selection = "${MediaStore.MediaColumns.DISPLAY_NAME} = ? AND " +
                "${MediaStore.MediaColumns.RELATIVE_PATH} = ?"
       // _display_name = ? AND relative_path = ?
        selection="_display_name = ?"
        val selectionArgs = arrayOf(fileName)

        // 使用 MediaStore 查询文件
        val contentUri = MediaStore.Files.getContentUri("external")
        Log.d(tagLog, "contentUri=" + contentUri)
      //contentUri==  content://media/external/file
        val cursor = contentResolver.query(
            contentUri,
            null,
            selection,
            selectionArgs,
            null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val uri = ContentUris.withAppendedId(
                    contentUri,
                    it.getLong(it.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                )
                Log.d(tagLog, "datasrc uri=" + uri)


                Thread(Runnable {
                    playMp3(context, uri)
                }).start()

            } else {
                Log.e("playMp3FromDocumentDir", "MP3 file not found")
            }
        }

        Log.d(tagLog, "endfun playAudio2024FrmDcmt()")

    }

    private fun playMp3(context: Context, uri: Uri) {

        try {
            val mediaPlayer = MediaPlayer().apply {
                setDataSource(context, uri)
                prepare() // 准备播放
                start() // 开始播放
            }
        } catch (e: Exception) {
            Log.e(tagLog, "playAudio() Caught exception", e)


        }

    }


    private fun reqSycMsgClk(deviceName1: String) {
        val deviceName2 = getDeviceName(this)

        //----block send
        var msg = "/reqSyn"
        val time = getTimestampInSecs()
        var msgid = encodeMd5(deviceName1 + msg + time)
        val msg1 = Msg(deviceName1, msg, time, msgid)
        val encodeJson_msg = encodeJson(msg1)
        sendMsg(encodeJson_msg.toString())

    }


    private fun checkPermissions4READ_PHONE_STATE() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                READ_PHONE_STATE_pmscode
            )
            sleep(1000)

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                pmscode_READ_CONTACTS
            )


        }
    }


    private fun sendMsgAllAgainForeach(mainActivity: Context) {
        // var smsList = ListSms()
        val messages = getAllrows(this) // 传入 Context
        messages.forEach { message ->
            var v = message.v;
            if (!v.startsWith("/"))
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

            RcvmsgHdlrAsync(message)
            Log.d(tagLog, "end startListening ..")
            //  insertDB(jsonObj["devicename"], jsonObj["msg"]);

        }
        val udpListener2 = UdpListener(28888)
        udpListener2.startListening { message ->
            // 这里是处理接收到的消息的地方
            Log.d(tagLog, "startListening msg rcv ..")
            sleep(400);
            Log.d(tagLog, "startListening msg rcv after 3sec")

            RcvmsgHdlrAsync(message)
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
                write_row(this@MainActivity, msgid, encodeJson_msg);


                //-----------block show list
                var smsList = ListSms()
                smsList = Fmtforeachx(smsList)
                //order by sendtime
                Log.d(tagLog, "smslist.size:" + smsList.size)
                // binding.textView.text = "cnt:" + smsList.size
                //goto main thrd updt ui
                withContext(Dispatchers.Main) {

                    bindData2Table(smsList);
                    //滚动到底部
                    var scrollView = binding.scrvw;
                    scrToButtom(scrollView)
                }
            }




            binding.txtbx1.setText("")


        } catch (e: Exception) {
            Log.e(tagLog, "Error while searching SMS or showing list", e)
        }
    }

    private fun RcvmsgHdlrAsync(messageStr: String) {

        Thread(Runnable {
            //  sendNecho(message)

            sleep(500)
            (rcvMsgHdlrCore(messageStr))

        }).start()
    }

    private fun rcvMsgHdlrCore(messageStr: String): Boolean {
        Log.d(tagLog, "fun msgrecv((")
        Log.d(tagLog, "message=" + messageStr);
        Log.d(tagLog, ")))")
        Log.d(tagLog, "Message received: $messageStr")


        //---idx
        // if my msg  ret
        //insertDB
        //qry n bing to list


        // 例如，更新 UI 或保存消息
        var jsonObj = decodeJson(messageStr)
        // 检查 jsonObj 是否为 null，并确保 devicename 和 msg 键存在
        if (jsonObj == null) {
            Log.d(tagLog, "endfun msgrecv()#ret=")
            return true
        }


        // if my msg  ret
        val deviceName = jsonObj.optString("dvcnm")
        //        if (deviceName.equals(getDeviceName(this)))
        //            return


        //--------reqSyn
        val msg = jsonObj.optString("msg")

        if (msg == "/reqSyn") {
            sendMsgAllAgainForeach(this)
            return true
        }

        //--------
        val msgid = jsonObj.optString("id")// 使用 optString 来安全获取值
        write_row(this, msgid, messageStr)


        //-------bing to list
        var smsList = ListSms()
        smsList = Fmtforeachx(smsList)
        Log.d(tagLog, "smslist.size:" + smsList.size)
        // binding.textView.text = "cnt:" + smsList.size
        // 切换到主线程更新 UI
        runOnUiThread {
            bindData2Table(smsList);
            //滚动到底部
            var scrollView = binding.scrvw;
            scrToButtom(scrollView)
        }



        Log.d(tagLog, "endfun msgrecv()#ret=")
        return false
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


    // 用于存储数据行的 CheckBox 引用
    var checkBoxList = mutableListOf<CheckBox>()

    @SuppressLint("SetTextI18n")
    public fun bindData2Table(dataList: List<Msg>) {
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
        Log.d(tagLog, "\n\n\n")
        Log.d(tagLog, "fun onRequestPermissionsResult(")
        Log.d(tagLog, "requestCode:" + requestCode.toString())
        Log.d(tagLog, "permissions:" + encodeJson(permissions).toString())
        Log.d(tagLog, "grantResults:" + encodeJson(permissions).toString())
        Log.d(tagLog, " )))")



        if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限已授予
                Log.d(tagLog, "权限已授予REQUEST_SMS_PERMISSION")

            } else {
                Log.e(tagLog, "Permission denied to read SMS")
            }
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

        Log.d(tagLog, "endfun onRequestPermissionsResult()")
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


