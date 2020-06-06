package com.soywiz.klock.internal

import com.soywiz.klock.*
import com.soywiz.klock.hr.HRTimeSpan
import java.util.*

internal actual object KlockInternal {
    actual val currentTime: Double get() = CurrentKlockInternalJvm.currentTime
    actual val hrNow: HRTimeSpan get() = CurrentKlockInternalJvm.hrNow
        actual fun localTimezoneOffsetMinutes(time: DateTime): TimeSpan = CurrentKlockInternalJvm.localTimezoneOffsetMinutes(time)
}

inline fun <T> TemporalKlockInternalJvm(impl: KlockInternalJvm, callback: () -> T): T {
    val old = CurrentKlockInternalJvm
    CurrentKlockInternalJvm = impl
    try {
        return callback()
    } finally {
        CurrentKlockInternalJvm = old
    }
}

var CurrentKlockInternalJvm = object : KlockInternalJvm {
}

interface KlockInternalJvm {
    val currentTime: Double get() = (System.currentTimeMillis()).toDouble()
    val hrNow: HRTimeSpan get() = HRTimeSpan.fromNanoseconds(System.nanoTime().toDouble())
    fun localTimezoneOffsetMinutes(time: DateTime): TimeSpan = TimeZone.getDefault().getOffset(time.unixMillisLong).milliseconds
}

actual typealias Serializable = java.io.Serializable
