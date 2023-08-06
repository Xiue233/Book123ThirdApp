package io.github.xiue233.book123.util

inline fun String?.isNullOrEmpty(defaultValue: () -> String): String =
    if (this.isNullOrEmpty()) defaultValue() else this