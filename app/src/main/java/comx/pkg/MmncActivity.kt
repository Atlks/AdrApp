package comx.pkg

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.security.MessageDigest

class MmncActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_mmnc)  // 这里设置第二个页面的布局

            // 假设在 onCreate 中
            val myButton = findViewById<Button>(R.id.geneBtn)

// 设置点击事件
            myButton.setOnClickListener {
                // 获取 EditText 中的文本，调用 toString() 转换为字符串
                btnclick()

            }

            val cpbtn = findViewById<Button>(R.id.copyBtn)
            cpbtn.setOnClickListener {
                // 获取 EditText 中的文本，调用 toString() 转换为字符串
                clkCpbtn()

            }


        } catch (e: Exception) {
            // 处理异常
            Log.e(tagLog, "Caught exception", e)
        }

    }

    private fun clkCpbtn() {
        var textView = findViewById<TextView>(R.id.textViewOtpt)
        val text = textView.text.toString()  // 获取 TextView 的文本并转换为 String
// 获取 ClipboardManager 系统服务
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

// 创建剪贴板数据
        val clip = ClipData.newPlainText("label", text)  // "label" 是剪贴板数据的标识符
        clipboard.setPrimaryClip(clip)  // 将数据放入剪贴板

// 可选：显示提示
        Toast.makeText(this, "文本已复制到剪贴板", Toast.LENGTH_SHORT).show()
    }

    private fun btnclick() {
        val basewd = findViewById<EditText>(R.id.basewdTxbx).text.toString()
        val nnn = findViewById<EditText>(R.id.nnnTxbx).text.toString()
        val salt = findViewById<EditText>(R.id.saltTxbx).text.toString()

        // 拼接字符串
        val seed = basewd + nnn + salt

        // 在这里处理按钮点击事件
        //Toast.makeText(this, "按钮被点击", Toast.LENGTH_SHORT).show()

        val hexSeed = encodeMd5(seed)
        Log.d(tagLog, "seed:" + hexSeed);
        val generateMnemonic = geneMmncCrpt(hexSeed)
        Log.d(tagLog, generateMnemonic)
        var textView = findViewById<TextView>(R.id.textViewOtpt)
        textView.text = generateMnemonic
    }


}