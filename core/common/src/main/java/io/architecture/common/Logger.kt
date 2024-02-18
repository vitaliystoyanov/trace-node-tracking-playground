package io.architecture.common

import android.util.Log

internal fun logCoroutineInfo(tag: String, msg: String) =
    Log.d(tag, "Running on: [${Thread.currentThread().name}] | $msg")
