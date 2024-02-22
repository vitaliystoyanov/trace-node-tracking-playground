package io.architecture.common

import io.architecture.runtime.logging.Logger


internal fun logCoroutineInfo(tag: String, msg: String) =
    Logger.debug(tag, "Running on: [${Thread.currentThread().name}] | $msg")
