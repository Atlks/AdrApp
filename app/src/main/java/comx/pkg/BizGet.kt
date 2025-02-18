package comx.pkg;


import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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


//import comx.pkg.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.lang.Thread.sleep



fun ListSms(): List<MainActivity.Msg> {
    Log.d(tagLog, "fun ListSms((")

    Log.d(tagLog, ")))")


    var smsList = mutableListOf<MainActivity.Msg>()
    val messages = getAllrows(newContext()) // 传入 Context
   //  map fld
    messages.forEach { message ->
        var v = message.v;
        var jsonobj = decodeJson(v)
        var dvcnm = getFld(jsonobj, "dvcnm")
        var msg = getFld(jsonobj, "msg")
        var timestmp = getFldLong(jsonobj, "time", 0)
        val sms = MainActivity.Msg(dvcnm, msg, timestmp, "")
        //ingr /cmd msg
        if (!msg.startsWith("/"))
            smsList.add(sms)

        // println("Device: ${message.deviceName}, Message: ${message.msg}, Time: ${message.time}")
    }
    smsList = orderMsgList(smsList)
    // Convert MutableList to List (Immutable)
   // return smsList.toList()
     return smsList
}


//这里报错  Unresolved reference: applicationContext
fun newContext(): Context {
    return appContext
}

