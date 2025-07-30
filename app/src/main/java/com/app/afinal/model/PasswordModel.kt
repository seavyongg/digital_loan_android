package com.app.afinal.model

import com.google.gson.annotations.SerializedName

data class PasswordModel(
    @SerializedName("current_password")
    var currentPassword: String? = null,
    @SerializedName("password")
    var newPassword: String? = null,
)
