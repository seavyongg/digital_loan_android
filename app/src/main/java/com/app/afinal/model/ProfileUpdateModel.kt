package com.app.afinal.model

import android.net.Uri
import com.google.gson.annotations.SerializedName
import java.util.Date

data class ProfileUpdateModel(
    @SerializedName("first_name")
    var firstname : String? = "",
    @SerializedName("last_name")
    var lastname : String? = "",
    @SerializedName("dob")
    var dob: String? = null,
    @SerializedName("gender")
    var gender : String? = null,
    @SerializedName("address")
    var address: String? = null,
    @SerializedName("image")
    var image: Uri? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("phone")
    var phone: String? = null
) {
}