package com.example.dicodingevent.util

open class Event<out T>(private val content: T) {

    @Suppress("MemberVisibilityCanBePrivate")
    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}