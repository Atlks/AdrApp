package comx.pkg

import lib.containsAll
import lib.containsAny2025

var dontSpk: Boolean = true
var spkEnable: Boolean = false
var notOk: Boolean = true // 修正类型拼写

public fun chkfltNotOk(messageWzFmt: String): Boolean {

    if (containsAny2025(
            "我来人间一趟 小小新娘花 姜育恒 歌曲 醉酒歌 我的唇吻不到我爱的人 再见也是朋友 再见只是陌生人 女人的选择 世纪精选 漫漫人海我遇见了你",
            messageWzFmt
        )
    )
    {
        return notOk
    }


    if (containsAny2025(
            " 白条额度秀 周周返现\n" +
                    "                爆品酒店 省时又省心 省钱秘籍 最高可省 自由财经\n" +
                    "        急速退款 代收代付  金流\n" +
                    "        备份完成 即开即用\n" +
                    "                不要在我寂寞的时候说爱我  白狐",
            messageWzFmt
        )
    )
        return true


    if (containsAny2025(
            "游戏氛围 担保地址 广大玩家 点击地址自动复制 无需实名 七天担保 出款完毕",
            messageWzFmt
        )
    )
        return true;


    if (containsAll("微信 功能 还剩", messageWzFmt))
        return true;
    if (containsAll("y 还剩", messageWzFmt))
        return true;

    if (containsAll("大麻 麻姑", messageWzFmt))
        return true;

    if (containsAll("免息   折起", messageWzFmt))
        return dontSpk;


    if (containsAll("音乐 还剩", messageWzFmt))
        return true;

    if (containsAny2025(
            "已下载更新 新货速发 额外赠送 专属充值活动 专属邀请 专属福利 爆款抢购 共度美好时光 限时提额 时尚新品 限時秒殺 满减更优惠 魅力四射 购物券包 专享福利 给您定制 积分加倍 送完為止 優惠中 专属提额特权 限时福利 省呗 预审成功 拒收请回复 usdt USDT  高仿 虚拟币 反水 返水 盈利 佣金",
            messageWzFmt
        )
    )
        return true;

    if (containsAny2025(
            "已下载更新 已连接到USB 已隐藏敏感通知 正在启动导航 正在重新规划路线 正在通过USB充电 输入法 360手机卫士 黑U usdt USDT  高仿 虚拟币 反水 返水 盈利 佣金",
            messageWzFmt
        )
    )
        return true;

    if (containsAny2025(
            "杨小曼 云菲菲 梦然 高安  杭娇-慕容晓晓 何鹏 李翊君 张韶涵 龙梅子 谢军 劉若英 思小玥 蒋雪儿 刘若英 庄心妍 蔡依林 蒋雪儿 陶钰玉 张冬玲 云飞儿 贺敬轩 魏新雨 雪无影 王心凌 乌兰托娅 莫露露- 李潇潇 孙艺琪 孙语赛 关淑怡 徐誉滕 冯曦妤 容祖儿 丽江小倩 张冬玲  罗百吉 S翼乐团 T.R.Y ",
            messageWzFmt
        )
    )
        return true

    if (containsAny2025(
            "需要的可以联系 发现更低价 火热抢购中 新款韩版 淘宝同款 景区门票 包车旅游 有意者私信飞机 机票劵 优惠价 优惠劵 优惠升级卡  刮刮乐 公司内部频道 免单城市 购物满额 美团支付劵 财富路径 美好的节日里  定制贷款 大额产品 优惠活动 提醒您多次了  办卡优惠 以审为准 拒收回复 拒收请回复",
            messageWzFmt
        )
    )
        return true


    if (containsAny2025(


            "正在下载 所有未接电话 全场景音乐服务  应用正在后台运行 下午闹钟 多个应用进行了敏感操作 省电模式 网络共享或热点 正在通过USB充电 正在连接到USB 已连接到USB 闹钟 闹铃 正在获取服务信息",
            messageWzFmt
        )
    )
        return true



    if (containsAny2025
            (
            "已连接到USB调试 正在通过USB充电 重要应用会自动更新 热点 USB充电 USB调试 自动任务",
            messageWzFmt
        )
    )
        return true

    if (containsAny2025("帝豪 阿梦 再次提醒", messageWzFmt))
        return true;


    if (containsAny2025("急速退款 特惠航线 特惠专场 旅行团 抢票 火车票 心动之旅", messageWzFmt))
        return true;
    if (containsAny2025("登录过期  备用金 ", messageWzFmt))
        return true;
    if (containsAny2025("还款金 积分奖励 订单奖励 尊享 权益 特价 毗邻 日利率 ", messageWzFmt))
        return true;

    if (containsAny2025(
            "有奖小调研 体验度调研 派对酒局 动静皆宜 健康生活即刻开启 问卷调研 幸运注单 新人首存 最高奖励 送不停 彩金专员 天降红包 白资 百家乐 盈利 赌场 迪拜 反水 返水 盈利 佣金",
            messageWzFmt
        )
    ){
        return notOk;
    }


    if (containsAny2025("闹钟 响铃 正在备份", messageWzFmt))
        return true;
    if (containsAny2025("降息 备用金 收钱提醒助手", messageWzFmt))
        return true;

    //cp sys tips
    if (containsAny2025("备份正在进行中", messageWzFmt))
        return true;


    if (containsAny2025("帝豪 奸淫 父女 出轨 大片 人妻 乱伦", messageWzFmt))
        return true
    if (containsAny2025("环境问题对接 产研 产研中心 救火", messageWzFmt))
        return true
    if (containsAny2025(
            "出境漫游  中国移动 中国联通 联通 美好的一天从收钱开始",
            messageWzFmt
        )
    )
        return true
    if (containsAny2025("正在备份照片  产研 产研中心 救火", messageWzFmt))
        return true

    return false;

}

fun chkNotOk(title: String, text: String): Boolean {

//        if (title.contains("scot")  || text.contains("scot"))
//
//            return spkEnable

    //小时 总计
    if (text.contains("平均") && text.contains("mA"))
        return true


    if (title == "没有标题" && text == "没有内容")
        return true

    if (containsAny2025(
            "遇见了你",
            title
        )
    )
        return true
    if (title == "" && text == "")
        return true

    if (title.toLowerCase() == "timer")
        return true

    if (title.toLowerCase() == "im2025")
        return dontSpk

    if(title.contains("当前") && title.contains("mA")&& title.contains("剩余"))
        return  dontSpk

    if(title.contains("当前") && title.contains("mA")  )
        return  dontSpk

    if (title.contains("短信") && title.contains("正在运行"))
        return true

    //all english ... no chnchar ,not phone
    if ((!isContainCjkChar(title)) && (!isAllNumber(title))) {
        return true
    }

    if (title == "Choose input method")
        return true
    return false
}