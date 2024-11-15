package comx.pkg

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.security.MessageDigest

class MmncActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mmnc)  // 这里设置第二个页面的布局

        try{
            val hexSeed = encodeMd5("888")
            Log.d(tagLog,"seed:"+hexSeed);
            Log.d(tagLog,generateMnemonic(hexSeed))


    } catch (e: Exception) {
        // 处理异常
        Log.e(tagLog, "Caught exception", e)
    }

    }


}