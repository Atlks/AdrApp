package comx.pkg

import comx.pkg.MainActivity.Msg

fun orderMsgList(smsList: MutableList<MainActivity.Msg>): MutableList<Msg> {
    val toMutableList = smsList.sortedBy { it.time }.toMutableList()
   // return toMutableList.takeLast(minOf(1000, toMutableList.size))
    return toMutableList.subList(maxOf(0, toMutableList.size - 1000), toMutableList.size)

    // return toMutableList.subList(0, minOf(1000, toMutableList.size))
}


fun Fmtforeachx(smsList: List<Msg>): List<Msg> {
    val smsList2 = mutableListOf<Msg>()

    smsList.forEachIndexed { index, msg ->
        var msgNew = msg.copy()

        if (msg.dvcnm == getDeviceName(newContext())) {
            msgNew = msg.copy(dvcnm = "✨✨(${msg.dvcnm})")
            // smsList[index] = msg.copy(dvcnm = "✨✨(${msg.dvcnm})")
            //  msgNew.dvcnm="✨✨(${msg.dvcnm})"
        }
        smsList2.add(msgNew)

    }
    return smsList2
}