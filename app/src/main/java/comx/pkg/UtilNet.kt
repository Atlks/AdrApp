package comx.pkg

import java.net.Inet4Address
import java.net.NetworkInterface


/**
 * 返回结果： 返回修改后的广播地址。
 */
fun getDeviceBroadcastIP(): String {
    try {
        //val interfaces = NetworkInterface.getNetworkInterfaces()
        val interfaces = NetworkInterface.getNetworkInterfaces()
        for (networkInterface in interfaces) {
            // 跳过回环接口和未启用的接口
            if (!networkInterface.isUp || networkInterface.isLoopback) continue

            val addresses = networkInterface.inetAddresses
            for (address in addresses) {
                if (address is Inet4Address) { // 只处理 IPv4 地址
                    val ip = address.hostAddress
                    // 替换最后一个段为 255
                    return ip.substringBeforeLast(".") + ".255"
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "192.168.0.255" // 返回 null 表示无法获取 IP
}