package comx.pkg

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.nio.charset.StandardCharsets
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

var tagLog2 = "MainActivity1114"


fun sendMsg(msg: String) {
    try {
        CoroutineScope(Dispatchers.IO).launch {
            val message = msg.toString()
            val address = "255.255.255.255" // 或广播地址 "255.255.255.255"
            val port = 18888

            send(message, address, port)
            send(message, getDeviceBroadcastIP(), port)

        }
    } catch (e: Exception) {
        Log.e(tagLog, "Error sendMsg(): ${e.message}")
    }


}

suspend fun send(message: String, address: String, port: Int) {

    withContext(Dispatchers.IO) {
        val debugInfo = listOf(broadcastAddress.hostAddress, port, message)
        Log.d(tagLog, "fun  send(( ${debugInfo.joinToString()} )))")


        // 创建一个 DatagramSocket
        val socket = DatagramSocket()

        try {
            // 设置广播地址和端口
            val broadcastAddress = InetAddress.getByName(address)


            // 打印调试信息
            val debugInfo = listOf(broadcastAddress.hostAddress, port, message)
           // println("fun send(( ${debugInfo.joinToString()} ))")
            Log.d(tagLog, "  send(( ${debugInfo.joinToString()} )))")


            // 将消息编码为字节数组
            val data = message.toByteArray(StandardCharsets.UTF_8)


            // 创建数据包
            val packet = DatagramPacket(data, data.size, broadcastAddress, port)

            // 发送数据包
            socket.send(packet)
            println("Broadcast message sent: $message")

        } catch (e: Exception) {
            Log.e(tagLog, "Error send(): ${e.message}")
        } finally {
            // 关闭 socket
            socket.close()
        }
        Log.d(tagLog, "endfun send( )")

    }
}


/**
 * val udpListener = UdpListener(18888)
 * udpListener.startListening()
 */
class UdpListener(private val port: Int) {

    private var socket: DatagramSocket? = null

    fun startListening(addMessageFun: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val logtag = tagLog
            try {
                // 创建一个 UDP 套接字并绑定到指定端口
                socket = DatagramSocket(port)
                Log.d(logtag, "Listening for UDP messages on port $port...")

                val buffer = ByteArray(1024) // 缓冲区，用于接收数据包

                while (true) {
                    val packet = DatagramPacket(buffer, buffer.size)

                    // 接收数据包（阻塞操作）
                    socket?.receive(packet)

                    // 解码数据包中的消息
                    val message = String(packet.data, 0, packet.length, Charsets.UTF_8)
                    val senderAddress = packet.address.hostAddress
                    val senderPort = packet.port

                    Log.d(
                        logtag,
                        "Received message: $message from $senderAddress:$senderPort"
                    )

                    // 处理接收到的消息
                    addMessageFun(message)
                }
            } catch (e: Exception) {
                Log.e(logtag, "Error while listening for UDP messages: ${e.message}")
            } finally {
                socket?.close()
            }
        }
    }

    private fun addMessage(message: String) {
        // 这里实现消息处理逻辑，可以更新 UI 或存储消息
        Log.d("UdpListener", "Message added: $message")
    }

    fun stopListening() {
        socket?.close()
        Log.d("UdpListener", "Stopped listening on port $port")
    }
}
