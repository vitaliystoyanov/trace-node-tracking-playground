package io.architecture.core.runtime.configuration

import platform.UIKit.UIDevice

actual val platform: IPlatform = object : IPlatform {
    override val name: String =
        "Java ${UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion}"
}