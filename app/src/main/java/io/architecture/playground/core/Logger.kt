package io.architecture.playground.core

import android.util.Log

fun logCoroutineInfo(tag: String, msg: String) =
    Log.d(tag, "Running on: [${Thread.currentThread().name}] | $msg")
