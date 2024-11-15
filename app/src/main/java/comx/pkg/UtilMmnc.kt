//mmnc.kt

package comx.pkg



import org.bitcoinj.crypto.MnemonicCode
import org.bitcoinj.crypto.MnemonicException
import org.bouncycastle.util.encoders.Hex
  val tagLog = "MainActivity1114"
fun generateMnemonic(hexPrivateKey: String): String {
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

