package com.example.tictactoe.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName ("name"        )  var name:         String?  = null,
    @SerializedName ("onlineStatus")  var onlineStatus: Boolean? = null,
    @SerializedName ("photoUrl"    )  var photoUrl:     String?  = null,
    @SerializedName ("id"          )  var id :          String?  = null
)
