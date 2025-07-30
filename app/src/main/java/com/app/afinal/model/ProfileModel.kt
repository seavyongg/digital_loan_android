package com.app.afinal.model

import com.google.gson.annotations.SerializedName

data class ProfileModel(
    @SerializedName("data")
    var data : user = user()
) {
    data class user(
        @SerializedName("id")
        var userId: Int? = 0,
        @SerializedName("email")
        var email: String? = "",
        @SerializedName("phone")
        var phone: String? = "",
        @SerializedName("profile")
        var profile: Profile? = Profile()
    ){
        data class Profile(
            @SerializedName("id")
            var profileId : Int? = 0,
            @SerializedName("first_name")
            var firstName: String? = "",
            @SerializedName("last_name")
            var lastName: String? = "",
            @SerializedName("image")
            var image: String? = null,
            @SerializedName("dob")
            var dob: String? = null,
            @SerializedName("gender")
            var gender : String? = "",
            @SerializedName("address")
            var address: String? = "",
        )
    }
}