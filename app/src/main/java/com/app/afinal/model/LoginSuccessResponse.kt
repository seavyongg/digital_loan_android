package com.app.afinal.model

import com.google.gson.annotations.SerializedName

data class LoginSuccessResponse(
    @SerializedName("token")
    var accessToken : String? = null,
    @SerializedName("data")
    var user: User? = User()
){
    data class User(
        @SerializedName("id")
        var userId: Int? = 0,
        @SerializedName("email")
        var email: String? = "",
        @SerializedName("phone")
        var phone: String? = "",
        @SerializedName("phone_verified_at")
        var phoneVerifiedAt: String? = "",
        @SerializedName("telegram_chat_id")
        var telegramChatId: String? = "",
        @SerializedName("face_verified_at")
        var faceVerifiedAt: String? = "",
        @SerializedName("role")
        var role: Int? = 2, // Default role is 2 (user), can be changed to 1 for admin
        @SerializedName("status")
        var status: Int? = 1, // Default status is 1 (active), can be changed to 0 for inactive
        @SerializedName("created_at")
        var createdAt: String? = "",
        @SerializedName("updated_at")
        var updatedAt: String? = "",
        @SerializedName("profile")
        var profile: Profile? = Profile()

    ){
        data class Profile(
            @SerializedName("id")
            var id: Int? = 0,
            @SerializedName("first_name")
            var firstName: String? = "",
            @SerializedName("last_name")
            var lastname : String? = "",
            @SerializedName("image")
            var image: String? = null,
            @SerializedName("dob")
            var dob: String? = null,
            @SerializedName("gender")
            var gender : String? = "female",
            @SerializedName("address")
            var address: String? = "",
            @SerializedName("user_id")
            var userId: Int? = 2,
            @SerializedName("created_at")
            var createdAt: String? = "",
            @SerializedName("updated_at")
            var updatedAt: String? = "",
        )
    }
}
