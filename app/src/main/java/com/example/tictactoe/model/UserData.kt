package com.example.tictactoe.model

data class UserData(
    var invite:String = "null",
    var incomingMove:String = "null",
    var whoAccepted:String = "null"
){
    companion object{
        const val INVITE = "invite"
        const val INCOMING_MOVE = "incomingMove"
        const val WHO_ACCEPTED = "whoAccepted"
    }

}