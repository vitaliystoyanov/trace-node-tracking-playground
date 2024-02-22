package io.architecture.runtime.logging

class Logger {

    companion object {
        fun debug(tag: String, message: String) {
            println(message)
        }

        fun error(tag: String, message: String, throwable: Throwable) {
            println(message)
        }
    }
}