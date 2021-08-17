package com.onelink.nrlp.android.utils

import com.onelink.nrlp.android.BuildConfig


object LukaKeRakk {

    init {
        System.loadLibrary("see")
    }

    private external fun helis(): String

    private external fun waat(): String

    private external fun mahsw(): String

    private external fun jojoo(): String

    private external fun monika(): String

    private external fun bsjw(): String

    private external fun mia(): String

    private external fun sia(): String

    private external fun moojo(): String

    private external fun poojo(): String

    private external fun dm(): String

    private external fun pm(): String

    private external fun isDetectedDevKeys(): Boolean

    private external fun isDetectedTestKeys(): Boolean

    private external fun isNotFoundReleaseKeys(): Boolean

    private external fun isFoundDangerousProps(): Boolean

    private external fun isPermissiveSelinux(): Boolean

    private external fun isSuExists(): Boolean

    private external fun isAccessedSuperuserApk(): Boolean

    private external fun isFoundSuBinary(): Boolean

    private external fun isFoundBusyboxBinary(): Boolean

    private external fun isFoundXposed(): Boolean

    private external fun isFoundResetprop(): Boolean

    private external fun isFoundWrongPathPermission(): Boolean

    private external fun isFoundHooks(): Boolean

    private external fun dap(): String

    private external fun pap(): String

    private external fun onk(): String

    private external fun twk(): String

    private external fun thk(): String

    private external fun onkd(): String

    private external fun twkd(): String

    private external fun thkd(): String


    fun isRooted() = isDetectedDevKeys()
            || isDetectedTestKeys()
            || isNotFoundReleaseKeys()
            || isFoundDangerousProps()
            || isPermissiveSelinux()
            || isSuExists()
            || isAccessedSuperuserApk()
            || isFoundSuBinary()
            || isFoundBusyboxBinary()
            || isFoundXposed()
            || isFoundResetprop()
            || isFoundWrongPathPermission()
            || isFoundHooks()

    private fun yppet() = helis().dc() +
            "-${waat().dc()}" +
            "-${mahsw().dc()}" +
            "-${bsjw().dc()}" +
            "-${mia().dc()}"

    fun shusi() = if (BuildConfig.IS_DEBUG) yppet() else sia().dc()

    fun susu() = if (BuildConfig.IS_DEBUG) moojo().dc() else poojo().dc()

    fun ure38() = if (BuildConfig.IS_DEBUG) jojoo().dc() else monika().dc()

    fun gdm() = if (BuildConfig.IS_DEBUG) dm().dc() else pm().dc()

    fun appurva() = if (BuildConfig.IS_DEBUG) dap().dc() else pap().dc()

    fun kcon() = if (BuildConfig.IS_DEBUG) onkd().dc() else onk().dc()

    fun kctw() = if (BuildConfig.IS_DEBUG) twkd().dc() else twk().dc()

    fun kcth() = if (BuildConfig.IS_DEBUG) thkd().dc() else thk().dc()


}