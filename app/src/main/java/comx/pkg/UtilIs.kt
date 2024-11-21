package comx.pkg

import java.util.Locale

// 语言检测方法
  fun detectLanguage(text: String): Locale {
    return if (text.any { it.isCJKCharacter() }) {
        // 如果包含中文字符，使用中文
        Locale.CHINESE
    } else {
        // 否则使用英文
        Locale.ENGLISH
    }
}

// 扩展函数：检测是否为 CJK（中日韩）字符
  fun Char.isCJKCharacter(): Boolean {
    return this.code in 0x4E00..0x9FFF ||  // 常用汉字
            this.code in 0x3400..0x4DBF ||  // 扩展汉字 A
            this.code in 0x20000..0x2A6DF   // 扩展汉字 B
}