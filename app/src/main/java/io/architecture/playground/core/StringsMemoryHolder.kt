package io.architecture.playground.core

import android.app.ActivityManager
import android.util.LruCache
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StringsMemoryHolder @Inject constructor(activityManager: ActivityManager) { // TODO Implement in-memory cache for Strings, consider that nodeId used elsewhere

    private val sizeInBytes = activityManager.memoryClass * 1024 * 1024
    private val cache =
        LruCache<String, String>(sizeInBytes / 8)     //taking 8th part of total size


}
