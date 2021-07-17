package com.project.githubsample.utils

/*
* For checking nullability of any data object
* */
fun Any?.isNull(): Boolean = this == null

fun Any?.isNotNull(): Boolean = !this.isNull()

// For optimizing the Lazy operation
fun <T> fastLazy(initializer: () -> T): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE, initializer) //default is Synchronized

