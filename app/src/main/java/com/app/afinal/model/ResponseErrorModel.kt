package com.app.afinal.model

data class ResponseErrorModel(
    var error : Boolean?,
    var message : String?,
    var messages : Messages?,
    var status : Boolean = true

){
    data class Messages(
        var err : String?
    )
}
