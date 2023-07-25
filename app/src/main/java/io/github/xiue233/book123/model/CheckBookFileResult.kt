package io.github.xiue233.book123.model

import com.google.gson.annotations.SerializedName

data class CheckBookFileResult(
    @SerializedName("isExist")
    val isExist: Boolean = false
)