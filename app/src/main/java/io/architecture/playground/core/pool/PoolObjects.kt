package io.architecture.playground.core.pool

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.util.Stack

open class PoolObjects<T : PoolMember> @AssistedInject constructor(@Assisted private val maxPoolSize: Int) {
    private val pool: Stack<T> = Stack()


    init {
        require(maxPoolSize > 0) { "The max pool size must be > 0" }
    }

    fun getMaxSize() = maxPoolSize

    @Synchronized
    fun acquire(): T {
        if (pool.size > 0) {
            return pool.pop()
        }
        throw RuntimeException("Pool can not be less than 0")
    }

    @Synchronized
    fun release(instance: T): Boolean {
        if (pool.size <= maxPoolSize) {
            instance.finalize()
            pool.push(instance)
            return true
        }
        return false
    }

    @AssistedFactory
    interface Factory<T : PoolMember> {
        fun create(maxSize: Int): PoolObjects<T>
    }

    override fun toString(): String {
        return "PoolObjects(size=${pool.size})"
    }
}




