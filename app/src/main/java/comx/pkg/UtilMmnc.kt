//mmnc.kt

package comx.pkg



import org.bitcoinj.crypto.MnemonicCode
import org.bitcoinj.crypto.MnemonicException
import org.bouncycastle.util.encoders.Hex
import java.util.UUID

val tagLog = "MainActivity1114"

fun geneMmnc(hexPrivateKey: String): String {
    // 将十六进制密钥转换为字节数组
    val privateKeyBytes = Hex.decode(hexPrivateKey)

    // 使用前 16 字节作为熵（128 位熵）
    val entropy = ByteArray(16)
    System.arraycopy(privateKeyBytes, 0, entropy, 0, minOf(privateKeyBytes.size, entropy.size))

    // 生成助记词
    val mnemonic: List<String> = try {
        MnemonicCode.INSTANCE.toMnemonic(entropy)
    } catch (e: MnemonicException.MnemonicLengthException) {
        throw RuntimeException(e)
    }
    return mnemonic.joinToString(" ")
}


fun geneMmncCrpt(seedMd5Hexencode: String): String {
    // 控制调试输出
    if (BuildConfigDEBUG) {
        println("geneMmncCrpt ${seedMd5Hexencode}")
    }

    // 打印随机数
    println(random(2, 9))

    // 生成各部分的字符串
    var s = generateMnemonicLeft(generateMnemonicRdm(), random(2, 9))
    s += " echo "
    val globalspltr2024="split"
    s += generateMnemonicLeft(generateMnemonicRdm(), random(2, 7)) + " " + globalspltr2024 + " "
    s += generateMnemonicIvei(geneMmnc(seedMd5Hexencode)) + " "
    s += generateMnemonicLeft(generateMnemonicRdm(), random(2, 12))

    // 调试输出
    if (BuildConfigDEBUG) {
        println("[geneMmncCrpt] ret => $s")
    }

    return s
}

fun generateMnemonicRdm(): String {

  var seed=generateUUID()
    return  geneMmnc(encodeMd5(seed))
}
fun generateUUID(): String {
    // 使用 UUID 类生成随机 UUID
    return UUID.randomUUID().toString()
}

val BuildConfigDEBUG=true
fun generateMnemonicIvei(mmnc: String): String {

    if (BuildConfigDEBUG) {
        println("generateMnemonicIvei ${mmnc}")
    }

    var mmncTrimmed = mmnc.trim()
    val arr = mmncTrimmed.split(" ")

    // 切割数组
    val aHoumyar = arr.slice(6..11)
    val aCyemyar = arr.slice(0..5)

    // 拼接结果
    val s = aHoumyar.joinToString(" ") + " " + aCyemyar.joinToString(" ")

    if (BuildConfigDEBUG) {
        println("[generateMnemonicIvei] ret => $s")
    }

    return s
}

fun generateMnemonicLeft(mmnc: String, n: Int): String {
    if (BuildConfigDEBUG) {
        println("generateMnemonicLeft ${mmnc} n=$n")
    }

    var mmncTrimmed = mmnc.trim()
    val arr = mmncTrimmed.split(" ")

    // 获取切割的数组部分
    val aHoumyar = arr.slice(6..11)  // 选择第7到第12个元素
    val aCyemyar = arr.slice(0 until n)  // 选择从第1个元素到第n个元素

    // 拼接结果
    val s = aCyemyar.joinToString(" ")

    if (BuildConfigDEBUG) {
        println("[generateMnemonicLeft] ret => $s")
    }

    return s
}


fun random(min: Int, max: Int): Int {
    // 生成一个 [min, max] 范围内的随机整数
    return (min..max).random()
}