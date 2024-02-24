package io.architecture.core.runtime.configuration

import android.os.Build

actual val platform: IPlatform = object : IPlatform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}}"
}